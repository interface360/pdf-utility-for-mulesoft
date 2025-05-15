package org.mule.pdf.extension.internal.operation;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.mule.pdf.extension.api.PDFAttributes;
import org.mule.pdf.extension.internal.metadata.BinaryMetadataResolver;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.slf4j.Logger;

public class GetPagesOperation {
	private static final Logger LOGGER = getLogger(GetPagesOperation.class);


	@MediaType(value = MediaType.ANY, strict = false)
	@DisplayName("Get Pages")
	public Result<InputStream, PDFAttributes> splitPdfs(
			@DisplayName("PDF Data") @Content @TypeResolver(BinaryMetadataResolver.class) InputStream sourcePdf,
			@DisplayName("File Name") @Summary("Filename of the PDF document") String fileName,
			@DisplayName("Pages") @Summary("Use 1-based indexing for page numbers. i.e. 0 is not allowed.") @Example("1-5,8,10-13") String pages,
			StreamingHelper streamingHelper) throws Exception {

		String newFileName = FilenameUtils.removeExtension(fileName);
		try (PDDocument xheonSourcePdf = Loader.loadPDF(new RandomAccessReadBuffer(sourcePdf));
				PDDocument xheonNewPdfPage = new PDDocument();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			int totalPages = xheonSourcePdf.getNumberOfPages();

			for (Integer pageNumber : getPageNumbers(pages)) {
				if (pageNumber < totalPages) {
					xheonNewPdfPage.addPage(xheonSourcePdf.getPage(pageNumber));
				}
			}

			xheonNewPdfPage.save(outputStream);
			InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

			// Collect metadata
			PDFAttributes attributes = new PDFAttributes();
			attributes.setPageCount(xheonNewPdfPage.getNumberOfPages());
			attributes.setFileSize(outputStream.size());
			attributes.setFileName(newFileName + "_extracted_" + ".pdf");

			return Result.<InputStream, PDFAttributes>builder().output(inputStream)
					.mediaType(org.mule.runtime.api.metadata.MediaType.parse("application/pdf")).attributes(attributes)
					.build();
		}
	}

	private List<Integer> getPageNumbers(String pages) {

		ArrayList<Integer> pageNumberIndex = new ArrayList<>();

		for (String desiredPageRange : pages.split(",")) {
			if (desiredPageRange.contains("-")) {
				String[] range = desiredPageRange.split("-");
				int start = Integer.parseInt(range[0]) - 1;
				int end = Integer.parseInt(range[1]) - 1;
				for (int i = start; i <= end; i++) {
					pageNumberIndex.add(i);
				}
			} else {
				pageNumberIndex.add(Integer.parseInt(desiredPageRange) - 1);
			}
		}
		return pageNumberIndex;
	}

}

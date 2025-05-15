package org.mule.pdf.extension.internal.config;

import org.mule.pdf.extension.internal.param.DocumentParams;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

/**
 * This class represents an extension configuration, values set in this class
 * are commonly used across multiple
 * operations since they represent something core from the extension.
 */
public class PDFUtilityConfig {

    @ParameterGroup(name = "PDF Document Params")
    private DocumentParams documentParams;

    public DocumentParams getDocumentParams() {
        return documentParams;
    }
}

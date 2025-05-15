package org.mule.pdf.extension.internal;

import org.mule.pdf.extension.internal.operation.GetPagesOperation;
import org.mule.pdf.extension.internal.operation.SplitPdfOperation;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.sdk.api.annotation.JavaVersionSupport;
import org.mule.sdk.api.meta.JavaVersion;

/**
 * This is the main class of an extension, is the entry point from which
 * configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "xheon-pdf")
@Extension(name = "Utility PDF")
// @Configurations(PDFModuleConfiguration.class)
@JavaVersionSupport({ JavaVersion.JAVA_8, JavaVersion.JAVA_11, JavaVersion.JAVA_17 })
@Operations({ SplitPdfOperation.class, GetPagesOperation.class })
public class PDFModuleExtension {

}

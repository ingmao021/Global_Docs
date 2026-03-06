package com.empresa.docprocessor.domain.factory;

import com.empresa.docprocessor.domain.model.DocumentFormat;
import com.empresa.docprocessor.domain.parser.*;
import com.empresa.docprocessor.domain.validator.DocumentValidator;
import com.empresa.docprocessor.domain.validator.MexicoValidator;

/**
 * Fábrica concreta para procesamiento de documentos de México.
 */
public class MexicoProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentValidator createValidator() {
        return new MexicoValidator();
    }

    @Override
    public DocumentParser createParser(DocumentFormat format) {
        return switch (format) {
            case PDF -> new PdfParser();
            case DOC -> new DocParser();
            case DOCX -> new DocxParser();
            case MD -> new MdParser();
            case CSV -> new CsvParser();
            case TXT -> new TxtParser();
            case XLSX -> new XlsxParser();
        };
    }
}

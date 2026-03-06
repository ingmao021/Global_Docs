package com.empresa.docprocessor.domain.parser;

import com.empresa.docprocessor.domain.exception.DocumentProcessingException;
import com.empresa.docprocessor.domain.exception.ParseException;
import com.empresa.docprocessor.domain.model.Document;

/**
 * Parser simulado para archivos DOC.
 */
public class DocParser implements DocumentParser {

    @Override
    public void parse(Document document) throws DocumentProcessingException {
        try {
            ParserSimulation.simulateProcessing();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ParseException("Interrupción durante el procesamiento", e);
        }
    }
}

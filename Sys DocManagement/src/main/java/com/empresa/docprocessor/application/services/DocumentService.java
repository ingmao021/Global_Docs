package com.empresa.docprocessor.application.services;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.DocumentFormat;
import com.empresa.docprocessor.domain.model.DocumentType;
import com.empresa.docprocessor.domain.model.ProcessingResult;
import com.empresa.docprocessor.infrastructure.reader.FileReaderService;
import com.empresa.docprocessor.infrastructure.registry.CountryFactoryRegistry;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio orquestador para procesar un único documento.
 */
public class DocumentService {

    private final CountryFactoryRegistry factoryRegistry;
    private final FileReaderService fileReaderService;
    private final Logger logger = Logger.getLogger(DocumentService.class.getName());

    public DocumentService(CountryFactoryRegistry factoryRegistry, FileReaderService fileReaderService) {
        this.factoryRegistry = factoryRegistry;
        this.fileReaderService = fileReaderService;
    }

    /**
     * Procesa un único archivo con el país y tipo indicados.
     *
     * @param file         archivo a procesar
     * @param country      país del documento
     * @param documentType tipo de documento
     * @return resultado del procesamiento (éxito o fallo)
     */
    public ProcessingResult processDocument(File file, Country country, DocumentType documentType) {
        try {
            var document = fileReaderService.buildDocument(file, country, documentType);
            var factory = factoryRegistry.getFactory(country);
            return factory.process(document);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error procesando " + file.getName(), e);
            DocumentFormat format;
            try {
                format = fileReaderService.getFormatFromFile(file);
            } catch (Exception ignored) {
                format = DocumentFormat.PDF;
            }
            return ProcessingResult.failure(
                    file.getName(),
                    country,
                    documentType,
                    format,
                    e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName(),
                    0L
            );
        }
    }
}

package com.empresa.docprocessor.domain.factory;

import com.empresa.docprocessor.domain.exception.DocumentProcessingException;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentFormat;
import com.empresa.docprocessor.domain.model.ProcessingResult;
import com.empresa.docprocessor.domain.parser.DocumentParser;
import com.empresa.docprocessor.domain.validator.DocumentValidator;

/**
 * Fábrica abstracta que define el algoritmo de procesamiento (template method)
 * y delega la creación del validador y del parser en las subclases (Factory Method).
 */
public abstract class DocumentProcessorFactory {

    /**
     * Factory Method: crea el validador específico del país.
     */
    public abstract DocumentValidator createValidator();

    /**
     * Factory Method: crea el parser para el formato indicado.
     *
     * @param format formato del documento a parsear
     * @return parser capaz de procesar ese formato
     */
    public abstract DocumentParser createParser(DocumentFormat format);

    /**
     * Algoritmo base: valida el documento y luego lo parsea.
     * Mide el tiempo de procesamiento y devuelve el resultado.
     *
     * @param doc documento a procesar
     * @return resultado con éxito o fallo y mensaje
     */
    public ProcessingResult process(Document doc) {
        DocumentValidator validator = createValidator();
        DocumentParser parser = createParser(doc.documentFormat());

        long startMs = System.currentTimeMillis();
        try {
            validator.validate(doc);
            parser.parse(doc);
            long timeMs = System.currentTimeMillis() - startMs;
            return ProcessingResult.success(
                    doc.fileName(),
                    doc.country(),
                    doc.documentType(),
                    doc.documentFormat(),
                    timeMs
            );
        } catch (DocumentProcessingException e) {
            long timeMs = System.currentTimeMillis() - startMs;
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ProcessingResult.failure(
                    doc.fileName(),
                    doc.country(),
                    doc.documentType(),
                    doc.documentFormat(),
                    message,
                    timeMs
            );
        } catch (Exception e) {
            long timeMs = System.currentTimeMillis() - startMs;
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ProcessingResult.failure(
                    doc.fileName(),
                    doc.country(),
                    doc.documentType(),
                    doc.documentFormat(),
                    message,
                    timeMs
            );
        }
    }
}

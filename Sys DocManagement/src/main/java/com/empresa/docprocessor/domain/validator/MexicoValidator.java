package com.empresa.docprocessor.domain.validator;

import com.empresa.docprocessor.domain.exception.ValidationException;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentType;

/**
 * Validador para documentos de México.
 * Reglas: matriz tipo-formato, RFC (12-13 caracteres), prefijo SAT en facturas.
 */
public class MexicoValidator implements DocumentValidator {

    private static final String SAT_PREFIX = "SAT";
    private static final int RFC_MIN_LENGTH = 12;
    private static final int RFC_MAX_LENGTH = 13;

    @Override
    public void validate(Document document) throws ValidationException {
        if (!document.documentType().isCompatibleWith(document.documentFormat())) {
            throw new ValidationException("Formato " + document.documentFormat() + " no compatible con el tipo " + document.documentType());
        }
        if (document.documentType() == DocumentType.FACTURA_ELECTRONICA) {
            if (document.fileName() == null || !document.fileName().toUpperCase().contains(SAT_PREFIX)) {
                throw new ValidationException("Factura electrónica de México debe contener el prefijo SAT en el nombre del archivo");
            }
            validateRFCInFileName(document.fileName());
        }
    }

    /**
     * Valida que el nombre del archivo contenga un patrón RFC (12-13 caracteres alfanuméricos).
     */
    private void validateRFCInFileName(String fileName) throws ValidationException {
        String alphanumeric = fileName.replaceAll("[^A-Za-z0-9]", "");
        if (alphanumeric.length() < RFC_MIN_LENGTH || alphanumeric.length() > RFC_MAX_LENGTH) {
            throw new ValidationException("RFC inválido: debe tener entre 12 y 13 caracteres alfanuméricos");
        }
    }
}

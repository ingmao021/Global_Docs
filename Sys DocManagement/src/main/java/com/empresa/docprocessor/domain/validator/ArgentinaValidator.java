package com.empresa.docprocessor.domain.validator;

import com.empresa.docprocessor.domain.exception.ValidationException;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentType;

/**
 * Validador para documentos de Argentina.
 * Reglas: matriz tipo-formato, CUIT (11 dígitos), prefijo AFIP en facturas.
 */
public class ArgentinaValidator implements DocumentValidator {

    private static final String AFIP_PREFIX = "AFIP";
    private static final int CUIT_DIGITS = 11;

    @Override
    public void validate(Document document) throws ValidationException {
        if (!document.documentType().isCompatibleWith(document.documentFormat())) {
            throw new ValidationException("Formato " + document.documentFormat() + " no compatible con el tipo " + document.documentType());
        }
        if (document.documentType() == DocumentType.FACTURA_ELECTRONICA) {
            if (document.fileName() == null || !document.fileName().toUpperCase().contains(AFIP_PREFIX)) {
                throw new ValidationException("Factura electrónica de Argentina debe contener el prefijo AFIP en el nombre del archivo");
            }
            validateCUITInFileName(document.fileName());
        }
    }

    /**
     * Valida que el nombre del archivo contenga un patrón CUIT (11 dígitos).
     */
    private void validateCUITInFileName(String fileName) throws ValidationException {
        String digitsOnly = fileName.replaceAll("\\D", "");
        if (digitsOnly.length() < CUIT_DIGITS) {
            throw new ValidationException("CUIT inválido: se requieren 11 dígitos");
        }
    }
}

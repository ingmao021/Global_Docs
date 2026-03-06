package com.empresa.docprocessor.domain.validator;

import com.empresa.docprocessor.domain.exception.ValidationException;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentType;

/**
 * Validador para documentos de Chile.
 * Reglas: matriz tipo-formato, RUT (8 dígitos + verificador), prefijo SII en facturas.
 */
public class ChileValidator implements DocumentValidator {

    private static final String SII_PREFIX = "SII";
    private static final int RUT_DIGITS = 8;

    @Override
    public void validate(Document document) throws ValidationException {
        if (!document.documentType().isCompatibleWith(document.documentFormat())) {
            throw new ValidationException("Formato " + document.documentFormat() + " no compatible con el tipo " + document.documentType());
        }
        if (document.documentType() == DocumentType.FACTURA_ELECTRONICA) {
            if (document.fileName() == null || !document.fileName().toUpperCase().contains(SII_PREFIX)) {
                throw new ValidationException("Factura electrónica de Chile debe contener el prefijo SII en el nombre del archivo");
            }
            validateRUTInFileName(document.fileName());
        }
    }

    /**
     * Valida que el nombre del archivo contenga un patrón RUT (8 dígitos + dígito verificador o K).
     */
    private void validateRUTInFileName(String fileName) throws ValidationException {
        String normalized = fileName.toUpperCase().replaceAll("[^0-9K]", "");
        if (normalized.length() < 9) {
            throw new ValidationException("RUT inválido: se requieren 8 dígitos más dígito verificador (0-9 o K)");
        }
        String rutBase = normalized.substring(0, RUT_DIGITS);
        char verificationChar = normalized.charAt(RUT_DIGITS);
        if (!Character.isDigit(verificationChar) && verificationChar != 'K') {
            throw new ValidationException("RUT inválido: dígito verificador debe ser 0-9 o K");
        }
    }
}

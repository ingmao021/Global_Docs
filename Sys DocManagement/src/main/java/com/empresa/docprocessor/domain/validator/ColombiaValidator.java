package com.empresa.docprocessor.domain.validator;

import com.empresa.docprocessor.domain.exception.ValidationException;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentType;

/**
 * Validador para documentos de Colombia.
 * Reglas: matriz tipo-formato, NIT (9 dígitos + verificador), prefijo DIAN en facturas.
 */
public class ColombiaValidator implements DocumentValidator {

    private static final String DIAN_PREFIX = "DIAN";
    private static final int NIT_DIGITS = 9;

    @Override
    public void validate(Document document) throws ValidationException {
        if (!document.documentType().isCompatibleWith(document.documentFormat())) {
            throw new ValidationException("Formato " + document.documentFormat() + " no compatible con el tipo " + document.documentType());
        }
        if (document.documentType() == DocumentType.FACTURA_ELECTRONICA) {
            if (document.fileName() == null || !document.fileName().toUpperCase().contains(DIAN_PREFIX)) {
                throw new ValidationException("Factura electrónica de Colombia debe contener el prefijo DIAN en el nombre del archivo");
            }
            validateNITInFileName(document.fileName());
        }
    }

    /**
     * Valida que el nombre del archivo contenga un patrón NIT (9 dígitos + dígito verificador).
     */
    private void validateNITInFileName(String fileName) throws ValidationException {
        String digitsOnly = fileName.replaceAll("\\D", "");
        if (digitsOnly.length() < 10) {
            throw new ValidationException("NIT inválido: se requieren 9 dígitos más dígito verificador");
        }
        String nitBase = digitsOnly.substring(0, NIT_DIGITS);
        char verificationDigit = digitsOnly.charAt(NIT_DIGITS);
        if (!Character.isDigit(verificationDigit)) {
            throw new ValidationException("NIT inválido: dígito verificador debe ser numérico");
        }
    }
}

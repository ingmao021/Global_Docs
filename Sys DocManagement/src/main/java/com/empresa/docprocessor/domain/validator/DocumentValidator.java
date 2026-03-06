package com.empresa.docprocessor.domain.validator;

import com.empresa.docprocessor.domain.exception.DocumentProcessingException;
import com.empresa.docprocessor.domain.model.Document;

/**
 * Contrato para validadores de documentos por país.
 * Cada implementación aplica las reglas específicas (NIT, RFC, CUIT, RUT, prefijos, matriz tipo-formato).
 */
public interface DocumentValidator {

    /**
     * Valida el documento según las reglas del país y la matriz de compatibilidad tipo-formato.
     *
     * @param document documento a validar
     * @throws com.empresa.docprocessor.domain.exception.ValidationException cuando la validación falla
     */
    void validate(Document document) throws DocumentProcessingException;
}

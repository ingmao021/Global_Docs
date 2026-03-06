package com.empresa.docprocessor.application.dto;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.DocumentType;

/**
 * DTO para un documento fallido en procesamiento por lotes.
 */
public record ProcessingError(
        String fileName,
        Country country,
        DocumentType documentType,
        String message
) {
}

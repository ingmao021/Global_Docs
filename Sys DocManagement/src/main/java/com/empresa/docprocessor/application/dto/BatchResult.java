package com.empresa.docprocessor.application.dto;

import com.empresa.docprocessor.domain.model.ProcessingResult;

import java.util.List;

/**
 * Resultado del procesamiento por lotes: exitosos, fallidos y estadísticas.
 */
public record BatchResult(
        List<ProcessingResult> successful,
        List<ProcessingError> failed,
        int totalCount,
        int successCount,
        int failureCount,
        long totalTimeMs
) {
}

package com.empresa.docprocessor.domain.model;

import java.util.Set;

/**
 * Tipos de documento soportados por el sistema.
 * Incluye la matriz de compatibilidad tipo-formato (RF-3, RF-4).
 */
public enum DocumentType {
    FACTURA_ELECTRONICA(Set.of(DocumentFormat.PDF, DocumentFormat.XLSX, DocumentFormat.CSV)),
    CONTRATO_LEGAL(Set.of(DocumentFormat.PDF, DocumentFormat.DOCX, DocumentFormat.DOC, DocumentFormat.TXT, DocumentFormat.MD)),
    REPORTE_FINANCIERO(Set.of(DocumentFormat.PDF, DocumentFormat.DOCX, DocumentFormat.DOC, DocumentFormat.XLSX, DocumentFormat.CSV)),
    CERTIFICADO_DIGITAL(Set.of(DocumentFormat.PDF)),
    DECLARACION_TRIBUTARIA(Set.of(DocumentFormat.PDF, DocumentFormat.DOCX, DocumentFormat.DOC, DocumentFormat.XLSX, DocumentFormat.CSV));

    private final Set<DocumentFormat> compatibleFormats;

    DocumentType(Set<DocumentFormat> compatibleFormats) {
        this.compatibleFormats = compatibleFormats;
    }

    /**
     * Devuelve los formatos permitidos para este tipo de documento.
     */
    public Set<DocumentFormat> getCompatibleFormats() {
        return compatibleFormats;
    }

    /**
     * Indica si el formato dado es compatible con este tipo de documento.
     */
    public boolean isCompatibleWith(DocumentFormat format) {
        return compatibleFormats.contains(format);
    }
}

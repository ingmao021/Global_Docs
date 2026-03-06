package com.empresa.docprocessor.infrastructure.reader;

import com.empresa.docprocessor.domain.exception.UnsupportedFormatException;
import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.Document;
import com.empresa.docprocessor.domain.model.DocumentFormat;
import com.empresa.docprocessor.domain.model.DocumentType;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

/**
 * Servicio de infraestructura para obtener formato desde archivo y construir Document.
 */
public class FileReaderService {

    private static final Map<String, DocumentFormat> EXTENSION_TO_FORMAT = Map.ofEntries(
            Map.entry("pdf", DocumentFormat.PDF),
            Map.entry("doc", DocumentFormat.DOC),
            Map.entry("docx", DocumentFormat.DOCX),
            Map.entry("md", DocumentFormat.MD),
            Map.entry("csv", DocumentFormat.CSV),
            Map.entry("txt", DocumentFormat.TXT),
            Map.entry("xlsx", DocumentFormat.XLSX)
    );

    /**
     * Obtiene el formato del documento a partir de la extensión del archivo.
     *
     * @param file archivo
     * @return formato correspondiente
     * @throws UnsupportedFormatException si la extensión no es soportada
     */
    public DocumentFormat getFormatFromFile(File file) throws UnsupportedFormatException {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0 || lastDot == name.length() - 1) {
            throw new UnsupportedFormatException("Archivo sin extensión reconocible: " + name);
        }
        String ext = name.substring(lastDot + 1).toLowerCase();
        DocumentFormat format = EXTENSION_TO_FORMAT.get(ext);
        if (format == null) {
            throw new UnsupportedFormatException("Formato no soportado: " + ext);
        }
        return format;
    }

    /**
     * Construye un Document a partir del archivo, país y tipo.
     *
     * @param file         archivo
     * @param country      país
     * @param documentType tipo de documento
     * @return Document listo para procesar
     */
    public Document buildDocument(File file, Country country, DocumentType documentType) throws UnsupportedFormatException {
        DocumentFormat format = getFormatFromFile(file);
        Path path = file.toPath();
        return new Document(file.getName(), path, documentType, format, country);
    }
}

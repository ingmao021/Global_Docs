package com.empresa.docprocessor.infrastructure.logging;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.DocumentType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * Logger de procesamiento que escribe en archivo con formato:
 * [TIMESTAMP] [PAÍS] [TIPO] [ARCHIVO] [ESTADO] [MENSAJE]
 * Archivo: logs/processing_YYYY-MM-DD.log
 */
public class ProcessingLogger {

    private static final String LOG_DIR = "logs";
    private static final String LOG_PREFIX = "processing_";
    private static final String LOG_SUFFIX = ".log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Logger logger;
    private Handler fileHandler;
    private LocalDate currentLogDate;

    public ProcessingLogger() {
        this.logger = Logger.getLogger(ProcessingLogger.class.getName());
        this.logger.setUseParentHandlers(false);
        this.currentLogDate = null;
    }

    /**
     * Registra una línea con el formato requerido.
     *
     * @param level    nivel (INFO, WARN, ERROR)
     * @param country  país del documento
     * @param type     tipo de documento
     * @param fileName nombre del archivo
     * @param status   estado (ej. "Exitoso", "Fallido", "Validando")
     * @param message  mensaje detallado
     */
    public void log(Level level, Country country, DocumentType type, String fileName, String status, String message) {
        ensureHandlerForToday();
        String countryStr = country != null ? country.name() : "-";
        String typeStr = type != null ? type.name() : "-";
        String fileStr = fileName != null ? fileName : "-";
        String statusStr = status != null ? status : "-";
        String msgStr = message != null ? message : "";
        String line = String.format("[%s] [%s] [%s] [%s] [%s] [%s]",
                LocalDateTime.now().format(TIMESTAMP_FORMAT),
                countryStr,
                typeStr,
                fileStr,
                statusStr,
                msgStr);
        logger.log(level, line);
    }

    public void info(Country country, DocumentType type, String fileName, String status, String message) {
        log(Level.INFO, country, type, fileName, status, message);
    }

    public void warn(Country country, DocumentType type, String fileName, String status, String message) {
        log(Level.WARNING, country, type, fileName, status, message);
    }

    public void error(Country country, DocumentType type, String fileName, String status, String message) {
        log(Level.SEVERE, country, type, fileName, status, message);
    }

    private synchronized void ensureHandlerForToday() {
        LocalDate today = LocalDate.now();
        if (fileHandler == null || !today.equals(currentLogDate)) {
            if (fileHandler != null) {
                logger.removeHandler(fileHandler);
                fileHandler.close();
            }
            try {
                Path dir = Paths.get(LOG_DIR);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }
                String logFileName = LOG_PREFIX + today.format(DATE_FORMAT) + LOG_SUFFIX;
                Path logPath = dir.resolve(logFileName);
                fileHandler = new FileHandler(logPath.toAbsolutePath().toString(), true);
                fileHandler.setFormatter(new ProcessingLogFormatter());
                logger.addHandler(fileHandler);
                currentLogDate = today;
            } catch (IOException e) {
                logger.warning("No se pudo crear el archivo de log: " + e.getMessage());
            }
        }
    }

    /**
     * Formateador que escribe solo el mensaje (el mensaje ya incluye toda la línea).
     */
    private static class ProcessingLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return record.getMessage() + System.lineSeparator();
        }
    }
}

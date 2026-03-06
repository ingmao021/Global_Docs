package com.empresa.docprocessor.application.services;

import com.empresa.docprocessor.application.dto.BatchResult;
import com.empresa.docprocessor.application.dto.ProcessingError;
import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.DocumentType;
import com.empresa.docprocessor.domain.factory.DocumentProcessorFactory;
import com.empresa.docprocessor.domain.model.ProcessingResult;
import com.empresa.docprocessor.infrastructure.reader.FileReaderService;
import com.empresa.docprocessor.infrastructure.registry.CountryFactoryRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleConsumer;

/**
 * Servicio de procesamiento por lotes: chunks de hasta 500 documentos, pool de 4 hilos.
 */
public class BatchService {

    private static final int CHUNK_MAX_SIZE = 500;
    private static final int THREAD_POOL_SIZE = 4;

    private final CountryFactoryRegistry factoryRegistry;
    private final FileReaderService fileReaderService;

    public BatchService(CountryFactoryRegistry factoryRegistry, FileReaderService fileReaderService) {
        this.factoryRegistry = factoryRegistry;
        this.fileReaderService = fileReaderService;
    }

    /**
     * Procesa la lista de archivos por lotes.
     *
     * @param files            archivos a procesar
     * @param country          país de todos los documentos
     * @param documentType     tipo de documento
     * @param progressCallback se invoca con progreso en [0.0, 1.0] (puede ser null)
     * @return resultado con exitosos, fallidos y estadísticas
     */
    public BatchResult processBatch(
            List<File> files,
            Country country,
            DocumentType documentType,
            DoubleConsumer progressCallback) {

        if (files == null || files.isEmpty()) {
            return new BatchResult(List.of(), List.of(), 0, 0, 0, 0L);
        }

        List<List<File>> chunks = partition(files, CHUNK_MAX_SIZE);
        List<ProcessingResult> successful = Collections.synchronizedList(new ArrayList<>());
        List<ProcessingError> failed = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger processedCount = new AtomicInteger(0);
        int totalCount = files.size();
        long startMs = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        var factory = factoryRegistry.getFactory(country);

        try {
            List<Future<?>> futures = new ArrayList<>();
            for (List<File> chunk : chunks) {
                futures.add(executor.submit(() -> processChunk(chunk, country, documentType, factory, successful, failed, processedCount, totalCount, progressCallback)));
            }
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (ExecutionException e) {
                    throw new RuntimeException("Error en lote", e.getCause());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Lote interrumpido", e);
                }
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executor.shutdownNow();
            }
        }

        long totalTimeMs = System.currentTimeMillis() - startMs;
        return new BatchResult(
                List.copyOf(successful),
                List.copyOf(failed),
                totalCount,
                successful.size(),
                failed.size(),
                totalTimeMs
        );
    }

    private void processChunk(
            List<File> chunk,
            Country country,
            DocumentType documentType,
            DocumentProcessorFactory factory,
            List<ProcessingResult> successful,
            List<ProcessingError> failed,
            AtomicInteger processedCount,
            int totalCount,
            DoubleConsumer progressCallback) {

        for (File file : chunk) {
            try {
                var document = fileReaderService.buildDocument(file, country, documentType);
                ProcessingResult result = factory.process(document);
                if (result.success()) {
                    successful.add(result);
                } else {
                    failed.add(new ProcessingError(
                            result.fileName(),
                            result.country(),
                            result.documentType(),
                            result.message()));
                }
            } catch (Exception e) {
                failed.add(new ProcessingError(
                        file.getName(),
                        country,
                        documentType,
                        e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            }
            int processed = processedCount.incrementAndGet();
            if (progressCallback != null) {
                progressCallback.accept(processed / (double) totalCount);
            }
        }
    }

    private List<List<File>> partition(List<File> files, int maxChunkSize) {
        List<List<File>> chunks = new ArrayList<>();
        for (int i = 0; i < files.size(); i += maxChunkSize) {
            chunks.add(files.subList(i, Math.min(i + maxChunkSize, files.size())));
        }
        return chunks;
    }
}

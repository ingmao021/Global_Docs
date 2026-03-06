package com.empresa.docprocessor.domain.parser;

import java.util.Random;

/**
 * Utilidad para simular el tiempo de procesamiento de parsers (50-200 ms aleatorio).
 */
final class ParserSimulation {

    private static final int MIN_DELAY_MS = 50;
    private static final int MAX_DELAY_MS = 200;
    private static final Random RANDOM = new Random();

    private ParserSimulation() {
    }

    /**
     * Simula el trabajo de parseo con un delay aleatorio entre 50 y 200 ms.
     *
     * @throws InterruptedException si el hilo es interrumpido durante la espera
     */
    static void simulateProcessing() throws InterruptedException {
        int delayMs = MIN_DELAY_MS + RANDOM.nextInt(MAX_DELAY_MS - MIN_DELAY_MS + 1);
        Thread.sleep(delayMs);
    }
}

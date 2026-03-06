package com.empresa.docprocessor.presentation.viewmodels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel para el panel de procesamiento: progreso, contadores, estado y cancelación.
 */
public class ProcessingDashboardViewModel {

    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final IntegerProperty processedCount = new SimpleIntegerProperty(0);
    private final IntegerProperty totalCount = new SimpleIntegerProperty(0);
    private final StringProperty statusMessage = new SimpleStringProperty("");
    private final BooleanProperty cancelRequested = new SimpleBooleanProperty(false);
    private final BooleanProperty processingActive = new SimpleBooleanProperty(false);

    public DoubleProperty progressProperty() {
        return progress;
    }

    public double getProgress() {
        return progress.get();
    }

    public void setProgress(double value) {
        progress.set(value);
    }

    public IntegerProperty processedCountProperty() {
        return processedCount;
    }

    public int getProcessedCount() {
        return processedCount.get();
    }

    public void setProcessedCount(int value) {
        processedCount.set(value);
    }

    public IntegerProperty totalCountProperty() {
        return totalCount;
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public void setTotalCount(int value) {
        totalCount.set(value);
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage.get();
    }

    public void setStatusMessage(String message) {
        statusMessage.set(message != null ? message : "");
    }

    public BooleanProperty cancelRequestedProperty() {
        return cancelRequested;
    }

    public boolean isCancelRequested() {
        return cancelRequested.get();
    }

    public void setCancelRequested(boolean value) {
        cancelRequested.set(value);
    }

    public void requestCancel() {
        cancelRequested.set(true);
    }

    public BooleanProperty processingActiveProperty() {
        return processingActive;
    }

    public boolean isProcessingActive() {
        return processingActive.get();
    }

    public void setProcessingActive(boolean value) {
        processingActive.set(value);
    }

    /**
     * Reinicia el estado del panel para un nuevo lote.
     */
    public void resetForNewBatch(int total) {
        progress.set(0.0);
        processedCount.set(0);
        totalCount.set(total);
        statusMessage.set("");
        cancelRequested.set(false);
    }
}

package com.empresa.docprocessor.presentation.viewmodels;

import com.empresa.docprocessor.domain.model.DocumentType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

/**
 * ViewModel para la vista de carga: archivos seleccionados, tipo de documento y preview para la tabla.
 */
public class DocumentUploadViewModel {

    private final ObservableList<File> selectedFiles = FXCollections.observableArrayList();
    private final ObjectProperty<DocumentType> selectedDocumentType = new SimpleObjectProperty<>(DocumentType.FACTURA_ELECTRONICA);

    /**
     * Lista observable de archivos pendientes de procesar (preview en TableView).
     */
    public ObservableList<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(java.util.List<File> files) {
        selectedFiles.clear();
        if (files != null) {
            selectedFiles.addAll(files);
        }
    }

    public void addFiles(java.util.List<File> files) {
        if (files != null) {
            selectedFiles.addAll(files);
        }
    }

    public void clearFiles() {
        selectedFiles.clear();
    }

    public ObjectProperty<DocumentType> selectedDocumentTypeProperty() {
        return selectedDocumentType;
    }

    public DocumentType getSelectedDocumentType() {
        return selectedDocumentType.get();
    }

    public void setSelectedDocumentType(DocumentType type) {
        selectedDocumentType.set(type);
    }
}

package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.application.dto.BatchResult;
import com.empresa.docprocessor.domain.model.DocumentType;
import com.empresa.docprocessor.presentation.viewmodels.DocumentUploadViewModel;
import com.empresa.docprocessor.presentation.viewmodels.MainViewModel;
import com.empresa.docprocessor.presentation.viewmodels.ProcessingDashboardViewModel;
import com.empresa.docprocessor.presentation.viewmodels.ResultsViewModel;
import com.empresa.docprocessor.presentation.viewmodels.ErrorLogViewModel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la vista de carga: drag & drop, selector de tipo, preview y procesamiento por lote.
 */
public class DocumentUploadController {

    @FXML private ComboBox<DocumentType> documentTypeComboBox;
    @FXML private TableView<File> filesTableView;
    @FXML private TableColumn<File, String> fileNameColumn;
    @FXML private TableColumn<File, Long> sizeColumn;

    private AppContext appContext;

    public void init(AppContext appContext) {
        this.appContext = appContext;
        MainViewModel mainVm = appContext.getMainViewModel();
        DocumentUploadViewModel uploadVm = appContext.getDocumentUploadViewModel();

        documentTypeComboBox.getItems().setAll(DocumentType.values());
        documentTypeComboBox.valueProperty().bindBidirectional(uploadVm.selectedDocumentTypeProperty());

        fileNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue() != null ? cell.getValue().getName() : ""));
        sizeColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue() != null ? cell.getValue().length() : 0).asObject());
        filesTableView.setItems(uploadVm.getSelectedFiles());

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        filesTableView.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        filesTableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                appContext.getDocumentUploadViewModel().addFiles(db.getFiles());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    @FXML
    private void onSelectFiles() {
        javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
        chooser.setTitle("Seleccionar archivos");
        List<File> files = chooser.showOpenMultipleDialog(filesTableView.getScene().getWindow());
        if (files != null && !files.isEmpty()) {
            appContext.getDocumentUploadViewModel().addFiles(files);
        }
    }

    @FXML
    private void onLoadSampleData() {
        try {
            var files = appContext.getSampleDataGenerator().generateSampleFiles();
            appContext.getDocumentUploadViewModel().addFiles(files);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datos de prueba");
            alert.setHeaderText(null);
            alert.setContentText("Se cargaron " + files.size() + " documentos de muestra. Seleccione país y tipo, luego pulse \"Procesar lote\".");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudieron generar los datos de prueba: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onSelectFolder() {
        javafx.stage.DirectoryChooser chooser = new javafx.stage.DirectoryChooser();
        chooser.setTitle("Seleccionar carpeta");
        File dir = chooser.showDialog(filesTableView.getScene().getWindow());
        if (dir != null && dir.isDirectory()) {
            File[] list = dir.listFiles();
            if (list != null) {
                List<File> supported = new ArrayList<>();
                for (File f : list) {
                    if (f.isFile()) {
                        String name = f.getName().toLowerCase();
                        if (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx")
                                || name.endsWith(".md") || name.endsWith(".csv") || name.endsWith(".txt") || name.endsWith(".xlsx")) {
                            supported.add(f);
                        }
                    }
                }
                appContext.getDocumentUploadViewModel().addFiles(supported);
            }
        }
    }

    @FXML
    private void onProcessBatch() {
        DocumentUploadViewModel uploadVm = appContext.getDocumentUploadViewModel();
        MainViewModel mainVm = appContext.getMainViewModel();
        ProcessingDashboardViewModel dashboardVm = appContext.getDashboardViewModel();
        ResultsViewModel resultsVm = appContext.getResultsViewModel();
        ErrorLogViewModel errorLogVm = appContext.getErrorLogViewModel();

        List<File> files = new ArrayList<>(uploadVm.getSelectedFiles());
        if (files.isEmpty()) {
            return;
        }

        dashboardVm.resetForNewBatch(files.size());
        dashboardVm.setProcessingActive(true);
        dashboardVm.setStatusMessage("Validando...");

        Task<BatchResult> task = new Task<>() {
            @Override
            protected BatchResult call() {
                return appContext.getBatchService().processBatch(
                        files,
                        mainVm.getActiveCountry(),
                        uploadVm.getSelectedDocumentType(),
                        progress -> Platform.runLater(() -> {
                            dashboardVm.setProgress(progress);
                            dashboardVm.setProcessedCount((int) Math.round(progress * files.size()));
                            dashboardVm.setTotalCount(files.size());
                            dashboardVm.setStatusMessage("Procesando... " + (int)(progress * 100) + "%");
                        })
                );
            }
        };

        task.setOnSucceeded(e -> {
            BatchResult result = task.getValue();
            dashboardVm.setProcessingActive(false);
            dashboardVm.setProgress(1.0);
            dashboardVm.setProcessedCount(result.successCount() + result.failureCount());
            dashboardVm.setStatusMessage("Finalizado. Exitosos: " + result.successCount() + ", Fallidos: " + result.failureCount());
            resultsVm.addResults(result.successful());
            errorLogVm.addErrors(result.failed());
            mainVm.addProcessedCount(result.totalCount());
        });

        task.setOnFailed(e -> {
            dashboardVm.setProcessingActive(false);
            dashboardVm.setStatusMessage("Error: " + (task.getException() != null ? task.getException().getMessage() : "Desconocido"));
        });

        new Thread(task).start();
    }
}

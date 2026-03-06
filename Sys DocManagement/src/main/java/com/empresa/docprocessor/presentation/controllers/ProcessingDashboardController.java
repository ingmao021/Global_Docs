package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.presentation.viewmodels.ProcessingDashboardViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * Controlador del panel de procesamiento: ProgressBar, contador y botón cancelar.
 */
public class ProcessingDashboardController {

    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label counterLabel;
    @FXML private Button cancelButton;

    private AppContext appContext;

    public void init(AppContext appContext) {
        this.appContext = appContext;
        ProcessingDashboardViewModel vm = appContext.getDashboardViewModel();

        progressBar.progressProperty().bind(vm.progressProperty());
        statusLabel.textProperty().bind(vm.statusMessageProperty());
        counterLabel.textProperty().bind(
                vm.processedCountProperty().asString().concat(" / ").concat(vm.totalCountProperty().asString()));
        cancelButton.disableProperty().bind(vm.processingActiveProperty().not());
    }

    @FXML
    private void onCancel() {
        if (appContext != null) {
            appContext.getDashboardViewModel().requestCancel();
        }
    }
}

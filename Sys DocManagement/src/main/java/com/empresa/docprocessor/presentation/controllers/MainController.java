package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.presentation.viewmodels.MainViewModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Controlador de la vista principal: barra lateral, selector de país e indicador de documentos.
 */
public class MainController {

    @FXML private ComboBox<Country> countryComboBox;
    @FXML private Label documentsCountLabel;
    @FXML private VBox contentArea;

    private AppContext appContext;

    public void init(AppContext appContext) {
        this.appContext = appContext;
        MainViewModel vm = appContext.getMainViewModel();

        countryComboBox.getItems().setAll(Country.values());
        countryComboBox.valueProperty().bindBidirectional(vm.activeCountryProperty());
        documentsCountLabel.textProperty().bind(vm.documentsProcessedInSessionProperty().asString());
    }

    /**
     * Carga la vista de subida (puede invocarse desde fuera para vista inicial).
     */
    public void loadUploadView() {
        onNavigateUpload();
    }

    @FXML
    private void onNavigateUpload() {
        loadView("/views/DocumentUploadView.fxml", loader -> {
            DocumentUploadController c = loader.getController();
            c.init(appContext);
        });
    }

    @FXML
    private void onNavigateDashboard() {
        loadView("/views/ProcessingDashboardView.fxml", loader -> {
            ProcessingDashboardController c = loader.getController();
            c.init(appContext);
        });
    }

    @FXML
    private void onNavigateResults() {
        loadView("/views/ResultsView.fxml", loader -> {
            ResultsController c = loader.getController();
            c.init(appContext);
        });
    }

    @FXML
    private void onNavigateErrorLog() {
        loadView("/views/ErrorLogView.fxml", loader -> {
            ErrorLogController c = loader.getController();
            c.init(appContext);
        });
    }

    private void loadView(String fxmlPath, java.util.function.Consumer<FXMLLoader> onLoaded) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox view = loader.load();
            contentArea.getChildren().setAll(view);
            if (onLoaded != null) {
                onLoaded.accept(loader);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando vista: " + fxmlPath, e);
        }
    }
}

package com.empresa.docprocessor;

import com.empresa.docprocessor.application.services.BatchService;
import com.empresa.docprocessor.application.services.DocumentService;
import com.empresa.docprocessor.infrastructure.reader.FileReaderService;
import com.empresa.docprocessor.infrastructure.registry.CountryFactoryRegistry;
import com.empresa.docprocessor.infrastructure.samples.SampleDataGenerator;
import com.empresa.docprocessor.presentation.controllers.MainController;
import com.empresa.docprocessor.presentation.controllers.AppContext;
import com.empresa.docprocessor.presentation.viewmodels.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de arranque de la aplicación JavaFX.
 */
public class DocProcessorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        CountryFactoryRegistry factoryRegistry = new CountryFactoryRegistry();
        FileReaderService fileReaderService = new FileReaderService();
        DocumentService documentService = new DocumentService(factoryRegistry, fileReaderService);
        BatchService batchService = new BatchService(factoryRegistry, fileReaderService);

        MainViewModel mainViewModel = new MainViewModel();
        DocumentUploadViewModel documentUploadViewModel = new DocumentUploadViewModel();
        ProcessingDashboardViewModel dashboardViewModel = new ProcessingDashboardViewModel();
        ResultsViewModel resultsViewModel = new ResultsViewModel();
        ErrorLogViewModel errorLogViewModel = new ErrorLogViewModel();

        AppContext appContext = new AppContext(
                mainViewModel,
                documentUploadViewModel,
                dashboardViewModel,
                resultsViewModel,
                errorLogViewModel,
                documentService,
                batchService,
                new SampleDataGenerator()
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.init(appContext);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Sistema de Procesamiento de Documentos Empresariales");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();

        // Cargar vista de subida por defecto
        mainController.loadUploadView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.ProcessingResult;
import com.empresa.docprocessor.presentation.viewmodels.ResultsViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Controlador de la vista de resultados: TableView, filtros y exportar a CSV.
 */
public class ResultsController {

    @FXML private ComboBox<String> filterSuccessComboBox;
    @FXML private ComboBox<Country> filterCountryComboBox;
    @FXML private TableView<ProcessingResult> resultsTableView;
    @FXML private TableColumn<ProcessingResult, String> colFile;
    @FXML private TableColumn<ProcessingResult, String> colCountry;
    @FXML private TableColumn<ProcessingResult, String> colType;
    @FXML private TableColumn<ProcessingResult, String> colFormat;
    @FXML private TableColumn<ProcessingResult, String> colState;
    @FXML private TableColumn<ProcessingResult, Long> colTime;

    private AppContext appContext;

    public void init(AppContext appContext) {
        this.appContext = appContext;
        ResultsViewModel vm = appContext.getResultsViewModel();

        filterSuccessComboBox.getItems().setAll("Todos", "Exitosos", "Fallidos");
        filterSuccessComboBox.valueProperty().addListener((o, oldVal, newVal) -> {
            if ("Exitosos".equals(newVal)) vm.setFilterBySuccess(true);
            else if ("Fallidos".equals(newVal)) vm.setFilterBySuccess(false);
            else vm.setFilterBySuccess(null);
        });

        filterCountryComboBox.getItems().clear();
        filterCountryComboBox.getItems().add(null);
        filterCountryComboBox.getItems().addAll(Country.values());
        filterCountryComboBox.valueProperty().bindBidirectional(vm.filterByCountryProperty());

        colFile.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().fileName() : ""));
        colCountry.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().country().name() : ""));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().documentType().name() : ""));
        colFormat.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null && cell.getValue().documentFormat() != null ? cell.getValue().documentFormat().name() : ""));
        colState.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? (cell.getValue().success() ? "Exitoso" : "Fallido") : ""));
        colTime.setCellValueFactory(cell -> new javafx.beans.property.SimpleLongProperty(cell.getValue() != null ? cell.getValue().processingTimeMs() : 0).asObject());

        resultsTableView.setItems(vm.getFilteredResults());
    }

    @FXML
    private void onExportCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exportar resultados a CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = chooser.showSaveDialog(resultsTableView.getScene().getWindow());
        if (file == null) return;
        try (FileWriter w = new FileWriter(file, StandardCharsets.UTF_8)) {
            w.write("Archivo;País;Tipo;Formato;Estado;TiempoMs\n");
            for (ProcessingResult r : appContext.getResultsViewModel().getFilteredResults()) {
                w.write(String.format("%s;%s;%s;%s;%s;%d\n",
                        r.fileName(),
                        r.country(),
                        r.documentType(),
                        r.documentFormat() != null ? r.documentFormat() : "",
                        r.success() ? "Exitoso" : "Fallido",
                        r.processingTimeMs()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error exportando CSV", e);
        }
    }
}

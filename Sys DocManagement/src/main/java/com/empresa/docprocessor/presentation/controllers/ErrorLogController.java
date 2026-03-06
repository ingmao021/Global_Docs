package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.application.dto.ProcessingError;
import com.empresa.docprocessor.presentation.viewmodels.ErrorLogViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controlador del log de errores: TableView y buscador por nombre de archivo.
 */
public class ErrorLogController {

    @FXML private TextField searchTextField;
    @FXML private TableView<ProcessingError> errorsTableView;
    @FXML private TableColumn<ProcessingError, String> colFile;
    @FXML private TableColumn<ProcessingError, String> colCountry;
    @FXML private TableColumn<ProcessingError, String> colType;
    @FXML private TableColumn<ProcessingError, String> colDetail;

    private AppContext appContext;

    public void init(AppContext appContext) {
        this.appContext = appContext;
        ErrorLogViewModel vm = appContext.getErrorLogViewModel();

        searchTextField.textProperty().bindBidirectional(vm.searchTextProperty());

        colFile.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().fileName() : ""));
        colCountry.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().country().name() : ""));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().documentType().name() : ""));
        colDetail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue() != null ? cell.getValue().message() : ""));

        errorsTableView.setItems(vm.getFilteredErrors());
    }

    @FXML
    private void onClearLog() {
        if (appContext != null) {
            appContext.getErrorLogViewModel().clearSessionLog();
        }
    }
}

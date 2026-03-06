package com.empresa.docprocessor.presentation.viewmodels;

import com.empresa.docprocessor.application.dto.ProcessingError;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * ViewModel para la vista de log de errores: lista de errores y buscador por nombre de archivo.
 */
public class ErrorLogViewModel {

    private final ObservableList<ProcessingError> allErrors = FXCollections.observableArrayList();
    private final FilteredList<ProcessingError> filteredErrors = new FilteredList<>(allErrors, p -> true);

    private final StringProperty searchText = new SimpleStringProperty("");

    public ErrorLogViewModel() {
        searchText.addListener((o, oldVal, newVal) -> applySearchFilter());
    }

    /**
     * Lista filtrada para enlazar con la TableView.
     */
    public ObservableList<ProcessingError> getFilteredErrors() {
        return filteredErrors;
    }

    public ObservableList<ProcessingError> getAllErrors() {
        return allErrors;
    }

    public void addErrors(java.util.List<ProcessingError> errors) {
        if (errors != null) {
            allErrors.addAll(errors);
        }
    }

    /**
     * Limpia el log de errores de la sesión.
     */
    public void clearSessionLog() {
        allErrors.clear();
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }

    public String getSearchText() {
        return searchText.get();
    }

    public void setSearchText(String text) {
        searchText.set(text != null ? text : "");
    }

    private void applySearchFilter() {
        String search = (searchText.get() != null ? searchText.get() : "").trim().toLowerCase();
        if (search.isEmpty()) {
            filteredErrors.setPredicate(e -> true);
        } else {
            filteredErrors.setPredicate(e -> e.fileName() != null && e.fileName().toLowerCase().contains(search));
        }
    }
}

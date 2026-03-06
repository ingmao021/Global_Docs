package com.empresa.docprocessor.presentation.viewmodels;

import com.empresa.docprocessor.domain.model.Country;
import com.empresa.docprocessor.domain.model.ProcessingResult;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * ViewModel para la vista de resultados: lista de resultados con filtros por estado y país.
 */
public class ResultsViewModel {

    private final ObservableList<ProcessingResult> allResults = FXCollections.observableArrayList();
    private final FilteredList<ProcessingResult> filteredResults = new FilteredList<>(allResults, p -> true);

    /**
     * Filtro: null = todos, true = solo exitosos, false = solo fallidos.
     */
    private final ObjectProperty<Boolean> filterBySuccess = new SimpleObjectProperty<>(null);

    /**
     * Filtro por país: null = todos los países.
     */
    private final ObjectProperty<Country> filterByCountry = new SimpleObjectProperty<>(null);

    public ResultsViewModel() {
        filterBySuccess.addListener((o, oldVal, newVal) -> applyFilters());
        filterByCountry.addListener((o, oldVal, newVal) -> applyFilters());
    }

    /**
     * Lista filtrada para enlazar con la TableView.
     */
    public ObservableList<ProcessingResult> getFilteredResults() {
        return filteredResults;
    }

    /**
     * Lista completa (para añadir resultados sin duplicar lógica).
     */
    public ObservableList<ProcessingResult> getAllResults() {
        return allResults;
    }

    public void addResults(java.util.List<ProcessingResult> results) {
        if (results != null) {
            allResults.addAll(results);
        }
    }

    public void clearResults() {
        allResults.clear();
    }

    public ObjectProperty<Boolean> filterBySuccessProperty() {
        return filterBySuccess;
    }

    public Boolean getFilterBySuccess() {
        return filterBySuccess.get();
    }

    public void setFilterBySuccess(Boolean value) {
        filterBySuccess.set(value);
    }

    public ObjectProperty<Country> filterByCountryProperty() {
        return filterByCountry;
    }

    public Country getFilterByCountry() {
        return filterByCountry.get();
    }

    public void setFilterByCountry(Country country) {
        filterByCountry.set(country);
    }

    private void applyFilters() {
        Boolean successFilter = filterBySuccess.get();
        Country countryFilter = filterByCountry.get();
        filteredResults.setPredicate(r -> {
            if (successFilter != null && r.success() != successFilter) {
                return false;
            }
            if (countryFilter != null && r.country() != countryFilter) {
                return false;
            }
            return true;
        });
    }
}

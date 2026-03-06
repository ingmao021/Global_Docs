package com.empresa.docprocessor.presentation.viewmodels;

import com.empresa.docprocessor.domain.model.Country;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * ViewModel para la vista principal: país activo y contador de documentos procesados en sesión.
 */
public class MainViewModel {

    private final ObjectProperty<Country> activeCountry = new SimpleObjectProperty<>(Country.COLOMBIA);
    private final IntegerProperty documentsProcessedInSession = new SimpleIntegerProperty(0);

    public ObjectProperty<Country> activeCountryProperty() {
        return activeCountry;
    }

    public Country getActiveCountry() {
        return activeCountry.get();
    }

    public void setActiveCountry(Country country) {
        activeCountry.set(country);
    }

    public IntegerProperty documentsProcessedInSessionProperty() {
        return documentsProcessedInSession;
    }

    public int getDocumentsProcessedInSession() {
        return documentsProcessedInSession.get();
    }

    public void setDocumentsProcessedInSession(int value) {
        documentsProcessedInSession.set(value);
    }

    /**
     * Incrementa el contador de documentos procesados en la sesión.
     */
    public void addProcessedCount(int delta) {
        documentsProcessedInSession.set(documentsProcessedInSession.get() + delta);
    }

    /**
     * Reinicia el contador de la sesión.
     */
    public void resetSessionCount() {
        documentsProcessedInSession.set(0);
    }
}

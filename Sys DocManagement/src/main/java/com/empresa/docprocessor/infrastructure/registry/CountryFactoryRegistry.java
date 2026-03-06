package com.empresa.docprocessor.infrastructure.registry;

import com.empresa.docprocessor.domain.factory.*;
import com.empresa.docprocessor.domain.model.Country;

import java.util.Map;
import java.util.EnumMap;

/**
 * Registro de fábricas de procesamiento por país.
 * Inicializado al arranque; la selección de fábrica se hace por mapa, sin if/else ni switch.
 */
public class CountryFactoryRegistry {

    private final Map<Country, DocumentProcessorFactory> factoryByCountry;

    public CountryFactoryRegistry() {
        Map<Country, DocumentProcessorFactory> map = new EnumMap<>(Country.class);
        map.put(Country.COLOMBIA, new ColombiaProcessorFactory());
        map.put(Country.MEXICO, new MexicoProcessorFactory());
        map.put(Country.ARGENTINA, new ArgentinaProcessorFactory());
        map.put(Country.CHILE, new ChileProcessorFactory());
        this.factoryByCountry = Map.copyOf(map);
    }

    /**
     * Obtiene la fábrica asociada al país. Nunca usa ramas condicionales.
     *
     * @param country país del documento
     * @return fábrica de procesamiento para ese país
     * @throws IllegalArgumentException si el país no tiene fábrica registrada
     */
    public DocumentProcessorFactory getFactory(Country country) {
        DocumentProcessorFactory factory = factoryByCountry.get(country);
        if (factory == null) {
            throw new IllegalArgumentException("No hay fábrica registrada para el país: " + country);
        }
        return factory;
    }

    /**
     * Indica si existe una fábrica para el país dado.
     */
    public boolean supports(Country country) {
        return factoryByCountry.containsKey(country);
    }
}

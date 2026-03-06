package com.empresa.docprocessor.presentation.controllers;

import com.empresa.docprocessor.application.services.BatchService;
import com.empresa.docprocessor.application.services.DocumentService;
import com.empresa.docprocessor.infrastructure.samples.SampleDataGenerator;
import com.empresa.docprocessor.presentation.viewmodels.*;

/**
 * Contexto de la aplicación: ViewModels y servicios compartidos entre controladores.
 */
public class AppContext {

    private final MainViewModel mainViewModel;
    private final DocumentUploadViewModel documentUploadViewModel;
    private final ProcessingDashboardViewModel dashboardViewModel;
    private final ResultsViewModel resultsViewModel;
    private final ErrorLogViewModel errorLogViewModel;
    private final DocumentService documentService;
    private final BatchService batchService;
    private final SampleDataGenerator sampleDataGenerator;

    public AppContext(
            MainViewModel mainViewModel,
            DocumentUploadViewModel documentUploadViewModel,
            ProcessingDashboardViewModel dashboardViewModel,
            ResultsViewModel resultsViewModel,
            ErrorLogViewModel errorLogViewModel,
            DocumentService documentService,
            BatchService batchService,
            SampleDataGenerator sampleDataGenerator) {
        this.mainViewModel = mainViewModel;
        this.documentUploadViewModel = documentUploadViewModel;
        this.dashboardViewModel = dashboardViewModel;
        this.resultsViewModel = resultsViewModel;
        this.errorLogViewModel = errorLogViewModel;
        this.documentService = documentService;
        this.batchService = batchService;
        this.sampleDataGenerator = sampleDataGenerator;
    }

    public MainViewModel getMainViewModel() { return mainViewModel; }
    public DocumentUploadViewModel getDocumentUploadViewModel() { return documentUploadViewModel; }
    public ProcessingDashboardViewModel getDashboardViewModel() { return dashboardViewModel; }
    public ResultsViewModel getResultsViewModel() { return resultsViewModel; }
    public ErrorLogViewModel getErrorLogViewModel() { return errorLogViewModel; }
    public DocumentService getDocumentService() { return documentService; }
    public BatchService getBatchService() { return batchService; }
    public SampleDataGenerator getSampleDataGenerator() { return sampleDataGenerator; }
}

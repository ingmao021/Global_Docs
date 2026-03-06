package com.empresa.docprocessor.infrastructure.samples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Genera al menos 20 documentos de muestra con nombres realistas por país para probar el sistema.
 */
public class SampleDataGenerator {

    private static final String SAMPLE_DIR = "docprocessor_samples";

    /**
     * Genera archivos de prueba en un directorio temporal y devuelve la lista de archivos.
     * Nombres con prefijos y formatos válidos por país (DIAN, SAT, AFIP, SII para facturas).
     */
    public List<File> generateSampleFiles() throws IOException {
        Path baseDir = Path.of(System.getProperty("java.io.tmpdir")).resolve(SAMPLE_DIR);
        Files.createDirectories(baseDir);

        List<File> files = new ArrayList<>();

        // Colombia (5): facturas DIAN + NIT, otros tipos
        files.add(createFile(baseDir, "DIAN_factura_9012345670.pdf"));
        files.add(createFile(baseDir, "DIAN_factura_8023456789.pdf"));
        files.add(createFile(baseDir, "contrato_marco_colombia_2024.docx"));
        files.add(createFile(baseDir, "reporte_financiero_col.pdf"));
        files.add(createFile(baseDir, "declaracion_tributaria_col.xlsx"));

        // México (5): facturas SAT + RFC, otros
        files.add(createFile(baseDir, "SAT_factura_ABC123456789.pdf"));
        files.add(createFile(baseDir, "SAT_factura_XYZ9876543210.xlsx"));
        files.add(createFile(baseDir, "contrato_servicios_mex.docx"));
        files.add(createFile(baseDir, "reporte_trimestral_mex.csv"));
        files.add(createFile(baseDir, "certificado_digital_mex.pdf"));

        // Argentina (5): facturas AFIP + CUIT, otros
        files.add(createFile(baseDir, "AFIP_factura_20123456789.pdf"));
        files.add(createFile(baseDir, "AFIP_factura_27234567890.csv"));
        files.add(createFile(baseDir, "contrato_arrendamiento_arg.docx"));
        files.add(createFile(baseDir, "reporte_financiero_arg.xlsx"));
        files.add(createFile(baseDir, "declaracion_afip_arg.pdf"));

        // Chile (5): facturas SII + RUT, otros
        files.add(createFile(baseDir, "SII_factura_123456789.pdf"));
        files.add(createFile(baseDir, "SII_factura_98765432K.xlsx"));
        files.add(createFile(baseDir, "contrato_compraventa_chile.doc"));
        files.add(createFile(baseDir, "reporte_anual_chile.csv"));
        files.add(createFile(baseDir, "declaracion_sii_chile.txt"));

        // 5 más variados (formatos y tipos)
        files.add(createFile(baseDir, "documento_general_01.pdf"));
        files.add(createFile(baseDir, "anexo_contrato_02.docx"));
        files.add(createFile(baseDir, "datos_export_03.csv"));
        files.add(createFile(baseDir, "notas_04.txt"));
        files.add(createFile(baseDir, "readme_05.md"));

        return files;
    }

    private File createFile(Path baseDir, String fileName) throws IOException {
        Path path = baseDir.resolve(fileName);
        if (!Files.exists(path)) {
            Files.writeString(path, "");
        }
        return path.toFile();
    }
}

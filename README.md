# Sistema de Procesamiento de Documentos Empresariales

Java 21 + JavaFX 21 | Patrón Factory Method | Sin base de datos.

## Requisitos

- **JDK 21**
- **Maven 3.8+** (o usar el Maven embebido de tu IDE)

## Compilar

```bash
mvn clean compile
```

## Ejecutar

### Opción 1: Desde IntelliJ IDEA (Recomendado)

1. **Panel derecho** → Tab `Maven` (si no aparece: `View` → `Tool Windows` → `Maven`)
2. Expande: `docprocessor` → `Plugins` → `javafx` 
3. **Doble click en `run`** para ejecutar

### Opción 2: Terminal integrada de IntelliJ

1. `View` → `Tool Windows` → `Terminal` (o presiona `Alt + F12`)
2. Ejecuta:
```bash
mvn javafx:run
```

### Opción 3: Línea de comandos (CMD o PowerShell)

Navega a la carpeta del proyecto y ejecuta:
```bash
mvn clean javafx:run
```

**Nota:** Maven debe estar instalado o usar el Maven embebido del IDE.

## Datos de prueba

1. Abrir la aplicación.
2. En la vista **Cargar documentos**, pulsar **"Cargar datos de prueba (25 archivos)"**.
3. Elegir **País activo** en la barra lateral (Colombia, México, Argentina o Chile).
4. Seleccionar **Tipo de documento** (p. ej. Factura electrónica).
5. Pulsar **"Procesar lote"**.
6. Ver progreso en **Procesamiento**, resultados en **Resultados** y errores en **Log de errores**.

Los nombres de muestra incluyen prefijos por país (DIAN, SAT, AFIP, SII) para probar validaciones.

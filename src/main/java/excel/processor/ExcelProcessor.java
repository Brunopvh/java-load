package excel.processor;

import excel.leitor.ExcelReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que usa o padrão Adapter. Ela recebe um objeto ExcelReader e
 * implementa a lógica de processamento e filtragem (Adaptando a leitura à lógica).
 */
public class ExcelProcessor {

    private final ExcelReader reader;

    /**
     * Construtor que recebe a implementação de leitura (o Adapter).
     * @param reader A implementação específica de leitura (ex: XLSXReader).
     */
    public ExcelProcessor(ExcelReader reader) {
        this.reader = reader;
    }

    public void load(InputStream inputStream) throws IOException {
        this.reader.load(inputStream);
    }

    /**
     * Método principal que carrega, filtra e salva a nova planilha.
     * @param inputStream Fluxo de entrada do arquivo original.
     * @param sheetName Nome da planilha a ser filtrada.
     * @param columnIndex Índice da coluna para aplicar o filtro (base 0).
     * @param filterValue String de valor a ser buscada.
     * @param outputFilePath Caminho onde a nova planilha será salva.
     * @throws IOException Se houver erro de I/O.
     */
    public void filterAndSave(
            InputStream inputStream, String sheetName, int columnIndex, String filterValue, String outputFilePath
    ) throws IOException {

        try {
            // 1. Carregar o arquivo usando o Reader injetado
            this.load(inputStream);
            Sheet sheetEntrada = reader.getSheet(sheetName);

            if (sheetEntrada == null) {
                System.err.println("Planilha '" + sheetName + "' não encontrada.");
                return;
            }

            // 2. Criar o novo Workbook para os dados filtrados
            Workbook workbookSaida = new XSSFWorkbook();
            Sheet sheetSaida = workbookSaida.createSheet("Dados Filtrados - " + sheetName);

            int numLinhaSaida = 0;
            Iterator<Row> linhaIterator = sheetEntrada.iterator();

            // 3. Copia o cabeçalho
            if (linhaIterator.hasNext()) {
                copiarLinha(linhaIterator.next(), sheetSaida.createRow(numLinhaSaida++));
            }

            // 4. Lógica de Filtragem (Implementação do Adapter)
            while (linhaIterator.hasNext()) {
                Row linhaAtual = linhaIterator.next();
                Cell celulaFiltro = linhaAtual.getCell(columnIndex);

                if (celulaFiltro != null && celulaFiltro.getCellType() == CellType.STRING) {
                    String valorCelula = celulaFiltro.getStringCellValue();

                    if (valorCelula.toLowerCase().contains(filterValue.toLowerCase())) {
                        // Linha atende ao filtro, copia para a nova planilha
                        copiarLinha(linhaAtual, sheetSaida.createRow(numLinhaSaida++));
                    }
                }
            }

            // 5. Gravar o novo arquivo
            try (FileOutputStream arquivoSaida = new FileOutputStream(outputFilePath)) {
                workbookSaida.write(arquivoSaida);
            }
            workbookSaida.close();

            System.out.println("✅ Filtragem concluída. Novo arquivo salvo em: " + outputFilePath);

        } finally {
            // Garante que o recurso do leitor seja fechado
            reader.close();
        }
    }

    public Workbook getWorkbook() {
        return this.reader.getWorkbook();
    }
    /**
     * Método auxiliar (do seu código anterior) para copiar linhas.
     */
    private static void copiarLinha(Row linhaOriginal, Row novaLinha) {
        for (int i = 0; i < linhaOriginal.getLastCellNum(); i++) {
            Cell celulaOriginal = linhaOriginal.getCell(i);
            Cell novaCelula = novaLinha.createCell(i);

            if (celulaOriginal != null) {
                // ... (lógica de cópia de célula omitida para brevidade, mas deve ser a mesma de antes)
                // Usando apenas cópia de valor como exemplo:
                switch (celulaOriginal.getCellType()) {
                    case STRING:
                        novaCelula.setCellValue(celulaOriginal.getStringCellValue());
                        break;
                    case NUMERIC:
                        novaCelula.setCellValue(celulaOriginal.getNumericCellValue());
                        break;
                    default:
                        // Tratar outros tipos conforme necessário
                        break;
                }
            }
        }
    }

    // Obter as colunas
    public List<String> getHeader(Sheet sheet) {
        Row header = sheet.getRow(0);
        if (header == null) {
            throw new RuntimeException("A planilha não tem cabeçalho (linha 0)");
        }
        List<String> columns = new ArrayList<>();
        for (Cell cell : header) {
            columns.add(cell.getStringCellValue().trim());
        }
        return columns;
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "nan";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Lida com datas e números
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    // Formata números para evitar notação científica em inteiros
                    // ou usa um DecimalFormat se for o caso.
                    return String.valueOf((long)cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Tenta obter o valor resolvido da fórmula
                try {
                    // Você precisaria de um FormulaEvaluator aqui,
                    // mas para simplificar, apenas retorna a fórmula
                    return cell.getCellFormula();
                } catch (Exception e) {
                    return "";
                }
            case BLANK:
                return "nan";
            default:
                return "nan";
        }
    }
}
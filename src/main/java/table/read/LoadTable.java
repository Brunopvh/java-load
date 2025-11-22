package table.read;

import excel.leitor.XLSXReader;
import excel.processor.ExcelProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import type_utils.table.ColumnTable;
import type_utils.table.HeaderItem;
import type_utils.table.HeaderTable;


public class LoadTable {

    private ExcelProcessor excelProcessor;
    private String sheetName;
    private Map<String, Integer> indexColumns = new HashMap<String, Integer>();

    public LoadTable(InputStream fis, String sheetName) throws IOException {
        this.sheetName = sheetName;
        this.excelProcessor = new ExcelProcessor(new XLSXReader());
        this.excelProcessor.load(fis);
        List<String> h = this.getHeader();
        for(int i = 0; i < h.size(); i++){
            this.indexColumns.put(h.get(i), i);
        }
    }

    public Map<String, Integer> getIndexColumns(){
        return this.indexColumns;
    }

    // Obter as colunas
    public List<String> getHeader() {
        Sheet sheet = this.excelProcessor.getWorkbook().getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("A aba '" + sheetName + "' não existe");
        }

        Row header = sheet.getRow(0); // primeira linha
        if (header == null) {
            throw new RuntimeException("A planilha não tem cabeçalho (linha 0)");
        }

        List<String> columns = new ArrayList<>();
        for (Cell cell : header) {
            columns.add(cell.getStringCellValue().trim());
        }
        return columns;
    }

    public ColumnTable getColumnTable(String colName) {
        // 1. Obter a Planilha e o Índice da Coluna
        Sheet sheet = this.excelProcessor.getWorkbook().getSheet(sheetName);
        Integer index = this.indexColumns.get(colName);

        // Certifique-se de que o índice da coluna foi encontrado
        if (index == null) {
            throw new IllegalArgumentException("Coluna '" + colName + "' não encontrada no mapa de índices.");
        }

        ColumnTable col = new ColumnTable(new HeaderItem(colName, index));

        // 2. Loop Corrigido: Começa em 1 (para pular o cabeçalho) e vai até <= getLastRowNum()
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            // **Verifica se a linha existe (pode ser null se a linha foi deletada)**
            if (row == null) {
                continue;
            }

            // **USANDO O ÍNDICE CORRETO DA COLUNA (index) e NÃO o índice da linha (i)**
            Cell cell = row.getCell(index);

            // 3. Lógica para Tratar o Tipo da Célula (Resolve o IllegalStateException)
            String cellValue = getCellValueAsString(cell);

            // Adiciona o valor limpo (trim)
            col.add(cellValue.trim());
        }
        return col;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        String value = "";
        // Assume-se Apache POI 4.x+ (usa getCellType())
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                // Converte o valor numérico para String
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                // Tenta obter o resultado da fórmula como string, senão trata como Numeric
                try {
                    value = cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BLANK:
            default:
                value = "";
                break;
        }
        return value;
    }

}

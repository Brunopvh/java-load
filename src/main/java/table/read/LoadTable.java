package table.read;

import excel.leitor.ExcelReader;
import excel.leitor.XLSXReader;
import excel.processor.ExcelProcessor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;

public class LoadTable {

    private ExcelProcessor excelProcessor;
    private String sheetName;

    public LoadTable(InputStream fis, String sheetName) throws IOException {
        this.sheetName = sheetName;
        this.excelProcessor = new ExcelProcessor(new XLSXReader());
        this.excelProcessor.load(fis);
    }
    // Obter as colunas
    public List<String> getColumns() {
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

}

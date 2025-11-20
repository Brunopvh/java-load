package excel.leitor;

import excel.leitor.ExcelReader;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementação de leitura para arquivos XLSX (usando XSSFWorkbook).
 */
public class XLSXReader implements ExcelReader {

    private XSSFWorkbook workbook;

    @Override
    public void load(InputStream inputStream) throws IOException {
        // A linha que carrega todo o arquivo na memória
        this.workbook = new XSSFWorkbook(inputStream);
    }

    @Override
    public Workbook getWorkbook() {
        return this.workbook;
    }

    @Override
    public Sheet getSheet(int index) {
        if (this.workbook == null) {
            throw new IllegalStateException("O Workbook não foi carregado. Chame load() primeiro.");
        }
        return this.workbook.getSheetAt(index);
    }

    @Override
    public Sheet getSheet(String name) {
        if (this.workbook == null) {
            throw new IllegalStateException("O Workbook não foi carregado. Chame load() primeiro.");
        }
        return this.workbook.getSheet(name);
    }

    @Override
    public void close() throws IOException {
        if (this.workbook != null) {
            this.workbook.close();
            this.workbook = null;
        }
    }
}
package excel.leitor;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface que define os métodos essenciais para leitura de um arquivo Excel.
 */
public interface ExcelReader {

    /**
     * Carrega o Workbook a partir do InputStream.
     * @param inputStream O fluxo de entrada do arquivo Excel.
     * @throws IOException Se ocorrer um erro durante a leitura do arquivo.
     */
    void load(InputStream inputStream) throws IOException;

    /**
     * Retorna o Workbook carregado.
     * @return O objeto Workbook (XLSX, HSSF, etc.).
     */
    Workbook getWorkbook();

    /**
     * Retorna uma planilha específica pelo seu índice.
     * @param index O índice da planilha (começando em 0).
     * @return O objeto Sheet.
     */
    Sheet getSheet(int index);

    /**
     * Retorna uma planilha específica pelo seu nome.
     * @param name O nome da planilha.
     * @return O objeto Sheet.
     */
    Sheet getSheet(String name);

    /**
     * Fecha o Workbook e os recursos associados.
     * @throws IOException Se ocorrer um erro ao fechar.
     */
    void close() throws IOException;
}
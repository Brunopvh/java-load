package type_utils.save;
import type_utils.save.SaveTable;
import type_utils.DataTable;
import type_utils.table.ColumnTable;
import type_utils.table.HeaderItem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

// Workbook
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class SaveTableExcel implements SaveTable {

    private DataTable dataTable;

    public SaveTableExcel(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    public void toExcel(OutputStream os) throws IOException {

        //Map<HeaderItem, ColumnTable> finalExport = this.dataTable.getTable();
        // 1. Cria o Workbook (a planilha)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DadosExportados");

        // Obtém o esquema da tabela (HeaderTable)
        List<HeaderItem> headers = this.dataTable.getHeader().getHeaderItems();
        int columnCount = headers.size();

        // --- 2. Cria a Linha de Cabeçalho (Header Row) ---
        Row headerRow = sheet.createRow(0); // A primeira linha (índice 0)

        // Mapeia os dados da DataTable para as colunas do Excel
        for (int i = 0; i < columnCount; i++) {
            Cell cell = headerRow.createCell(i);
            // Assumindo que HeaderItem tem um método para obter o nome do cabeçalho
            // (ex: getName() ou toString())
            cell.setCellValue(headers.get(i).getName());
        }

        // --- 3. Preenche as Linhas de Dados ---
        // Encontra o número máximo de linhas de dados (o tamanho da coluna mais longa)
        int maxRows = 0;
        for (HeaderItem headerItem : headers) {
            ColumnTable col = this.dataTable.getColumn(headerItem);
            if (col != null && col.getValues().size() > maxRows) {
                maxRows = col.getValues().size();
            }
        }

        // Itera sobre cada LINHA DE DADOS (começando da linha 1 do Excel)
        for (int rowNum = 0; rowNum < maxRows; rowNum++) {
            Row row = sheet.createRow(rowNum + 1); // +1 porque a linha 0 é o cabeçalho

            // Itera sobre cada COLUNA (Cell)
            for (int colNum = 0; colNum < columnCount; colNum++) {
                HeaderItem headerItem = headers.get(colNum);
                ColumnTable col = this.dataTable.getColumn(headerItem);
                Cell cell = row.createCell(colNum);

                // Verifica se há um valor para essa célula na ColumnTable
                if (col != null && rowNum < col.getValues().size()) {
                    String value = col.getValues().get(rowNum);
                    // Define o valor da célula (sem formatação complexa)
                    cell.setCellValue(value);
                } else {
                    // Célula vazia ou nula
                    cell.setCellValue("");
                }
            }
        }

        // --- 4. Salva o Workbook no OutputStream ---
        try {
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Fecha o Workbook para liberar recursos
            workbook.close();
        }

    }

    @Override
    public void toJson(OutputStream os) {

    }
}

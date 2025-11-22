package type_utils;

import org.apache.poi.ss.usermodel.*;
import type_utils.table.*;
import type_utils.DataTable;
import excel.leitor.ExcelReader;
import excel.leitor.XLSXReader;
import excel.processor.ExcelProcessor;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataTableExcel implements DataTable {

    private HeaderTable headerTable;
    private Map<HeaderItem, ColumnTable> mapTable;

    public DataTableExcel(List<ColumnTable> bodyColumns){
        this.headerTable = new HeaderTable();
        this.mapTable = new HashMap<HeaderItem, ColumnTable>();
        for(ColumnTable col : bodyColumns){
            this.headerTable.addHeaderItem(col.getColName());
            this.mapTable.put(col.getColName(), col);
        }
    }

    public DataTableExcel(){
        this.headerTable = new HeaderTable();
        this.mapTable = new HashMap<HeaderItem, ColumnTable>();
    }

    @Override
    public void updateColumn(ColumnTable columnTable) {
        this.mapTable.put(columnTable.getColName(), columnTable);
    }

    @Override
    public ColumnTable getColumn(HeaderItem colName) {
        return this.mapTable.get(colName);
    }

    @Override
    public Map<HeaderItem, ColumnTable> getTable() {
        return this.mapTable;
    }

    @Override
    public HeaderTable getHeader() {
        return this.headerTable;
    }

    /**
     * Cria uma instância de DataTableExcel lendo os dados de um InputStream.
     * @param inputStream O stream de dados do arquivo Excel.
     * @return Uma nova instância de DataTableExcel.
     * @throws Exception Se houver erro durante a leitura do arquivo.
     */
    public static DataTableExcel createFromExcel(InputStream inputStream, int sheetIndex) throws Exception {
        // 1. Instanciar o leitor/processador de Excel.
        // As classes ExcelProcessor e ExcelReader são supostas.
        // Adapte esta linha conforme a sua API real de Excel.
        ExcelReader rd = new XLSXReader();
        ExcelProcessor processor = new ExcelProcessor(rd);
        processor.load(inputStream);
        Workbook wb = processor.getWorkbook();
        Sheet s = wb.getSheetAt(sheetIndex);
        List<String> header = processor.getHeader(s);
        //HeaderTable headerTable = new HeaderTable();
        int total = header.size();
        List<ColumnTable> columns = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            HeaderItem headerItem = new HeaderItem(header.get(i), i);
            columns.add(new ColumnTable(headerItem));
        }

        Iterator<Row> iter = s.rowIterator();
        if (iter.hasNext()) {
            iter.next(); // Pula a linha de cabeçalho (assumindo que o iterador ainda não avançou)
        }

        // Itera sobre as linhas restantes (os dados)
        while (iter.hasNext()) {
            Row row = iter.next();
            // Obtém o iterador de células da linha atual
            Iterator<Cell> cellIter = row.cellIterator();

            int columnIndex = 0;

            // Itera sobre as células da linha
            while (cellIter.hasNext() && columnIndex < total) {
                Cell cell = cellIter.next();

                // 1. Obtém o valor da célula como String
                // É essencial ter uma função robusta para converter a célula em String,
                // já que o POI retorna diferentes tipos (número, string, data, etc.).
                // Usarei um método auxiliar (getCellValueAsString) para demonstrar isso.
                String cellValue = processor.getCellValueAsString(cell);

                // 2. Adiciona o valor à ColumnTable correspondente ao índice
                // O índice da célula (columnIndex) é o mesmo índice da ColumnTable na lista 'columns'.
                columns.get(columnIndex).add(cellValue);
                columnIndex++;
            }
        }

        // 3. Criar e retornar o objeto DataTableExcel.
        return new DataTableExcel(columns);
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
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
                return "";
            default:
                return "";
        }
    }
}

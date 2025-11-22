package type_utils.table;

import java.util.ArrayList;
import java.util.List;
import type_utils.table.HeaderItem;
import type_utils.table.HeaderTable;

public class ColumnTable {

    private HeaderItem colName;
    private List<String> values = new ArrayList<String>();

    public ColumnTable(HeaderItem colName, List<String> values) {
        this.colName = colName;
        this.values = values;
    }

    public ColumnTable(HeaderItem colName) {
        this.colName = colName;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public HeaderItem getColName() {
        return this.colName;
    }

    public void setColName(HeaderItem colName) {
        this.colName = colName;
    }

    public void add(String val) {
        this.values.add(val);
    }

    public void addAll(List<String> val) {
        this.values.addAll(val);
    }

    public void update(int index, String val) {
        this.values.set(index, val);
    }
}

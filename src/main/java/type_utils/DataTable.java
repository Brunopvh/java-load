package type_utils;

import type_utils.table.HeaderTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import type_utils.table.*;


public interface DataTable {

    public void updateColumn(ColumnTable columnTable);
    public ColumnTable getColumn(HeaderItem colName);
    public HeaderTable getHeader();
    public Map<HeaderItem, ColumnTable> getTable();
}

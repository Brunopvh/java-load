package type_utils.table;
import java.util.ArrayList;
import java.util.List;

import excel.processor.ExcelProcessor;
import type_utils.table.HeaderItem;
import java.util.Map;
import java.util.HashMap;

public class HeaderTable {

    private List<HeaderItem> headerItems;

    public HeaderTable(List<HeaderItem> headerItems) {
        this.headerItems = headerItems;
    }

    public HeaderTable() {
        this.headerItems = new ArrayList<HeaderItem>();
    }

    public List<HeaderItem> getHeaderItems() {
        return headerItems;
    }

    public void setHeaderItems(List<HeaderItem> headerItems) {
        this.headerItems = headerItems;
    }

    public void addHeaderItem(HeaderItem headerItem) {
        this.headerItems.add(headerItem);
    }

    public void deleteHeaderItem(HeaderItem headerItem) {
        this.headerItems.remove(headerItem);
    }

    public void replaceHeaderItem(HeaderItem headerItem, Integer index) {
        this.headerItems.set(index, headerItem);
    }
}

package type_utils.table;

public class HeaderItem {
    private String name;
    private Integer positionIndex;

    public HeaderItem(String name, Integer positionIndex) {
        this.name = name;
        this.positionIndex = positionIndex;
    }
    public String getName() {
        return name;
    }

    public Integer getPositionIndex() {
        return positionIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }
}

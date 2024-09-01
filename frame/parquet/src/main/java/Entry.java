import java.io.Serializable;

public class Entry implements Serializable {

    private String stringCol;
    private Integer intCol;
    private Double doubleCol;

    public Entry() {
    }

    public Entry(String stringCol, Integer intCol, Double doubleCol) {
        this.stringCol = stringCol;
        this.intCol = intCol;
        this.doubleCol = doubleCol;
    }

    public String getStringCol() {
        return stringCol;
    }

    public void setStringCol(String stringCol) {
        this.stringCol = stringCol;
    }

    public Integer getIntCol() {
        return intCol;
    }

    public void setIntCol(Integer intCol) {
        this.intCol = intCol;
    }

    public Double getDoubleCol() {
        return doubleCol;
    }

    public void setDoubleCol(Double doubleCol) {
        this.doubleCol = doubleCol;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "stringCol='" + stringCol + '\'' +
                ", intCol=" + intCol +
                ", doubleCol=" + doubleCol +
                '}';
    }
}

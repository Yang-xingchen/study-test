package studytest.format.model;

import java.io.Serializable;

public class TestModel implements Serializable {

    private String src;

    private int type;

    private String value;

    public String getSrc() {
        return src;
    }

    public TestModel setSrc(String src) {
        this.src = src;
        return this;
    }

    public int getType() {
        return type;
    }

    public TestModel setType(int type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TestModel setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "src='" + src + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}

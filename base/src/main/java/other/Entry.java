package other;

import java.io.Serializable;

public class Entry {

    private boolean useConstructor;

    private long aLong;

    private int integer = 1;

    private double aDouble;

    private String string;

    public Entry() {
        useConstructor = true;
    }

    public boolean isUseConstructor() {
        return useConstructor;
    }

    public Entry setUseConstructor(boolean useConstructor) {
        this.useConstructor = useConstructor;
        return this;
    }

    public long getaLong() {
        return aLong;
    }

    public Entry setaLong(long aLong) {
        this.aLong = aLong;
        return this;
    }

    public int getInteger() {
        return integer;
    }

    public Entry setInteger(int integer) {
        this.integer = integer;
        return this;
    }

    public double getaDouble() {
        return aDouble;
    }

    public Entry setaDouble(double aDouble) {
        this.aDouble = aDouble;
        return this;
    }

    public String getString() {
        return string;
    }

    public Entry setString(String string) {
        this.string = string;
        return this;
    }

}

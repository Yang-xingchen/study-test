package other;

import java.io.Serializable;

public class Entry implements Serializable, Cloneable {


    private static int useConstructorCount = 0;

    private long aLong;

    private int integer = 1;

    private double aDouble;

    private String string;

    public static int getUseConstructorCount() {
        return useConstructorCount;
    }

    public Entry() {
        useConstructorCount++;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        return clone;
    }

}

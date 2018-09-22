package proxy;

public class Service1 implements Service{
    private String name;

    public Service1() {
    }

    public Service1(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Service1 setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void print() {
        System.out.println(name);
    }
}

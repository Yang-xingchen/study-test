package proxy;

import java.util.stream.Stream;

public class Service2 implements Service {

    private int id;

    public int getId() {
        return id;
    }

    public Service2 setId(int id) {
        this.id = id;
        return this;
    }

    public Service2() {

    }

    public Service2(int id) {

        this.id = id;
    }

    @Override
    public void print() {
        Stream.of(Thread.currentThread().getStackTrace()).forEach(System.out::println);
    }
}

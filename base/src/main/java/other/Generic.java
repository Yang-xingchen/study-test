package other;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Generic {

    @Test
    public void extendsTest() {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        List<? extends Fruit> fruits = apples;
        // 编译失败
//        fruits.add(new Apple());
        Fruit fruit = fruits.get(0);
    }

    @Test
    public void superTest() {
        List<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit());
        List<? super Fruit> objects = new ArrayList<>();
        objects.add(new Apple());
        // 只能是Object
        Object object = objects.get(0);
    }

    private class Fruit {}

    private class Apple extends Fruit {}

}

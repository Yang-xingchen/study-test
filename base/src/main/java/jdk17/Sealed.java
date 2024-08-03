package jdk17;

/**
 * 加sealed必须有子类
 * permits必须加sealed，且子类只能是列表内
 * sealed的子类可以是下述类型:
 *  1. final, 无子类
 *  2. sealed-permits, 有子类且子类需在其permits里
 *  3. sealed， 必须有子类
 *  4. non-sealed，子类数量无限制
 */
public class Sealed {

    /**
     * 使用permits必须加sealed
     * 子类只能是Child1, Child2, Child3, Child4, 不能是间接子类
     */
    public sealed class Parent permits Child1, Child2, Child3, Child4 {

    }

//    // 'Child0' is not allowed in the sealed hierarchy
//    public final class Child0 extends Parent {
//
//    }

    /**
     * 子类允许final
     */
    public final class Child1 extends Parent {

    }

    /**
     * 子类可以有子类，可以限定
     */
    public sealed class Child2 extends Parent permits GrandSon21 {

    }

    /**
     * 子类的子类无需在父类permits里
     */
    public final class GrandSon21 extends Child2 {

    }

    /**
     * 子类的子类可以无限定
     * sealed必须有子类
     */
    public sealed class Child3 extends Parent {

    }

    public final class GrandSon31 extends Child3 {

    }

    /**
     * 子类的子类可以无密封
     * non-sealed可以子类数量无限制
     */
    public non-sealed class Child4 extends Parent {

    }

//    public final class GrandSon41 extends Child4 {
//
//    }

}

package methodHandles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.*;

public class MethodHandlesTest {

    @Test
    public void empty() throws Throwable {
        // zero/empty 总是返回默认值
        // zero返回MethodHandle为无参方法类型
        Assertions.assertEquals(0, (int) zero(int.class).invokeExact());
        Assertions.assertEquals(0L, (long) zero(long.class).invokeExact());
        Assertions.assertEquals(0.0, (double) zero(double.class).invokeExact());
        Assertions.assertEquals(null, zero(Object.class).invokeExact());
        // empty返回MethodHandle为参数内的方法类型，不执行方法且忽略参数
        Assertions.assertEquals(0, (int) MethodHandles.empty(MethodType.methodType(int.class)).invokeExact());
        Assertions.assertEquals(0, (int) MethodHandles.empty(MethodType.methodType(int.class, int.class)).invokeExact(1));
        Assertions.assertEquals(0L, (long) MethodHandles.empty(MethodType.methodType(long.class)).invokeExact());
        Assertions.assertEquals(0.0, (double) MethodHandles.empty(MethodType.methodType(double.class)).invokeExact());
        Assertions.assertEquals(null, MethodHandles.empty(MethodType.methodType(Object.class)).invokeExact());
    }

    @Test
    public void constant() throws Throwable {
        // identity返回参数值，即 Function.identity()
        Assertions.assertEquals("hello", (String) identity(String.class).invokeExact("hello"));
        Assertions.assertEquals("world", (String) identity(String.class).invokeExact("world"));
        // constant总是返回同一个值
        MethodHandle helloMethodHandel = MethodHandles.constant(String.class, "hello");
        Assertions.assertEquals("hello", (String) helloMethodHandel.invokeExact());
        Assertions.assertEquals("hello", (String) helloMethodHandel.invokeExact());
    }

    @Test
    public void array() throws Throwable {
        int[] arr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        // arrayLength: 获取数组长度
        Assertions.assertEquals(arr.length, (int) arrayLength(int[].class).invokeExact(arr));
        // arrayElementGetter: 访问数组值
        Assertions.assertEquals(arr[0], (int) arrayElementGetter(int[].class).invokeExact(arr, 0));
        // arrayConstructor: 创建数组
        int[] arr1 = (int[]) arrayConstructor(int[].class).invokeExact(10);
        // arrayElementSetter: 设置值
        MethodHandle arrayElementSetter = arrayElementSetter(int[].class);
        for (int i = 0; i < 10; i++) {
            // 参数: 数组实例，索引，值
            arrayElementSetter.invokeExact(arr1, i, i + 1);
        }
        Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr1);
//        MethodHandles.arrayElementVarHandle()
    }

    @Test
    public void adapter() throws Throwable{
        MethodHandle arg0 = lookup().findStatic(MethodHandlesTest.class, "arg", MethodType.methodType(void.class));
        MethodHandle arg1 = lookup().findStatic(MethodHandlesTest.class, "arg", MethodType.methodType(void.class, String.class));
        MethodHandle arg2 = lookup().findStatic(MethodHandlesTest.class, "arg", MethodType.methodType(void.class, String.class, String.class));
        MethodHandle ret0 = lookup().findStatic(MethodHandlesTest.class, "ret", MethodType.methodType(String.class));
        MethodHandle ret1 = lookup().findStatic(MethodHandlesTest.class, "ret", MethodType.methodType(String.class, String.class));
        MethodHandle ret2 = lookup().findStatic(MethodHandlesTest.class, "ret", MethodType.methodType(String.class, String.class, String.class));
        MethodHandle ret3 = lookup().findStatic(MethodHandlesTest.class, "ret", MethodType.methodType(String.class, String.class, String.class, String.class));

        // insertArguments: 对返回的MethodHandle增加参数，其值为方法的参数
        MethodHandle insertArg1 = insertArguments(arg1, 0, "test");
        // MethodHandlesTest.arg("test")
        insertArg1.invokeExact();
        MethodHandle insertArg2 = insertArguments(arg2, 0, "test1", "test2");
        // MethodHandlesTest.arg("test1", "test2")
        insertArg2.invokeExact();
        MethodHandle insertArg22 = insertArguments(arg2, 1, "test2");
        // MethodHandlesTest.arg("test1", "test2")
        insertArg22.invokeExact("test1");

        // dropArguments: 对返回的MethodHandle移除参数
        MethodHandle dropArg0 = dropArguments(arg0, 0, String.class);
        // MethodHandlesTest.arg()
        dropArg0.invoke("boom!");
        MethodHandle dropArg01 = dropArguments(arg0, 0, String.class, String.class);
        // MethodHandlesTest.arg()
        dropArg01.invoke("ping!", "boom!");
        MethodHandle dropArg2 = dropArguments(arg2, 1, String.class);
        // MethodHandlesTest.arg("test1", "test2")
        dropArg2.invoke("test1", "boom!", "test2");

        // dropReturn: 对返回的MethodHandle忽略返回值
        MethodHandle dropRet = dropReturn(ret1);
        Assertions.assertEquals(MethodType.methodType(void.class, String.class), dropRet.type());
        Assertions.assertEquals(MethodType.methodType(String.class, String.class), ret1.type());

        // permuteArguments: 对返回的MethodHandle调整参数位置, reorder为返回的MethodHandle参数位置对应原MethodHandle参数列表
        MethodHandle permuteArg210 = permuteArguments(arg2, MethodType.methodType(void.class, String.class, String.class), 1, 0);
        // MethodHandlesTest.arg("test1", "test2")
        permuteArg210.invoke("test2", "test1");
        MethodHandle permuteArg211 = permuteArguments(arg2, MethodType.methodType(void.class, String.class), 0, 0);
        // MethodHandlesTest.arg("test2", "test2")
        permuteArg211.invoke("test2");

        // filterArguments: 对返回的MethodHandle的参数调用方法
        MethodHandle toUpperCase = lookup().findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class));
        MethodHandle filterArg1 = filterArguments(arg1, 0, toUpperCase);
        // MethodHandlesTest.arg("test".toUpperCase())
        filterArg1.invoke("test");
        MethodHandle filterArg2 = filterArguments(arg2, 1, toUpperCase);
        // MethodHandlesTest.arg("test1", "test2".toUpperCase())
        filterArg2.invoke("test1", "test2");
        MethodHandle concat = insertArguments(lookup().findVirtual(String.class, "concat", MethodType.methodType(String.class, String.class)), 0, "test");
        MethodHandle filterArg22 = filterArguments(arg2, 0, toUpperCase, concat);
        // MethodHandlesTest.arg("test1".toUpperCase(), "test".concat("2"))
        filterArg22.invoke("test1", "2");

        // filterReturnValue: 对返回的MethodHandle的结果调用方法
        MethodHandle filterRet01 = filterReturnValue(ret0, ret1);
        // MethodHandlesTest.ret(MethodHandlesTest.ret())
        Assertions.assertEquals("ret1[ret0]", (String) filterRet01.invokeExact());

        // foldArguments: 折叠参数方法，把第二个参数的结果作为第一个参数的参数(如果有结果)
        MethodHandle foldR1A1 = foldArguments(ret1, 0, arg1);
        // MethodHandlesTest.arg("test");
        // return MethodHandlesTest.ret("test");
        Assertions.assertEquals("ret1[test]", foldR1A1.invoke("test"));
        MethodHandle foldR2R1 = foldArguments(ret2, 0, ret1);
        // MethodHandlesTest.ret(MethodHandlesTest.ret("test"), "test")
        Assertions.assertEquals("ret2[ret1[test]/test]", foldR2R1.invoke("test"));
        MethodHandle foldR3R2 = foldArguments(ret3, 0, ret2);
        // MethodHandlesTest.ret(MethodHandlesTest.ret("test1", "test2"), "test1", "test2")
        Assertions.assertEquals("ret3[ret2[test1/test2]/test1/test2]", foldR3R2.invoke("test1", "test2"));
        MethodHandle foldR3R1 = foldArguments(ret3, 1, ret1);
        // MethodHandlesTest.ret("test1", MethodHandlesTest.ret("test2"), "test2")
        Assertions.assertEquals("ret3[test1/ret1[test2]/test2]", foldR3R1.invoke("test1", "test2"));
        MethodHandle foldR2R0 = foldArguments(ret2, 1, ret0);
        // MethodHandlesTest.ret("test1", MethodHandlesTest.ret())
        Assertions.assertEquals("ret2[test1/ret0]", foldR2R0.invoke("test1"));
        MethodHandle foldR1A11 = foldArguments(ret1, 1, arg0);
        // MethodHandlesTest.arg();
        // return MethodHandlesTest.ret("test1");
        Assertions.assertEquals("ret1[test1]", foldR1A11.invoke("test1"));

        // collectArguments: 对返回的MethodHandle收集参数，调用filter方法
        MethodHandle collectRet202 = collectArguments(ret2, 0, ret2);
        // MethodHandlesTest.ret(MethodHandlesTest.ret("test1", "test2"), "test3")
        Assertions.assertEquals("ret2[ret2[test1/test2]/test3]", collectRet202.invoke("test1", "test2", "test3"));
        MethodHandle collectRet212 = collectArguments(ret2, 1, ret2);
        // MethodHandlesTest.ret("test1", MethodHandlesTest.ret("test2", "test3"))
        Assertions.assertEquals("ret2[test1/ret2[test2/test3]]", collectRet212.invoke("test1", "test2", "test3"));
        MethodHandle collectRet210 = collectArguments(ret2, 1, ret0);
        // MethodHandlesTest.ret("test1", MethodHandlesTest.ret())
        Assertions.assertEquals("ret2[test1/ret0]", collectRet210.invoke("test1"));
        MethodHandle collectRet100 = collectArguments(ret1, 0, ret0);
        // MethodHandlesTest.ret("test1", MethodHandlesTest.ret())
        Assertions.assertEquals("ret1[ret0]", collectRet100.invoke());
    }

    public static void arg() {
        System.out.println("arg0");
    }

    public static void arg(String s) {
        System.out.println("arg1:" + s);
    }

    public static void arg(String s, String s1) {
        System.out.println("arg1:" + s + ", arg2: " + s1);
    }

    public static String ret() {
        return "ret0";
    }

    public static String ret(String s) {
        return "ret1[" + s + "]";
    }

    public static String ret(String s, String s1) {
        return "ret2[" + s + "/" + s1 + "]";
    }

    public static String ret(String s, String s1, String s2) {
        return "ret3[" + s + "/" + s1 + "/" + s2 + "]";
    }

}

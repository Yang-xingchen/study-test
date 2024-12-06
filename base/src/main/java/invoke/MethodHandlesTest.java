package invoke;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void ifElseTest() throws Throwable {
        MethodHandle oddLength = lookup().findStatic(MethodHandlesTest.class, "oddLength", MethodType.methodType(boolean.class, String.class));
        MethodHandle concat = lookup().findVirtual(String.class, "concat", MethodType.methodType(String.class, String.class));
        MethodHandle oddString = insertArguments(concat, 0, "odd:");
        MethodHandle evenString = insertArguments(concat, 0, "even:");

        // guardWithTest: 条件分支, 返回MethodHandle参数为if分支和else分支的参数, 前面部分为条件参数
        // 参数: arg
        // if (oddLength(arg)) {
        //     return oddString(arg);
        // } else {
        //     return evenString(arg);
        // }
        MethodHandle oddEvenString = guardWithTest(oddLength, oddString, evenString);
        Assertions.assertEquals("odd:a", (String) oddEvenString.invokeExact("a"));
        Assertions.assertEquals("even:ab", (String) oddEvenString.invokeExact("ab"));
    }

    public static boolean oddLength(String s) {
        return (s.length() & 1) == 1;
    }

    @Test
    public void switchTest() throws Throwable {
        MethodHandle def = dropArguments(MethodHandles.constant(String.class, "def"), 0, int.class);
        MethodHandle zero = dropArguments(MethodHandles.constant(String.class, "zero"), 0, int.class);
        MethodHandle one = dropArguments(MethodHandles.constant(String.class, "one"), 0, int.class);
        MethodHandle two = dropArguments(MethodHandles.constant(String.class, "two"), 0, int.class);
        // switch(arg) {
        //     case 0: return zero(arg);
        //     case 1: return one(arg);
        //     case 2: return two(arg);
        //     default: return def(arg);
        // }
        MethodHandle tableSwitch = tableSwitch(def, zero, one, two);
        Assertions.assertEquals("def", (String) tableSwitch.invokeExact(-1));
        Assertions.assertEquals("zero", (String) tableSwitch.invokeExact(0));
        Assertions.assertEquals("one", (String) tableSwitch.invokeExact(1));
        Assertions.assertEquals("two", (String) tableSwitch.invokeExact(2));
        Assertions.assertEquals("def", (String) tableSwitch.invokeExact(3));

        MethodHandle intStringConcat = lookup().findStatic(MethodHandlesTest.class, "intStringConcat", MethodType.methodType(String.class, int.class, String.class));
        MethodHandle intStringConcat2 = lookup().findStatic(MethodHandlesTest.class, "intStringConcat2", MethodType.methodType(String.class, int.class, String.class));
        // switch(arg[0]) {
        //     case 0: return zero(arg[0], arg[1]);
        //     case 1: return one(arg[0], arg[1]);
        //     case 2: return two(arg[0], arg[1]);
        //     default: return def(arg[0], arg[1]);
        // }
        MethodHandle tableSwitch2 = tableSwitch(intStringConcat2, intStringConcat, intStringConcat);
        Assertions.assertEquals("concat1: zero/0", (String) tableSwitch2.invokeExact(0, "zero"));
        Assertions.assertEquals("concat1: one/1", (String) tableSwitch2.invokeExact(1, "one"));
        Assertions.assertEquals("concat2: two/2", (String) tableSwitch2.invokeExact(2, "two"));
    }

    public static String intStringConcat(int i, String s) {
        return "concat1: " + s + "/" + i;
    }

    public static String intStringConcat2(int i, String s) {
        return "concat2: " + s + "/" + i;
    }

    @Test
    public void loop() throws Throwable {
        MethodHandle sum = publicLookup().findStatic(Integer.class, "sum", MethodType.methodType(int.class, int.class, int.class));
        MethodHandle less = publicLookup().findStatic(MethodHandlesTest.class, "less", MethodType.methodType(boolean.class, int.class, int.class));
        MethodHandle constant0 = MethodHandles.constant(int.class, 0);

        // iteratedLoop: foreach
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // list.iterator()
        MethodHandle iterator = insertArguments(publicLookup().findVirtual(List.class, "iterator", MethodType.methodType(Iterator.class)), 0, list);
        // int ret = constant(0);
        // for (int i : list) {
        //     ret = Integer.sum(ret, i);
        // }
        // return ret;
        MethodHandle iteratedLoop = iteratedLoop(iterator, constant0, sum);
        Assertions.assertEquals(45, (int) iteratedLoop.invokeExact());

        // countedLoop: fori
        // int ret = constant(0);
        // for (int i = 0; i < constant(10); i++) {
        //     ret = Integer.sum(ret, i);
        // }
        // return ret;
        MethodHandle countedLoop = countedLoop(MethodHandles.constant(int.class, 10), constant0, sum);
        Assertions.assertEquals(45, (int) countedLoop.invokeExact());

        // whileLoop: while
        // atomGet: AtomicInteger.get()
        MethodHandle atomGet = publicLookup().findVirtual(AtomicInteger.class, "get", MethodType.methodType(int.class));
        // atomGetAndIncrement: AtomicInteger.getAndIncrement()
        MethodHandle atomGetAndIncrement = publicLookup().findVirtual(AtomicInteger.class, "getAndIncrement", MethodType.methodType(int.class));
        // whileInit: (AtomicInteger a) -> 0
        MethodHandle whileInit = dropArguments(constant0, 0, AtomicInteger.class);
        // less10: (int i, AtomicInteger a) -> a.get() < 10
        MethodHandle less10 = dropArguments(insertArguments(filterArguments(less, 0, atomGet), 1, 10), 0, int.class);
        // whileBody: (int i, AtomicInteger a) -> sum(i, a.getAndIncrement())
        MethodHandle whileBody = filterArguments(sum, 1, atomGetAndIncrement);
        // int ret = whileInit(arg[0]);
        // while (less10(ret, arg[0])) {
        //     whileBody(ret, arg[0]);
        // }
        // return ret;
        MethodHandle whileLoop = whileLoop(whileInit, less10, whileBody);
        Assertions.assertEquals(45, (int) whileLoop.invokeExact(new AtomicInteger()));

        // doWhileLoop: do-while, 与whileLoop类似，省略相关测试

        // loop: 通用循环, 可在一条循环内执行多条语句
        // 每个参数由一个长度为4的数组组成, 分别表示: 初始化、循环体、结束条件、结果处理
        // getResult: (int i, AtomInteger a) -> i
        MethodHandle getResult = dropArguments(identity(int.class), 1, AtomicInteger.class);
        // int ret = whileInit(arg[0]);
        // while (less10(ret, arg[0])) {
        //     whileBody(ret, arg[0]);
        // }
        // return getResult(ret, arg[0]);
        MethodHandle loop1 = MethodHandles.loop(new MethodHandle[]{whileInit, whileBody, less10, getResult});
        Assertions.assertEquals(45, (int) loop1.invokeExact(new AtomicInteger()));
    }

    public static boolean less(int a, int b) {
        return a < b;
    }

    @Test
    public void exception() throws Throwable {
        MethodHandle println = publicLookup().findVirtual(System.out.getClass(), "println", MethodType.methodType(void.class, String.class));
        MethodHandle print = insertArguments(insertArguments(println, 1, "print"), 0, System.out);
        // throwException: 抛出异常, 类似throw
        // throwException: e -> {throw e;}
        MethodHandle throwException = throwException(void.class, RuntimeException.class);
        System.out.println("====================");
        try {
            // throw new RuntimeException();
            throwException.invoke(new RuntimeException());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        System.out.println("====================");
        try {
            throwException.invoke(new NullPointerException());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // catchException: try-catch
        System.out.println("====================");
        MethodHandle printStackTrace = publicLookup().findVirtual(RuntimeException.class, "printStackTrace", MethodType.methodType(void.class));
        MethodHandle catchException = catchException(throwException, RuntimeException.class, printStackTrace);
        // try {
        //     throwException(arg[0]);
        // } catch (RuntimeException e) {
        //     printStackTrace(e)
        // }
        catchException.invokeExact(new RuntimeException());

        // tryFinally: try-final
        System.out.println("====================");
        MethodHandle cleanup = dropArguments(print, 0, Throwable.class);
        MethodHandle tryFinally = tryFinally(throwException, cleanup);
        try {
            // Throwable throwable = null;
            // try {
            //     throwException(arg[0]);
            // } catch (Throwable e) {
            //     throwable = e;
            // } finally {
            //     cleanup(throwable);
            // }
            tryFinally.invokeExact(new RuntimeException());
        } catch (RuntimeException e) {
            System.out.println("====================");
            e.printStackTrace();
        }

        System.out.println("exit");
    }

}

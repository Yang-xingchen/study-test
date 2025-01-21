package other;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {

    private static final StringBuilder BUILDER = new StringBuilder("staticStringBuilder ");

    private static ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<String> initStringThreadLocal = ThreadLocal.withInitial(() -> "initString ");

    private static ThreadLocal<StringBuilder> initStringBuilderThreadLocal = ThreadLocal.withInitial(() -> new StringBuilder("initStringBuilder "));

    /**
     * 使用静态变量初始化
     * 非独享
     */
    private static ThreadLocal<StringBuilder> staticStringBuilder = ThreadLocal.withInitial(() -> BUILDER);

    /**
     * 不关闭
     * 在线程池中非独享
     */
    private static ThreadLocal<StringBuilder> nonShutdown = ThreadLocal.withInitial(() -> new StringBuilder("nonShutdown"));

    public static void main(String[] args){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 10000; i++) {
            threadPoolExecutor.execute(()->{
                stringThreadLocal.set("string ");
                stringBuilderThreadLocal.set(new StringBuilder("stringBuilder"));
                stringThreadLocal.set(stringThreadLocal.get()+"|");
                stringBuilderThreadLocal.get().append("|");
                initStringThreadLocal.set(stringThreadLocal.get()+"|");
                initStringBuilderThreadLocal.get().append("|");
                staticStringBuilder.get().append("|");
                nonShutdown.get().append("|");
                for (int j = 0; j < 3; j++) {
                    stringThreadLocal.set(stringThreadLocal.get()+j+" ");
                    stringBuilderThreadLocal.get().append(j).append(" ");
                    initStringThreadLocal.set(stringThreadLocal.get()+j+" ");
                    initStringBuilderThreadLocal.get().append(j).append(" ");
                    staticStringBuilder.get().append(j).append(" ");
                    nonShutdown.get().append(j).append(" ");
                }
                System.out.println(stringThreadLocal.get());
                System.out.println(stringBuilderThreadLocal.get());
                System.out.println(initStringThreadLocal.get());
                System.out.println(initStringBuilderThreadLocal.get());
                System.out.println(staticStringBuilder.get());
                System.out.println(nonShutdown.get());
                System.out.println();

                stringThreadLocal.remove();
                stringBuilderThreadLocal.remove();
                initStringThreadLocal.remove();
                initStringBuilderThreadLocal.remove();
                staticStringBuilder.remove();
            });
        }
        threadPoolExecutor.shutdown();
    }

}

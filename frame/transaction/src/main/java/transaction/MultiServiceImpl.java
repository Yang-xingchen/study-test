package transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@Slf4j
@Service
public class MultiServiceImpl implements MultiService {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 任务并发数量
     * 一次事务执行总任务数量需要小于该值
     */
    private static final int TASK_COUNT = 8;
    private Semaphore semaphore = new Semaphore(TASK_COUNT);
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(TASK_COUNT, TASK_COUNT, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
    private ThreadPoolExecutor executorMain = new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private void run(List<Runnable> task) {
        // 参数检查
        if (task.size() > executor.getCorePoolSize()) {
            throw new IllegalArgumentException();
        }
        try {
            AtomicBoolean fail = new AtomicBoolean(false);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(task.size());
            // 获取资源
            semaphore.acquire(task.size());
            try {
                List<? extends Future<?>> futures = task.stream().map(t -> executor.submit(() -> {
                    // 开启事务
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    TransactionStatus transaction = transactionManager.getTransaction(def);
                    try {
                        // 执行
                        t.run();
                    } catch (Exception e) {
                        fail.set(true);
                    } finally {
                        // 等待全部事务执行完毕
                        try {
                            cyclicBarrier.await();
                        } catch (Exception e) {
                            fail.set(true);
                        }
                    }
                    // 获取结果判断提交/回滚
                    if (fail.get()) {
                        transactionManager.rollback(transaction);
                    } else {
                        transactionManager.commit(transaction);
                    }
                })).toList();
                // 阻塞等待执行完毕
                for (Future<?> future : futures) {
                    future.get();
                }
            } finally {
                // 施放资源
                semaphore.release(task.size());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 正常情况
     */
    @Override
    public void normal() {
        String value = "A";
        run(List.of(
                () -> modelMapper.save(new Model(null, value)),
                () -> modelMapper.save(new Model(null, value)),
                () -> modelMapper.save(new Model(null, value))
        ));
        log.info(modelMapper.countByValue(value) == 3 ? "OK" : String.valueOf(modelMapper.countByValue(value)));
    }

    /**
     * 报错情况
     */
    @Override
    public void exception() {
        String value = "B";
        run(List.of(
                () -> modelMapper.save(new Model(null, value)),
                () -> modelMapper.save(new Model(null, value)),
                () -> {throw new RuntimeException();}
        ));
        log.info(modelMapper.countByValue(value) == 0 ? "OK" : String.valueOf(modelMapper.countByValue(value)));
    }

    @Override
    public void multiRun() {
        long base = System.currentTimeMillis();
        IntStream.range(0, 4).forEach(i -> {
            long submit = System.currentTimeMillis();
            executorMain.submit(() -> {
                String value = "C" + i;
                long exec = System.currentTimeMillis();
                run(List.of(
                        () -> {
                            log.info("{}: start: {}", value, System.currentTimeMillis() - base);
                            modelMapper.save(new Model(null, value));
                            try {
                                TimeUnit.MILLISECONDS.sleep(50);
                            } catch (Exception ignore) {}
                        },
                        () -> modelMapper.save(new Model(null, value)),
                        () -> modelMapper.save(new Model(null, value)),
                        () -> modelMapper.save(new Model(null, value)),
                        () -> modelMapper.save(new Model(null, value))
                ));
                log.info("{}: end: {}", value, System.currentTimeMillis() - base);
                log.info("{}: submit: {}, exec: {}", value, System.currentTimeMillis() - submit, System.currentTimeMillis() - exec);
                log.info(modelMapper.countByValue(value) == 5 ? "OK" : String.valueOf(modelMapper.countByValue(value)));
            });
        });
    }

}

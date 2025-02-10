package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.MapReduceProcessor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class MapReduceSchedule implements MapReduceProcessor {

    private static final Logger log = LoggerFactory.getLogger(BroadcastScheduler.class);

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        if (isRootTask()) {
            log.info("[root]scheduler: {}", context.getInstanceParams());
            List<SubTask> list = IntStream
                    .range(0, 5)
                    .boxed()
                    .map(SubTask::new)
                    .toList();
            map(list, "L1");
            return new ProcessResult(true);
        } else if ("L1".equals(context.getTaskName())) {
            SubTask subTask = (SubTask) context.getSubTask();
            log.info("[L1]scheduler: {}", subTask);
            List<SubTask> list = IntStream
                    .range(0, 3)
                    .map(i -> i + 10 * subTask.id)
                    .boxed()
                    .map(SubTask::new)
                    .toList();
            map(list, "L2");
            return new ProcessResult(true);
        }
        log.info("[L2]scheduler: {}", context.getSubTask());
        return new ProcessResult(true);
    }

    @Override
    public ProcessResult reduce(TaskContext context, List<TaskResult> taskResults) {
        log.info("reduce: {}, size: {}", context.getInstanceParams(), taskResults.size());
        return new ProcessResult(true);
    }

    public static class SubTask implements Serializable {

        public SubTask() {
        }

        public SubTask(Integer id) {
            this.id = id;
        }

        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "SubTask{" +
                    "id=" + id +
                    '}';
        }

    }

}

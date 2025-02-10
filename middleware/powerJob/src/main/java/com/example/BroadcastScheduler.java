package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.BroadcastProcessor;

import java.util.List;

@Component
public class BroadcastScheduler implements BroadcastProcessor {

    private static final Logger log = LoggerFactory.getLogger(BroadcastScheduler.class);

    @Override
    public ProcessResult preProcess(TaskContext context) throws Exception {
        log.info("pre: {}", context.getInstanceParams());
        return BroadcastProcessor.super.preProcess(context);
    }

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        log.info("scheduler: {}", context.getInstanceParams());
        return new ProcessResult(true);
    }

    @Override
    public ProcessResult postProcess(TaskContext context, List<TaskResult> taskResults) throws Exception {
        log.info("post: {}, size: {}", context.getInstanceParams(), taskResults.size());
        return BroadcastProcessor.super.postProcess(context, taskResults);
    }

}

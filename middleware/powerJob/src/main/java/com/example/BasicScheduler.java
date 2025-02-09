package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

@Component
public class BasicScheduler implements BasicProcessor {

    private static final Logger log = LoggerFactory.getLogger(BasicScheduler.class);

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        log.info("scheduler: {}", taskContext.getInstanceParams());
        return new ProcessResult(true);
    }

}

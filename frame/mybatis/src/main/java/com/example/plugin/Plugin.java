package com.example.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class Plugin implements Interceptor {


    private static final Logger log = LoggerFactory.getLogger(PluginMain.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("[invocation] method: {}, arg: {}, target: {}", invocation.getMethod(), Arrays.toString(invocation.getArgs()), invocation.getTarget());
        if (invocation.getArgs()[0] instanceof MappedStatement statement) {
            log.info("[invocation] sql: {}", statement.getBoundSql(null).getSql());
            log.info("[invocation] parameter: {}", statement.getBoundSql(null).getParameterMappings());
        }
        Object res = invocation.proceed();
        log.info("[invocation] res: {}", res);
        return res;
    }

}

package io.nio;

import java.nio.file.*;

public class WatchTest {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("/root");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        // 注册监听: 创建、删除、修改
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        // 获取监听事件
        while (true) {
            WatchKey key = watchService.take();
            key.pollEvents().forEach(watchEvent -> {
                System.out.printf("[%s]->%s\n", watchEvent.kind(), watchEvent.context());
                key.reset();
            });
        }
    }

}

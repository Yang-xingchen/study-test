package watcher;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final String IPS = "192.168.31.201:2181,192.168.31.202:2181,192.168.31.203:2181";

    public static void main(String[] args) throws Exception {
        try (ZooKeeper zooKeeper = new ZooKeeper(IPS, 2000, event -> {})) {
            getChildren(zooKeeper);
            TimeUnit.SECONDS.sleep(5);
        }
    }

    private static void getChildren(ZooKeeper zooKeeper) throws Exception {
        // 创建操作目录
        String rootPath = "/watcher";
        if (zooKeeper.exists(rootPath, false) == null) {
            zooKeeper.create(rootPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 监听
        List<String> children = zooKeeper.getChildren(rootPath, getWatcher(zooKeeper, rootPath));
        System.out.println("get: " + children);
        // 创建节点
        for (int i = 0; i < 5; i++) {
            zooKeeper.create(rootPath + "/test", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static Watcher getWatcher(ZooKeeper zooKeeper, String path) {
        return event -> {
            // 事件内容
            System.out.printf("thread: %s path: %s state: %s type: %s%n",
                    Thread.currentThread().getName(), event.getPath(), event.getState(), event.getType());
            try {
                // 获取数据及
                List<String> children = zooKeeper.getChildren(path, getWatcher(zooKeeper, path));
                System.out.println("watch: " + children);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}

package node;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static final String IPS = "192.168.31.201:2181,192.168.31.202:2181,192.168.31.203:2181";

    public static void main(String[] args) throws Exception {
        run(Main::getRootChildren);
//        run(zooKeeper -> getChildrenAll(zooKeeper, "/"));
    }

    private static void run(Consumer<ZooKeeper> consumer) throws Exception {
        try (ZooKeeper zooKeeper = new ZooKeeper(IPS, 2000, event -> {})) {
            consumer.accept(zooKeeper);
        }
    }

    /**
     * 持久节点
     */
    private static void persistent(ZooKeeper zooKeeper) {
        try {
            String p = "/persistent";
            Stat exists = zooKeeper.exists(p, false);
            System.out.println("exist: " + exists);
            if (exists != null) {
                zooKeeper.delete(p, -1);
            }
            String path = zooKeeper.create(p, "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("path: " + path);
            System.out.println("exist: " + zooKeeper.exists(p, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 临时节点
     */
    private static void ephemeral(ZooKeeper zooKeeper) {
        try {
            String p = "/ephemeral";
            Stat exists = zooKeeper.exists(p, false);
            // null
            System.out.println("exist: " + exists);
            if (exists != null) {
                zooKeeper.delete(p, -1);
            }
            String path = zooKeeper.create(p, "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("path: " + path);
            // non null
            System.out.println("exist: " + zooKeeper.exists(p, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 持久连续节点
     */
    private static void persistentSequential(ZooKeeper zooKeeper) {
        try {
            // null
            String p = "/persistentSequential";
            Stat exists = zooKeeper.exists(p, false);
            System.out.println("exist: " + exists);
            String path = zooKeeper.create(p, "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            System.out.println("path: " + path);
            // null
            System.out.println("exist: " + zooKeeper.exists(p, false));
            // non null
            System.out.println("exist: " + zooKeeper.exists(path, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取根目录下节点
     */
    private static void getRootChildren(ZooKeeper zooKeeper) {
        try {
            List<String> children = zooKeeper.getChildren("/", false);
            children.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归获取所有节点
     */
    private static void getChildrenAll(ZooKeeper zooKeeper, String path) {
        try {
            System.out.println(path);
            List<String> children = zooKeeper.getChildren(path, false);
            children.forEach(child -> getChildrenAll(zooKeeper, path + ("/".equals(path) ? "" : "/") + child));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

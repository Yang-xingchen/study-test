package mapreduce;

import hdfs.Main;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * wordCount数据生成
 * 格式: user good count
 */
public class DataGenerate {

    public static final int DATA_COUNT = 1000_0000;
    public static final int USER_COUNT = 100;
    public static final int GOOD_COUNT = 1000;
    public static final int COUNT_MAX = 10;

    public static void main(String[] args) throws Exception {
        Main.run(DataGenerate::generate);
    }

    private static void generate(FileSystem fileSystem) {
        try {
            fileSystem.mkdirs(new Path("/data"));
            Path dataPath = new Path("/data/wordCountData.txt");
            Path resPath = new Path("/data/wordCountResult.txt");
            if (fileSystem.exists(dataPath)) {
                fileSystem.delete(dataPath, false);
            }
            if (fileSystem.exists(resPath)) {
                fileSystem.delete(resPath, false);
            }

            try (FSDataOutputStream dataStream = fileSystem.create(dataPath);
                 FSDataOutputStream resStream = fileSystem.create(resPath)) {
                doGenerate(dataStream, resStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doGenerate(FSDataOutputStream dataStream, FSDataOutputStream resStream) throws IOException {
        Random random = new Random();
        int[] userCount = new int[USER_COUNT];
        int[] goodCount = new int[GOOD_COUNT];
        for (int i = 0; i < DATA_COUNT; i++) {
            int user = Math.abs(random.nextInt() % USER_COUNT);
            int good = Math.abs(random.nextInt() % GOOD_COUNT);
            int count = Math.abs(random.nextInt() % COUNT_MAX);
            userCount[user] += count;
            goodCount[good] += count;
            String line = "user" + format(user) + " good" + format(good) + " " + count + "\n";
            dataStream.write(line.getBytes(StandardCharsets.UTF_8));
        }
        for (int i = 0; i < userCount.length; i++) {
            resStream.write(("user" + format(i) + " " + userCount[i] + "\n").getBytes(StandardCharsets.UTF_8));
        }
        for (int i = 0; i < goodCount.length; i++) {
            resStream.write(("good" + format(i) + " " + goodCount[i] + "\n").getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String format(int i) {
        if (i < 10) {
            return "00" + i;
        }
        if (i < 100) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

}

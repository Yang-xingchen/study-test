package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Main {

	public static void main(String[] args) throws Exception {
		run(Main::writeAndRead);
	}

	public static void run(Consumer<FileSystem> consumer) throws Exception {
		Configuration conf = new Configuration();
		try (FileSystem fileSystem = FileSystem.get(URI.create("hdfs://192.168.31.201:8020"), conf, "root")) {
			consumer.accept(fileSystem);
		}
	}

	private static void writeAndRead(FileSystem fileSystem) {
		try {
			fileSystem.mkdirs(new Path("/test"));
			Path path = new Path("/test/a.txt");
			// 追加或创建文件
			FSDataOutputStream outputStream;
			if (fileSystem.exists(path)) {
				outputStream = fileSystem.append(path);
			} else {
				outputStream = fileSystem.create(path);
			}
			// 写
			try {
				outputStream.write("txt1\n".getBytes(StandardCharsets.UTF_8));
				outputStream.write("txt2\n".getBytes(StandardCharsets.UTF_8));
			} finally {
				outputStream.close();
			}
			// 读
			try (FSDataInputStream inputStream = fileSystem.open(path)) {
				byte[] buffer = new byte[512];
				int read = inputStream.read(buffer);
				byte[] bytes = new byte[read];
				System.arraycopy(buffer, 0, bytes, 0, read);
				System.out.println(new String(bytes));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

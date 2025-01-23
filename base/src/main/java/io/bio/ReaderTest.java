package io.bio;

import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReaderTest {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("nioTest.txt");
        // 写
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("TEST");
        }
        // 读
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Assertions.assertEquals("TEST", reader.readLine());
        }
        // mark/reset
        // mark标记点位，参数为标记后可读取最大字符数+1
        // reset回到上一个标记点位，之后从该点位继续读取
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.mark(5);
            Assertions.assertEquals("TEST", reader.readLine());
            reader.reset();
            Assertions.assertEquals("TEST", reader.readLine());
        }
    }

}

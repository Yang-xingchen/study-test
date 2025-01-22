package io.nio;

import org.junit.jupiter.api.Assertions;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ChannelTest {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("nioTest.txt");
        // 写
        try (SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.wrap("TEST ".getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < 3; i++) {
                channel.write(buffer);
                buffer.flip();
            }
        }
        // 读
        try (SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            StringBuilder sb = new StringBuilder();
            int size;
            while ((size = channel.read(buffer)) != -1) {
                buffer.flip();
                byte[] bytes = new byte[size];
                buffer.get(bytes);
                sb.append(new String(bytes));
                buffer.clear();
            }
            Assertions.assertEquals("TEST TEST TEST ", sb.toString());
        }
    }

}

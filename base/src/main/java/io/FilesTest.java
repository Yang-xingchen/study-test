package io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilesTest {

    @Test
    void writeAndRead() throws IOException{
        Path path = Paths.get("nioTest.txt");
        Files.write(path, "test".getBytes());
        assertEquals("test",Files.readString(path));
    }

}

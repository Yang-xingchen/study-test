package io.nio;

import org.junit.jupiter.api.Assertions;

import java.nio.file.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ZipTest {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("zipTest.zip");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        write(path);
        read(path);
        unzip(path);
    }

    private static void write(Path path) throws Exception {
        try (FileSystem fileSystem = FileSystems.newFileSystem(path, Map.of("create", true))) {
            Files.createDirectory(fileSystem.getPath("1"));
            Files.createFile(fileSystem.getPath("1", "1-1.txt"));
            Files.writeString(fileSystem.getPath("1", "1-2.txt"), "TEST", StandardOpenOption.CREATE);
            Files.createDirectory(fileSystem.getPath("2"));
            Files.createFile(fileSystem.getPath("3.txt"));
        }
    }

    private static void read(Path path) throws Exception {
        try (FileSystem fileSystem = FileSystems.newFileSystem(path)) {
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(fileSystem.getPath(""))) {
                Set<String> root = StreamSupport.stream(paths.spliterator(), false).map(Path::toString).collect(Collectors.toSet());
                Assertions.assertEquals(Set.of("1", "2", "3.txt"), root);
            }

            Assertions.assertTrue(Files.isDirectory(fileSystem.getPath("1")));
            Assertions.assertFalse(Files.isDirectory(fileSystem.getPath("1", "1-1.txt")));
            Assertions.assertFalse(Files.isDirectory(fileSystem.getPath("1", "1-2.txt")));
            Assertions.assertTrue(Files.isDirectory(fileSystem.getPath("2")));
            Assertions.assertFalse(Files.isDirectory(fileSystem.getPath("3.txt")));

            Assertions.assertEquals("TEST", Files.readString(fileSystem.getPath("1", "1-2.txt")));
        }
    }

    private static void unzip(Path path) throws Exception {
        try (FileSystem fileSystem = FileSystems.newFileSystem(path)) {
            Path target = Paths.get("1-2.txt");
            Files.copy(fileSystem.getPath("1", "1-2.txt"), target);
            Assertions.assertEquals("TEST", Files.readString(target));
        }
    }

}

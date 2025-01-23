package io.bio;

import org.junit.jupiter.api.Assertions;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTest {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("zipTest.zip");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        write(path);
        readByInputStream(path);
        readByFile(path);
        unzip(path);
    }

    /**
     * NOTE: 目录名称需使用'/'结尾
     */
    private static void write(Path path) throws Exception {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(path))) {
            zipOutputStream.putNextEntry(new ZipEntry("1/"));
            zipOutputStream.closeEntry();
            zipOutputStream.putNextEntry(new ZipEntry("1/1-1.txt"));
            zipOutputStream.closeEntry();
            zipOutputStream.putNextEntry(new ZipEntry("1/1-2.txt"));
            zipOutputStream.write("TEST".getBytes());
            zipOutputStream.closeEntry();
            zipOutputStream.putNextEntry(new ZipEntry("2/"));
            zipOutputStream.closeEntry();
            zipOutputStream.putNextEntry(new ZipEntry("3.txt"));
            zipOutputStream.closeEntry();
        }
    }

    /**
     * NOTE: 名称以'/'结尾时为目录
     */
    private static void readByInputStream(Path path) throws Exception {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path))) {
            Assertions.assertEquals("1/", zipInputStream.getNextEntry().getName());
            Assertions.assertEquals("1/1-1.txt", zipInputStream.getNextEntry().getName());
            Assertions.assertEquals("1/1-2.txt", zipInputStream.getNextEntry().getName());
            byte[] bytes12 = zipInputStream.readAllBytes();
            Assertions.assertEquals("2/", zipInputStream.getNextEntry().getName());
            Assertions.assertEquals("3.txt", zipInputStream.getNextEntry().getName());
            Assertions.assertNull(zipInputStream.getNextEntry());

            Assertions.assertEquals("TEST", new String(bytes12));
        }
    }

    private static void readByFile(Path path) throws Exception {
        try (ZipFile zipFile = new ZipFile(path.toString())) {
            // 可获取全部文件
            Set<String> root = zipFile.stream().map(ZipEntry::getName).collect(Collectors.toSet());
            Assertions.assertEquals(Set.of("1/", "1/1-2.txt", "1/1-1.txt", "2/", "3.txt"), root);

            Assertions.assertTrue(zipFile.getEntry("1/").isDirectory());
            Assertions.assertFalse(zipFile.getEntry("1/1-1.txt").isDirectory());
            Assertions.assertFalse(zipFile.getEntry("1/1-2.txt").isDirectory());
            Assertions.assertTrue(zipFile.getEntry("2/").isDirectory());
            Assertions.assertFalse(zipFile.getEntry("3.txt").isDirectory());

            byte[] bytes12 = zipFile.getInputStream(zipFile.getEntry("1/1-2.txt")).readAllBytes();
            Assertions.assertEquals("TEST", new String(bytes12));
        }
    }

    private static void unzip(Path path) throws Exception {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path))) {
            Path target = Paths.get("1-2.txt");
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
                if (!"1/1-2.txt".equals(nextEntry.getName())) {
                    continue;
                }
                try (OutputStream out = Files.newOutputStream(target)) {
                    zipInputStream.transferTo(out);
                }
                zipInputStream.closeEntry();
            }
            Assertions.assertEquals("TEST", Files.readString(target));
        }
    }

}

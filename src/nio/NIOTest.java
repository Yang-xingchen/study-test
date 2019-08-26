package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NIOTest {

    @Test
    void writeAndRead() throws IOException{
        Path path = Paths.get("nioTest.txt");
        Files.write(path, "test".getBytes());
        assertEquals("test",Files.readString(path));
    }

    @Test
    void buffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        assertEquals(1024, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());

        byteBuffer.put("Test".getBytes());
        assertEquals(1024, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());

        byteBuffer.flip();
        assertEquals(4, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());

        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        assertEquals("Test", new String(data));
        assertEquals(4, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());

        byteBuffer.clear();
        assertEquals(1024, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());

        byteBuffer.flip();
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
    }

    public static void main(String[] args) throws Exception{
        Path path = Paths.get("/root");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        while (true){
            WatchKey key = watchService.take();
            key.pollEvents().forEach(watchEvent -> {
                System.out.printf("[%s]->%s\n",watchEvent.kind(), watchEvent.context());
                key.reset();
            });
        }

    }

}

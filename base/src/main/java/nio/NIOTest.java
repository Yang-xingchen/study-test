package nio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
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
        assertEquals(1024, byteBuffer.remaining());
        //  T e s t
        //         p
        //          ...l
        byteBuffer.put("Test".getBytes());
        assertEquals(1024, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(1020, byteBuffer.remaining());
        //  T e s t T e s t
        //                 p
        //                  ...l
        byteBuffer.put("Test".getBytes());
        assertEquals(1024, byteBuffer.limit());
        assertEquals(8, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(1016, byteBuffer.remaining());
        //  T e s t T e s t
        // p
        //                 l
        byteBuffer.flip();
        assertEquals(8, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(8, byteBuffer.remaining());
        //  T E S T T e s t
        //         p
        //                 l
        byteBuffer.put("TEST".getBytes());
        assertEquals(8, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(4, byteBuffer.remaining());
        //  T E S T T e s t
        // p
        //         l
        byteBuffer.flip();
        assertEquals(4, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(4, byteBuffer.remaining());
        //  T E S T T e s t
        //         p
        //         l
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        assertEquals("TEST", new String(data));
        assertEquals(4, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(0, byteBuffer.remaining());

        //  T E S T T e s t
        //         p
        //         l
        Assertions.assertThrows(BufferOverflowException.class, () -> byteBuffer.put("test".getBytes()));
        Assertions.assertThrows(BufferUnderflowException.class, byteBuffer::get);

        //  T E S T T e s t
        // p
        //                  ...l
        byteBuffer.clear();
        assertEquals(1024, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(1024, byteBuffer.remaining());
        //  t e s t T E S T
        //                 p
        //                  ...l
        byteBuffer.put("testTEST".getBytes());
        assertEquals(1024, byteBuffer.limit());
        assertEquals(8, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(1016, byteBuffer.remaining());
        //  t e s t T E S T
        // p
        //                 l
        byteBuffer.flip();
        assertEquals(8, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(8, byteBuffer.remaining());
        //  t e s t T E S T
        //         p
        //                 l
        data = new byte[4];
        byteBuffer.get(data);
        assertEquals("test", new String(data));
        assertEquals(8, byteBuffer.limit());
        assertEquals(4, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(4, byteBuffer.remaining());
        //  t e s t t e s t
        //                 p
        //                 l
        byteBuffer.put("test".getBytes());
        assertEquals(8, byteBuffer.limit());
        assertEquals(8, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(0, byteBuffer.remaining());
        //  t e s t t e s t
        // p
        //                 l
        byteBuffer.flip();
        assertEquals(8, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(8, byteBuffer.remaining());
        //  t e s t t e s t
        //                 p
        //                 l
        data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        assertEquals("testtest", new String(data));
        assertEquals(8, byteBuffer.limit());
        assertEquals(8, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(0, byteBuffer.remaining());
        //  t e s t t e s t
        // p
        //                  ...l
        byteBuffer.clear();
        assertEquals(1024, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(1024, byteBuffer.remaining());
        //  t e s t t e s t
        // p
        // l
        byteBuffer.flip();
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
        assertEquals(1024, byteBuffer.capacity());
        assertEquals(0, byteBuffer.remaining());
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

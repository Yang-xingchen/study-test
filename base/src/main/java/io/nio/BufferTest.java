package io.nio;

import org.junit.jupiter.api.Assertions;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferTest {

    /**
     * <pre>
     *     {@link ByteBuffer#allocate(int)}分配容量
     *
     *     {@link ByteBuffer#capacity()}获取容量，分配后不会改变
     *     {@link ByteBuffer#position()}buffer当前处理位置
     *     {@link ByteBuffer#limit()}buffer可处理上限
     *     {@link ByteBuffer#remaining()}剩余空间: {@link ByteBuffer#limit()} - {@link ByteBuffer#position()}
     *
     *     {@link ByteBuffer#put}写入内容到buffer, 并增加{@link ByteBuffer#position()}
     *     {@link ByteBuffer#get}从buffer读取内容, 并增加{@link ByteBuffer#position()}
     *     {@link ByteBuffer#flip()}将{@link ByteBuffer#limit()}设置为{@link ByteBuffer#position()}，并将{@link ByteBuffer#position()}归0
     *     {@link ByteBuffer#clear()}重置为刚分配状态
     * </pre>
     */
    public static void main(String[] args) {
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

}

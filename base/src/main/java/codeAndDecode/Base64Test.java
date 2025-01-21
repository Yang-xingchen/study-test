package codeAndDecode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

public class Base64Test {

    private final String SRC = "Test";

    @Test
    void base64() {
        // 编码
        byte[] encode = Base64.getEncoder().encode(SRC.getBytes());
        System.out.println("encode=" + new String(encode) + ", length=" + encode.length);
        // 解码
        byte[] decode = Base64.getDecoder().decode(encode);
        Assertions.assertArrayEquals(SRC.getBytes(), decode);
    }

}

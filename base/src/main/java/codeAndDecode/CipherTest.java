package codeAndDecode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@SuppressWarnings({"SameParameterValue", "FieldCanBeLocal"})
public class CipherTest {

    private final String SRC = "Test";
    private final String KEY = "key";

    @Test
    void aes() throws Exception {
        test("AES", 128);
    }

    @Test
    void aes2() throws Exception {
        test("AES", 256);
    }

    @Test
    void des() throws Exception {
        test("DES", 56);
    }

    @Test
    void desEde() throws Exception {
        test("DESede", 168);
    }

    private void test(String algorithm, int keySize) throws Exception {
        byte[] encode = encode(SRC, KEY, algorithm, keySize);
        System.out.printf("code=%s, length=%d\n", new String(encode), encode.length);

        byte[] decode = decode(encode, KEY, algorithm, keySize);
        Assertions.assertArrayEquals(SRC.getBytes(), decode, "not equals");

        Assertions.assertThrows(BadPaddingException.class, () -> decode(encode, "KEY", algorithm, keySize), "false key");
    }

    /**
     * 加密
     */
    byte[] encode(String context, String key, String algorithm, int keySize) throws Exception {
        SecretKeySpec keySpec = getKey(keySize, algorithm, key.getBytes());
        System.out.println("encode = " + keySpec.hashCode());

        // 加密
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        System.out.println("block size=" + cipher.getBlockSize());
        return cipher.doFinal(context.getBytes());
    }

    /**
     * 解密
     */
    byte[] decode(byte[] context, String key, String algorithm, int keySize) throws Exception {
        SecretKeySpec keySpec = getKey(keySize, algorithm, key.getBytes());
        System.out.println("decode = " + keySpec.hashCode());

        // 解密
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        System.out.println("block size=" + cipher.getBlockSize());
        return cipher.doFinal(context);
    }

    @Test
    void keyTest() throws Exception {
        Assertions.assertArrayEquals(
                getKey(128, "AES", "AES".getBytes()).getEncoded(),
                getKey(128, "AES", "AES".getBytes()).getEncoded());
    }

    /**
     * 生成密钥
     * 采用传入密钥为随机算法种子生成实际密钥，可规避不同加密算法要求密钥长度不同
     */
    private SecretKeySpec getKey(int keySize, String algorithm, byte[] key) throws Exception {
        // 密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        // 随机数算法
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        // 种子
        secureRandom.setSeed(key);
        // 生成
        keyGenerator.init(keySize, secureRandom);
        return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), algorithm);
    }

//    @Test
//    void nio() throws Exception {
//        // fail
//        ByteBuffer src = ByteBuffer.allocate(64);
//        src.put("Test1".getBytes());
//        src.flip();
//        System.out.println("src=" + src);
//
//        SecretKeySpec encodeKeySpec = getKey(256, "AES", KEY.getBytes());
//        Cipher encodeCipher = Cipher.getInstance("AES");
//        encodeCipher.init(Cipher.ENCRYPT_MODE, encodeKeySpec);
//
//        ByteBuffer encode = ByteBuffer.allocate(128);
//        System.out.println("encode=" + encodeCipher.update(src, encode));
//        System.out.println("encode result=" + encode);
//        encodeCipher.doFinal();
//        System.out.println("encode final=" + encode);
//        encode.flip();
//
//        SecretKeySpec decodeKeySpec = getKey(256, "AES", KEY.getBytes());
//        Cipher decodeCipher = Cipher.getInstance("AES");
//        decodeCipher.init(Cipher.DECRYPT_MODE, decodeKeySpec);
//
//        ByteBuffer decode = ByteBuffer.allocate(64);
//        System.out.println("decode=" + decodeCipher.update(encode, decode));
//        System.out.println("getOutputSize=" + decodeCipher.getOutputSize(64));
//        decodeCipher.doFinal();
//
//        System.out.println(decode);
//        decode.flip();
//        byte[] data = new byte[decode.limit()];
//        decode.get(data);
//        System.out.println("decode result=" + new String(data));
//    }

}

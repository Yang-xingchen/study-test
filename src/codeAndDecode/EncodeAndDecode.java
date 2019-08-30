package codeAndDecode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@SuppressWarnings({"SameParameterValue", "FieldCanBeLocal"})
public class EncodeAndDecode {

    private final String SRC = "Test";
    private final String KEY = "key";

    private static final Logger LOGGER = LogManager.getLogger(EncodeAndDecode.class);

    @Test
    void base64(){
        byte[] encode = Base64.getEncoder().encode(SRC.getBytes());
        LOGGER.info("encode={}, length={}", new String(encode), encode.length);
        byte[] decode = Base64.getDecoder().decode(encode);
        Assertions.assertArrayEquals(SRC.getBytes(), decode);
    }

    @Test
    void aes() throws Exception{
        test("AES", 128);
    }

    @Test
    void aes2() throws Exception{
        test("AES", 256);
    }
    @Test
    void des() throws Exception{
        test("DES", 56);
    }

    @Test
    void desEde() throws Exception{
        test("DESede", 168);
    }

    @Test
    void keyTest() throws Exception{
        Assertions.assertArrayEquals(
                getKey(128, "AES", "AES".getBytes()).getEncoded(),
                getKey(128, "AES", "AES".getBytes()).getEncoded());
    }

    private void test(String algorithm, int keySize) throws Exception {
        byte[] encode = encode(SRC, KEY, algorithm, keySize);
        LOGGER.info("code={}, length={}", new String(encode), encode.length);

        byte[] decode = decode(encode, KEY, algorithm, keySize);
        Assertions.assertArrayEquals(SRC.getBytes(), decode, "true key");

        Assertions.assertThrows(BadPaddingException.class, ()->decode(encode, "KEY", algorithm, keySize), "false key");
    }

    private SecretKeySpec getKey(int keySize, String algorithm, byte[] key) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key);
        keyGenerator.init(keySize, secureRandom);
        return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), algorithm);
    }

    byte[] encode(String context, String key, String algorithm, int keySize) throws Exception{
        SecretKeySpec keySpec = getKey(keySize, algorithm, key.getBytes());
        LOGGER.info("encode = {}", keySpec.hashCode());

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        LOGGER.info("block size={}", cipher.getBlockSize());
        return cipher.doFinal(context.getBytes());
    }

    byte[] decode(byte[] context, String key, String algorithm, int keySize) throws Exception{
        SecretKeySpec keySpec = getKey(keySize, algorithm, key.getBytes());
        LOGGER.info("decode = {}", keySpec.hashCode());

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        LOGGER.info("block size={}", cipher.getBlockSize());
        return cipher.doFinal(context);
    }

    @Test
    void nio() throws Exception{
        // fail
        ByteBuffer src = ByteBuffer.allocate(64);
        src.put("Test1".getBytes());
        src.flip();
        LOGGER.info("src={}", src);

        SecretKeySpec encodeKeySpec = getKey(256, "AES", KEY.getBytes());
        Cipher encodeCipher = Cipher.getInstance("AES");
        encodeCipher.init(Cipher.ENCRYPT_MODE, encodeKeySpec);

        ByteBuffer encode = ByteBuffer.allocate(128);
        LOGGER.info("encode={}",encodeCipher.update(src, encode));
        LOGGER.info("encode result={}", encode);
        encodeCipher.doFinal();
        LOGGER.info("encode final={}", encode);
        encode.flip();

        SecretKeySpec decodeKeySpec = getKey(256, "AES", KEY.getBytes());
        Cipher decodeCipher = Cipher.getInstance("AES");
        decodeCipher.init(Cipher.DECRYPT_MODE, decodeKeySpec);

        ByteBuffer decode = ByteBuffer.allocate(64);
        LOGGER.info("decode={}", decodeCipher.update(encode, decode));
        LOGGER.info("getOutputSize={}", decodeCipher.getOutputSize(64));
        decodeCipher.doFinal();

        LOGGER.info(decode);
        decode.flip();
        byte[] data = new byte[decode.limit()];
        decode.get(data);
        LOGGER.info("decode result={}", new String(data));
    }

}

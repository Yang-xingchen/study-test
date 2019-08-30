package codeAndDecode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@SuppressWarnings({"SameParameterValue", "FieldCanBeLocal"})
public class EncodeAndDecode {

    private final String SRC = "Test";
    private final String KEY = "key";

    private static final Logger LOGGER = LogManager.getLogger(EncodeAndDecode.class);

    @Test
    void base64(){
        String encode = Base64.getEncoder().encodeToString(SRC.getBytes());
        LOGGER.info(encode);
        byte[] decode = Base64.getDecoder().decode(encode.getBytes());
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

}

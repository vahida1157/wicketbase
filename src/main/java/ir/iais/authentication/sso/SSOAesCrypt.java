/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public class SSOAesCrypt {

    /**
     * Encrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key - key to use should be 16 bytes long (128 bits)
     * @param iv - initialization vector
     * @param data - data to encrypt
     * @return encryptedData data in base64 encoding with iv attached at end
     * after a :
     */
    private static String key = "";

    public static String getKey() {
        if (key.equals("")) {
            try {
                Properties keyFile = new Properties();
                keyFile.load(SSOAesCrypt.class.getResourceAsStream("/ssoconfig.properties"));
                key = keyFile.getProperty("AESKEY");
            } catch (IOException ex) {
                org.apache.log4j.Logger.getLogger(SSOAesCrypt.class).error("can not load SSOConfig properties");
            }
        }
        return key;
    }

    /**
     * Encrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key - key to use should be 16 bytes long (128 bits)
     * @param data - data to encrypt
     * @return encryptedData data in base64 encoding with iv attached at end
     * after a :
     */
    public static String encrypt(String key, String data) {
        try {
            byte[] bytes = data.getBytes();

            SecureRandom random = new SecureRandom();
            byte[] ivBytes = new byte[16];
            random.nextBytes(ivBytes);

            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, newKey, random);
            byte[] destination = new byte[ivBytes.length + bytes.length];
            System.arraycopy(ivBytes, 0, destination, 0, ivBytes.length);
            System.arraycopy(bytes, 0, destination, ivBytes.length, bytes.length);
            byte[] encryptedData = cipher.doFinal(destination);

            String encryptedDataInBase64 = Base64.getEncoder().encodeToString(encryptedData);
            encryptedDataInBase64 = encryptedDataInBase64.replace('/', '_');
            encryptedDataInBase64 = encryptedDataInBase64.replace('+', '-');

            return encryptedDataInBase64;

        } catch (Throwable e) {
            //        logger.error("",e);
            throw new RuntimeException("", e);
        }

    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key - key to use should be 16 bytes long (128 bits)
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */
    public static String decrypt(String key, String data) {

        data = data.replace('_', '/');
        data = data.replace('-', '+');
        byte[] bytes = Base64.getDecoder().decode(data);
        if (bytes.length < 17) {
            return "";
        }

        byte[] ivBytes = Arrays.copyOfRange(bytes, 0, 16);
        byte[] contentBytes = Arrays.copyOfRange(bytes, 16, bytes.length);

        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes, 0, ciper.getBlockSize());

            ciper.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(ciper.doFinal(contentBytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ignored) {
            return "";
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.util.Base64;
import android.util.Log;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密工具包
 *
 * @author MaYichao
 */
public class DESUtils {

    private static final String ALGORITHM = "DES";

    /**
     * <p> 生成密钥 </p>
     *
     * @param seed 密钥种子
     * @return
     * @throws Exception
     */
    public static String getSecretKey(String seed){
        try {
            SecureRandom secureRandom;
            if (seed != null && !"".equals(seed)) {
                secureRandom = new SecureRandom(seed.getBytes());
            } else {
                secureRandom = new SecureRandom();
            }
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
    //        return Base64Utils.encode(secretKey.getEncoded());
            return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException ex) {
            Log.i("EFC","密钥生成失败",ex);
            return seed;
        }
    }

    /**
     * <p> 加密 </p>
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) {
        try {
            return Base64.encodeToString(encrypt(data.getBytes(), key), Base64.DEFAULT);
        } catch (Exception ex) {
            throw new RuntimeException("加密失败", ex);
        }
    }
    /**
     * <p> 加密 </p>
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(Base64.decode(key, Base64.DEFAULT));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * <p> 转换密钥 </p>
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }
}

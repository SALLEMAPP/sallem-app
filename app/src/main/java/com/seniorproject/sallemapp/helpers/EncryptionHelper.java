package com.seniorproject.sallemapp.helpers;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by abdul on 22-Apr-2017.
 */

public class EncryptionHelper {

    private static final String SALLEM_KEY = "Sallem2017Sallem";
    public static String Encrypt(String password) throws Exception{

        Key aesKey = new SecretKeySpec(SALLEM_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] ecrypted = cipher.doFinal(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b: ecrypted){
            sb.append((char)b);
        }
        return sb.toString();
    }
    public static String decrypt(String encryptedPassword) throws  Exception{
        byte[] byteArray = new byte[encryptedPassword.length()];
        for (int i= 0; i <encryptedPassword.length(); i++){
            byteArray[i] = (byte) encryptedPassword.charAt(i);
        }
        Key aesKey = new SecretKeySpec(SALLEM_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(byteArray));
        return decrypted;

    }
}

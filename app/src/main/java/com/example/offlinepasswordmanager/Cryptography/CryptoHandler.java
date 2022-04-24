package com.example.offlinepasswordmanager.Cryptography;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;

import com.example.offlinepasswordmanager.Storage.StorageController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {
    private static final String TAG = "CRYPTOHANDLER";
    private static CryptoHandler instance;
    private static final String keyAlias = "AESPasswordsEncryptionKey";

    private Cipher cipher;
    private SecretKey key;

    private KeyStore keyStore;
    private char[] passwordC;

    private CryptoHandler(Context context) {
        String passwordS = "_somepassword_";
        passwordC = new char[passwordS.length()];
        passwordS.getChars(0, passwordS.length(), passwordC, 0);

        try { //try to read the existing key
            //TODO: replace algorithm with AES/
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            File keyStorageFile = StorageController.getInstance().findFile(context.getFilesDir(), "keystorage");
            if (keyStorageFile == null)
                throw new FileNotFoundException("keystorage file was not found");
            FileInputStream fileInputStream = new FileInputStream(keyStorageFile);
            keyStore.load(fileInputStream, passwordC);

            KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(passwordC);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, protectionParameter);
            key = secretKeyEntry.getSecretKey();
        } catch (FileNotFoundException e) { //key doesn't exist, generate a new key and store it
            Log.d(TAG, e.getMessage() + "; Resolving by generating a new key...", e);
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);
                key = keyGenerator.generateKey();

                KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
                KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(passwordC);
                keyStore.load(null, passwordC);

                keyStore.setEntry(keyAlias, secretKeyEntry, protectionParameter);
                FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "keystorage"));
                keyStore.store(fos, passwordC);

                //cipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (Exception exception) {
                Log.d(TAG, exception.getMessage(), exception);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public static CryptoHandler getInstance(Context context) {
        return instance == null ? instance = new CryptoHandler(context) : instance;
    }

    public String dostuff(String toEncrypt) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] toReturn = cipher.doFinal(toEncrypt.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, key);
            toReturn = cipher.doFinal(toReturn);
            toEncrypt = new String(toReturn, "UTF-8");

            return null;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }

    public byte[] encrypt(String toEncrypt) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(toEncrypt.getBytes("UTF-8"));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String decrypt(byte[] toDecrypt) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(toDecrypt), "UTF-8");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }
}

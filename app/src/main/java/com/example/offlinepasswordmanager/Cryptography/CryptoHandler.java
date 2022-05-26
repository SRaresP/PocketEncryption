package com.example.offlinepasswordmanager.Cryptography;

import android.content.Context;
import android.util.Log;

import com.example.offlinepasswordmanager.Storage.StorageController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {
    private static final String TAG = "CRYPTOHANDLER";
    private static final String keyAlias = "AESStringsEncryptionKey";
    private static final String ivAlias = "AESStringsEncryptionIv";
    private static final String keyStoreFileName = "keystorage";
    //TODO: decide whether "paddingCharacter" needs to be kept
    private static final char paddingCharacter = 0;
    private static CryptoHandler instance;

    private Cipher cipher;
    private SecretKey key;
    private IvParameterSpec iv;
    private final int CurrentBlockSize;

    private KeyStore keyStore;
    private final char[] passwordC;

    private StorageController storageController;

    protected void generate(final boolean generateKey, final boolean generateIv, final Context context)
            throws CertificateException,
            IOException,
            NoSuchAlgorithmException,
            KeyStoreException {
        if (!(generateKey || generateIv)) {
            Log.i(TAG, "Didn't generate key or iv, this is a problem if there is another file not" +
                    " found exception above that mentions these secrets, otherwise this is intended behaviour");
            return;
        }

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(passwordC);
        keyStore.load(null, passwordC);
        FileOutputStream fos;

        if (generateKey) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            key = keyGenerator.generateKey();
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
            keyStore.setEntry(keyAlias, secretKeyEntry, protectionParameter);
            fos = new FileOutputStream(new File(context.getFilesDir(), keyStoreFileName));
            keyStore.store(fos, passwordC);
        }

        if (generateIv) {
            byte[] ivBytes = new byte[16];
            new SecureRandom().nextBytes(ivBytes);
            iv = new IvParameterSpec(ivBytes);
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(ivBytes, "AES"));
            keyStore.setEntry(ivAlias, secretKeyEntry, protectionParameter);
            fos = new FileOutputStream(new File(context.getFilesDir(), keyStoreFileName));
            keyStore.store(fos, passwordC);
        }

    }

    protected CryptoHandler(final Context context) {
        //TODO: modify this password before finalising app
        String passwordS = "_somepassword_";
        passwordC = new char[passwordS.length()];
        passwordS.getChars(0, passwordS.length(), passwordC, 0);
        storageController = new StorageController();

        boolean generateKey = false;
        boolean generateIv = false;

        try { //try to read the existing key
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            File keyStorageFile = storageController.findFile(context.getFilesDir(), keyStoreFileName, false);
            if (keyStorageFile == null) {
                generateIv = generateKey = true;
                throw new FileNotFoundException(keyStoreFileName + " file was not found");
            }
            FileInputStream fileInputStream = new FileInputStream(keyStorageFile);
            keyStore.load(fileInputStream, passwordC);

            KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(passwordC);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, protectionParameter);
            if (!(generateKey = (secretKeyEntry == null))) key = secretKeyEntry.getSecretKey();

            KeyStore.SecretKeyEntry secretIvEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(ivAlias, protectionParameter);
            if (!(generateIv = (secretIvEntry == null)))
                iv = new IvParameterSpec(secretIvEntry.getSecretKey().getEncoded());
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage() + "; Will attempt to resolve by generating a new file containing a key and iv...", e);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        try {
            generate(generateKey, generateIv, context);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            try {
                generate(generateKey, generateIv, context);
            } catch (Exception anotherE) {
                Log.e(TAG, e.getMessage() + "; Generation of secrets did not work properly twice in a row", e);
            }
        }

        CurrentBlockSize = cipher.getBlockSize();
        int i = 0;
    }

    public static CryptoHandler getInstance(final Context context) {
        return instance == null ? instance = new CryptoHandler(context) : instance;
    }

    public byte[] toMultipleOfSixteen(final String parameter) {
        byte[] input = parameter.getBytes(StandardCharsets.UTF_8);
        int difference = 16 - (input.length % 16);
        if (difference == 16) return input;
        byte[] result = new byte[input.length + difference];
        System.arraycopy(input, 0, result, 0, input.length);
        return result;
    }

    public byte[] fromMultipleOfSixteen(final byte[] parameter) {
        int count = 0;
        for (int i = parameter.length - 1; i > 0; --i) {
            if (parameter[i] == 0) {
                ++count;
            } else break;
        }
        byte[] result = new byte[parameter.length - count];
        System.arraycopy(parameter, 0, result, 0, result.length);
        return result;
    }

    public byte[] encrypt(final String toEncrypt) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(toMultipleOfSixteen(toEncrypt));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String decrypt(final byte[] toDecrypt) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] resultC = cipher.doFinal(toDecrypt);
            resultC = fromMultipleOfSixteen(resultC);
            String resultS = new String(resultC, StandardCharsets.UTF_8);
            return resultS;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public int getCurrentBlockSize() {
        return CurrentBlockSize;
    }
}

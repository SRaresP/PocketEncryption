package com.example.offlinepasswordmanager.Storage;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.NotDirectoryException;
import java.security.InvalidParameterException;

import javax.crypto.Cipher;

//A password file on the filesystem is actually a folder within the app UI,
//while a password within the UI is an entry within a file

//TODO: Implement encryption
//TODO: Implement duplicate checks
public class PwdStorageController extends StorageController {
    private static final String TAG = "PwdStorageController";

    private static final String NAME_HASH_SEPARATOR = " NAME_HASH_SEPARATOR ";
    private static final String PWD_SEPARATOR = " PWD_SEPARATOR\n";

    private static PwdStorageController instance;

    private File passwordDir;
    private File defaultFile;

    protected PwdStorageController() {
        super();
    }

    public static PwdStorageController getInstance() {
        if (instance == null) {
            return instance = new PwdStorageController();
        } else {
            return instance;
        }
    }

    public void addPassword(String pwdName, String pwdHash, String fileToAddTo) {
        if (pwdName.contains(NAME_HASH_SEPARATOR) ||
                pwdName.contains(PWD_SEPARATOR)) {
            throw new InvalidParameterException("Password name cannot contain \"" + NAME_HASH_SEPARATOR + "\" or \"" + PWD_SEPARATOR + '\"');
        } else if (pwdHash.contains(NAME_HASH_SEPARATOR)) {
            throw new InvalidParameterException("Password hash cannot contain \"" + NAME_HASH_SEPARATOR + '\"');
        } else if (pwdHash.contains(PWD_SEPARATOR)) {
            throw new InvalidParameterException("Password hash cannot contain \"" + PWD_SEPARATOR + '\"');
        }
        try {
            File file = findOrCreateFile(passwordDir, fileToAddTo);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(pwdName + NAME_HASH_SEPARATOR + pwdHash + PWD_SEPARATOR);
            fileWriter.flush();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public void addPassword(String pwdName, String pwdHash) {
        addPassword(pwdName, pwdHash, defaultFile.getName());
    }

    public String getPassword(String pwdName, String fileToGetFrom) throws NullPointerException, FileNotFoundException {
        File targetFile = findFile(passwordDir, fileToGetFrom);

        if (targetFile == null) {
            throw new FileNotFoundException("Password file was not found.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int buffer;
        try {
            FileReader fileReader = new FileReader(targetFile);
            while ((buffer = fileReader.read()) != -1) {
                stringBuilder.append((char)buffer);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return stringBuilder.toString();
    }

    public String getPassword(String pwdName) throws NullPointerException, FileNotFoundException {
        return getPassword(pwdName, defaultFile.getName());
        /*
        File[] files = pwdDir.listFiles();
        boolean found = false;
        if (files == null) {
            throw new NullPointerException("Passwords directory was empty.");
        }
        File targetFile = files[0];

        for (File f : files) {
            if (f.equals(defaultFile)) {
                targetFile = f;
                found = true;
                break;
            }
        }

        if (!found) {
            throw new FileNotFoundException("Password file was not found.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        byte buffer;
        try {
            FileInputStream fileInputStream = new FileInputStream(targetFile);
            while ((buffer = (byte) fileInputStream.read()) != -1) {
                stringBuilder.append((char) buffer);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return stringBuilder.toString();
        */
    }

    public void setup(File pwdDir) throws NotDirectoryException {
        if (!pwdDir.isDirectory()) {
            throw new NotDirectoryException(pwdDir.getAbsolutePath());
        }
        this.passwordDir = pwdDir;

        defaultFile = findOrCreateFile(passwordDir, "defaultPasswordsFile");
    }

    public void clearPasswords() {
        File[] files = passwordDir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else if (!f.delete()) {
                Log.i(TAG, "Couldn't delete file " + f.getAbsolutePath());
            }
        }
    }
}

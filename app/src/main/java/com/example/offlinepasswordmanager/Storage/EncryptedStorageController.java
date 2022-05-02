package com.example.offlinepasswordmanager.Storage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.security.InvalidParameterException;

//TODO: implement encryption
public class EncryptedStorageController extends StorageController {
    private static final String TAG = "EncryptedStorageController";
    private static final String EncryptedStorageFolderPath = "/EncryptedStorage";

    private static EncryptedStorageController instance;

    private final File encryptedStorageRoot;

    private EncryptedStorageController(final Context context) {
        super();
        encryptedStorageRoot = new File(context.getFilesDir().getAbsolutePath() + EncryptedStorageFolderPath);
        if (!encryptedStorageRoot.mkdirs())
            Log.i(TAG, "Failed to make EncryptedStorage folder when initialising, this may be because it already exists");
    }

    public static EncryptedStorageController getInstance(final Context context) {
        if (instance == null) {
            return instance = new EncryptedStorageController(context);
        } else {
            return instance;
        }
    }

    //the file name should be the name of the text within the app
    //eg if we store an account, we would have "Steam account" as the name

    public void add(final String fileName, final String fileContents, final String internalAppFolder)
            throws  FileAlreadyExistsException, IOException {
        File folder = new File(encryptedStorageRoot.getAbsolutePath() + internalAppFolder);
        folder.mkdirs();
        File file = findFile(folder, fileName, false);
        if (file != null) {
            throw new FileAlreadyExistsException("Duplicate file name provided");
        }
        file = new File(folder, fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContents);
        fileWriter.flush();
    }

    public void add(final String fileName, final String fileContents) throws IOException {
        add(fileName, fileContents, encryptedStorageRoot.getAbsolutePath());
    }

    public String get(final String fileName, final String folderToGetFrom) throws NullPointerException, FileNotFoundException, IOException {
        File folder = new File(encryptedStorageRoot.getAbsolutePath() + folderToGetFrom);
        folder.mkdirs();
        File targetFile = findFile(folder, fileName, false);
        if (targetFile == null) {
            throw new FileNotFoundException("Encrypted file was not found.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int buffer;
        FileReader fileReader = new FileReader(targetFile);
        while ((buffer = fileReader.read()) != -1) {
            stringBuilder.append((char) buffer);
        }

        return stringBuilder.toString();
    }

    public String get(final String fileName) throws NullPointerException, FileNotFoundException, IOException {
        return get(fileName, encryptedStorageRoot.getAbsolutePath());
    }

    public void wipeEncryptedData() {
        deleteDirContents(encryptedStorageRoot);
    }
}

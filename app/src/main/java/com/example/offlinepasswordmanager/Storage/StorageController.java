package com.example.offlinepasswordmanager.Storage;

import android.util.Log;

import java.io.File;

public class StorageController {
    private static final String TAG = "StorageController";

    private static StorageController instance;

    protected StorageController() {}

    public static StorageController getInstance() {
        if (instance == null) {
            return instance = new StorageController();
        } else {
            return instance;
        }
    }

    public File findOrCreateFile(File directory, String fileName) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new File(directory, fileName);
        }
        for (File f : files) {
            if (f.getName().equals(fileName)) {
                return f;
            }
        }
        return new File(directory, fileName);
    }

    public File findFile(File directory, String fileName, boolean checkSubdirectories) {
        if (!directory.isDirectory()) {
            return null;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        for (File f : files) {
            if (f.getName().equals(fileName)) {
                return f;
            }
            else if (checkSubdirectories && f.isDirectory()) {
                return findFile(f, fileName, true);
            }
        }
        return null;
    }

    public void deleteDirContents(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else if (!f.delete()) {
                Log.i(TAG, "Couldn't delete file.");
            }
        }
    }

    public void deleteDir(File directory) {
        deleteDirContents(directory);
        if (!directory.delete()) {
            Log.i(TAG, "Couldn't delete file.");
        }
    }
}

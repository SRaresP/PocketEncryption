package com.example.offlinepasswordmanager.Storage;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.offlinepasswordmanager.Cryptography.CryptoHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptedStorageController extends StorageController {
	private static final String TAG = "EncryptedStorageController";
	private static final String EncryptedStorageFolderPath = "/EncryptedStorage";

	private static EncryptedStorageController instance;

	private final File encryptedStorageRoot;
	private File masterPasswordFile;
	private final CryptoHandler cryptoHandler;
	private final MessageDigest messageDigest;

	private EncryptedStorageController(final Context context) throws NoSuchAlgorithmException {
		super();
		encryptedStorageRoot = new File(context.getFilesDir().getAbsolutePath() + EncryptedStorageFolderPath);
		if (!encryptedStorageRoot.mkdirs())
			Log.i(TAG, "Failed to make EncryptedStorage folder when initialising, this may be because it already exists");
		cryptoHandler = CryptoHandler.getInstance(context);

		messageDigest = MessageDigest.getInstance("SHA-256");

		masterPasswordFile = findFile(context.getFilesDir(), "mPass", false);
	}

	public static EncryptedStorageController getInstance(final Context context) {
		if (instance == null) {
			try {
				return instance = new EncryptedStorageController(context);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	//the file name should be the name of the text within the app
	//eg if we store an account, we would have "Steam account" as the name

	public void setMasterPassword(Context context, String masterPassword) throws IOException {
		masterPasswordFile = new File(context.getFilesDir().getAbsolutePath(), "mPass");

		messageDigest.update(masterPassword.getBytes(StandardCharsets.UTF_8));
		byte[] hashedPassword = messageDigest.digest();

		FileOutputStream fileOutputStream = new FileOutputStream(masterPasswordFile);
		fileOutputStream.write(hashedPassword);
		fileOutputStream.flush();
		fileOutputStream.close();
		//TODO: Remove this debug log
		Log.i(TAG, "Wrote to hash file: " + new String(hashedPassword, StandardCharsets.UTF_8));
	}

	public boolean checkMasterPassword(String masterPassword) throws IOException {
		if (masterPasswordFile == null) {
			throw new FileNotFoundException("Hash file was not found.");
		}

		FileInputStream fileInputStream = new FileInputStream(masterPasswordFile);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] input = new byte[32];
		while (fileInputStream.read(input) != -1) {
			byteArrayOutputStream.write(input);
		}
		byte[] hashedFileContents = byteArrayOutputStream.toByteArray();
		//TODO: Remove this debug log
		Log.i(TAG, "Read from hash file: " + new String(hashedFileContents, StandardCharsets.UTF_8));

		messageDigest.update(masterPassword.getBytes(StandardCharsets.UTF_8));
		byte[] hashedPassword = messageDigest.digest();

		fileInputStream.close();
		byteArrayOutputStream.close();

		return Arrays.equals(hashedFileContents, hashedPassword);
	}

	public void wipeMasterPassword() {
		if(!masterPasswordFile.delete()) {
			Log.i(TAG, "Failed to delete master password");
		}
	}

	public boolean masterPasswordFileExists() {
		return masterPasswordFile != null;
	}

	public void add(final String fileName, final String fileContents, final String internalAppFolder)
			throws FileAlreadyExistsException, IOException {
		File folder = new File(encryptedStorageRoot.getAbsolutePath() + internalAppFolder);
		folder.mkdirs();
		File file = findFile(folder, fileName, false);
		if (file != null) {
			throw new FileAlreadyExistsException("Duplicate file name provided");
		}
		file = new File(folder, fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		byte[] encFileContents = cryptoHandler.encrypt(fileContents);

		fileOutputStream.write(encFileContents);
		fileOutputStream.flush();
		fileOutputStream.close();
		//TODO: Remove this debug log
		Log.i(TAG, "Wrote to encrypted file: " + new String(encFileContents, StandardCharsets.UTF_8));
	}

	public void add(final String fileName, final String fileContents) throws IOException {
		add(fileName, fileContents, "");
	}

	public File createDirectory(final String fileName, final String internalAppFolder) throws IOException {
		File folder = new File(encryptedStorageRoot.getAbsolutePath() + internalAppFolder, fileName);
		if (!folder.mkdirs()) {
			throw new IOException("Failed to create directory " + fileName + " inside " + internalAppFolder);
		}
		return folder;
	}

	public String get(final String fileName, final String folderToGetFrom) throws NullPointerException, FileNotFoundException, IOException {
		File folder = new File(encryptedStorageRoot.getAbsolutePath() + folderToGetFrom);
		folder.mkdirs();
		File targetFile = findFile(folder, fileName, false);
		if (targetFile == null) {
			throw new FileNotFoundException("Encrypted file was not found.");
		}

		FileInputStream fileInputStream = new FileInputStream(targetFile);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		//WE HAVE TO READ THE ENCRYPTED DATA IN CHUNKS OF THE EXACT BLOCK SIZE IT WAS WRITTEN IN
		//well, I think
		byte[] input = new byte[cryptoHandler.getCurrentBlockSize()];
		while (fileInputStream.read(input) != -1) {
			byteArrayOutputStream.write(input);
		}
		byte[] fileContents = byteArrayOutputStream.toByteArray();

		fileInputStream.close();
		byteArrayOutputStream.close();
		//TODO: Remove this debug log
		Log.i(TAG, "Read from encrypted file: " + new String(fileContents));
		return cryptoHandler.decrypt(fileContents);
	}

	public String get(final String fileName) throws NullPointerException, FileNotFoundException, IOException {
		return get(fileName, "");
	}

	public void wipeEncryptedData() {
		deleteDirContents(encryptedStorageRoot);
	}

	public List<String> getFilenamesFrom(String internalDirPath) {
		File directory = new File(encryptedStorageRoot.getAbsolutePath() + internalDirPath);
		if (!directory.mkdirs()) {
			Log.e(TAG, "File passed to getFilenamesFrom() was not a directory");
		}
		File[] files = directory.listFiles();
		if (files == null) {
			return null;
		}
		ArrayList<String> filenames = new ArrayList<>(files.length);
		for (File file : files) {
			filenames.add(file.getName());
		}
		return filenames;
	}

	public ArrayList<String> getFilePathsFrom(String internalDirPath) {
		File directory = new File(encryptedStorageRoot.getAbsolutePath() + internalDirPath);
		File[] files = directory.listFiles();
		if (files == null) {
			return null;
		}
		ArrayList<String> filePaths = new ArrayList<>(files.length);
		for (File file : files) {
			filePaths.add(file.getAbsolutePath());
		}
		return filePaths;
	}

	public String getEncryptedStorageRootPath() {
		return encryptedStorageRoot.getAbsolutePath();
	}
}

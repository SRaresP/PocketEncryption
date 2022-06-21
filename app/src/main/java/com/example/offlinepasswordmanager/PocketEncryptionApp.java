package com.example.offlinepasswordmanager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PocketEncryptionApp extends Application {
	private static PocketEncryptionApp pocketEncryptionApp;

	private final ExecutorService executorService = Executors.newFixedThreadPool(4);
	private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

	@Override
	public void onCreate() {
		super.onCreate();
		pocketEncryptionApp = this;
	}

	public static PocketEncryptionApp getInstance() {
		return PocketEncryptionApp.pocketEncryptionApp;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public Handler getMainThreadHandler() {
		return mainThreadHandler;
	}
}

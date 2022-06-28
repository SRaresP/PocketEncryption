package com.example.offlinepasswordmanager.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.ui.custom.LoadingView;

public class AuthActivity extends AppCompatActivity {
	private static final String TAG = "AuthActivity";

	private void finishSetup(EncryptedStorageController encryptedStorageController, LoadingView loadingView) {
		Fragment fragment;
		if (encryptedStorageController.masterPasswordFileExists()) {
			fragment = new LoginFragment();
		} else {
			fragment = new WelcomeFragment();
		}

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.authLayout, fragment);
		fragmentTransaction.commit();

		loadingView.terminate();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		PocketEncryptionApp pocketEncryptionApp = PocketEncryptionApp.getInstance();

		LoadingView loadingView = new LoadingView(findViewById(R.id.authLayout), this, getString(R.string.initialising_encryption_handler), null, true).show();

		pocketEncryptionApp.getExecutorService().execute(() -> {
			EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);
			pocketEncryptionApp.getMainThreadHandler().post(() -> {
				finishSetup(encryptedStorageController, loadingView);
			});
		});
	}
}
package com.example.offlinepasswordmanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.ui.custom.LoadingView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AuthActivity extends AppCompatActivity {
	private static final String TAG = "AuthActivity";

	private ProgressBar loadingPB;
	private TextView loadingTV;
	private LinearLayoutCompat authLayout;

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

		LoadingView loadingView = new LoadingView(findViewById(R.id.authLayout), this, getString(R.string.initialising_encryption_handler));

		pocketEncryptionApp.getExecutorService().execute(() -> {
			EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);
			pocketEncryptionApp.getMainThreadHandler().post(() -> {
				finishSetup(encryptedStorageController, loadingView);
			});
		});
	}
}
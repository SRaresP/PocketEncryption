package com.example.offlinepasswordmanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;

public class AuthActivity extends AppCompatActivity {

	private ProgressBar loadingPB;
	private TextView loadingTV;
	private LinearLayoutCompat authLayout;

	private void finishSetup(EncryptedStorageController encryptedStorageController) {
		Fragment fragment;
		if (encryptedStorageController.masterPasswordFileExists()) {
			fragment = new LoginFragment();
		} else {
			fragment = new WelcomeFragment();
		}

		authLayout.removeView(loadingPB);
		authLayout.removeView(loadingTV);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.authLayout, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		PocketEncryptionApp pocketEncryptionApp = PocketEncryptionApp.getInstance();

		//TODO: tidy this up nicely
		authLayout = findViewById(R.id.authLayout);
		loadingPB = new ProgressBar(this);
		loadingPB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadingTV = new TextView(this);
		loadingTV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadingTV.setText(R.string.initialising_encryption_handler);
		loadingTV.setTextSize(20);
		authLayout.addView(loadingPB);
		authLayout.addView(loadingTV);

		pocketEncryptionApp.getExecutorService().execute(() -> {
			EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(AuthActivity.this);
			pocketEncryptionApp.getMainThreadHandler().post(() -> {
				finishSetup(encryptedStorageController);
			});
		});
	}
}
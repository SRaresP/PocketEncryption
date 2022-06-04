package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

public class AuthActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		Fragment fragment;

		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);

		if (encryptedStorageController.masterPasswordFileExists()) {
			fragment = new LoginFragment();
		}
		else {
			fragment = new WelcomeFragment();
		}

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.authLayout, fragment);
		fragmentTransaction.commit();
	}
}
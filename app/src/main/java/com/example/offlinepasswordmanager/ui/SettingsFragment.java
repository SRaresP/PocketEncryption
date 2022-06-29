package com.example.offlinepasswordmanager.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;

import java.io.IOException;

public class SettingsFragment extends Fragment {
	private static final String TAG = "SettingsFragment";

	public SettingsFragment() {
		// Required empty public constructor
	}

	public static SettingsFragment newInstance() {
		return new SettingsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FragmentActivity fragmentActivity = requireActivity();

		AppCompatButton updatePwdB = fragmentActivity.findViewById(R.id.setUpdatePwdB);
		AppCompatEditText currentPwdET = fragmentActivity.findViewById(R.id.setCurrentPwdET);
		AppCompatEditText newPwdET = fragmentActivity.findViewById(R.id.setNewPwdET);

		PocketEncryptionApp pocketEncryptionApp = PocketEncryptionApp.getInstance();
		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(fragmentActivity);

		updatePwdB.setOnClickListener(myView -> {
			String currentPwd = currentPwdET.getText().toString();
			String newPwd = newPwdET.getText().toString();
			pocketEncryptionApp.getExecutorService().execute(() -> {
				try {
					if (!encryptedStorageController.checkMasterPassword(currentPwd)) {
						pocketEncryptionApp.getMainThreadHandler().post(() -> {
							Toast.makeText(fragmentActivity, R.string.password_rejected, Toast.LENGTH_SHORT).show();
						});
						return;
					}
					encryptedStorageController.setMasterPassword(fragmentActivity, newPwd);
					pocketEncryptionApp.getMainThreadHandler().post(() -> {
						currentPwdET.setText("");
						newPwdET.setText("");
						Toast.makeText(fragmentActivity, R.string.password_set, Toast.LENGTH_SHORT).show();
					});
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
					pocketEncryptionApp.getMainThreadHandler().post(() -> {
						Toast.makeText(fragmentActivity, R.string.could_not_modify_password, Toast.LENGTH_SHORT).show();
					});
				}
			});
		});
	}
}
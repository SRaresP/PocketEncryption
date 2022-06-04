package com.example.offlinepasswordmanager.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;

import java.io.IOException;

public class WelcomeFragment extends Fragment {

	private static final String TAG = "WelcomeFragment";

	public WelcomeFragment() {
		// Required empty public constructor
	}

	public static WelcomeFragment newInstance() {
		return new WelcomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_welcome, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FragmentActivity activity = requireActivity();
		AppCompatButton setPwdB = activity.findViewById(R.id.welSetPwdB);
		AppCompatEditText editText = activity.findViewById(R.id.welPwdET);

		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(activity);

		setPwdB.setOnClickListener(myView -> {
			String inputPass = editText.getText().toString();
			try {
				encryptedStorageController.setMasterPassword(activity, inputPass);
				Toast.makeText(activity, "Password set", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(activity, PrimaryActivity.class);
				startActivity(intent);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				Toast.makeText(activity, "Could not write your password to internal app storage", Toast.LENGTH_LONG).show();
			}
		});
	}
}
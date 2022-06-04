package com.example.offlinepasswordmanager.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.io.IOException;

public class LoginFragment extends Fragment {

	private static final String TAG = "LoginFragment";

	public LoginFragment() {
		// Required empty public constructor
	}

	public static LoginFragment newInstance(String param1, String param2) {
		LoginFragment fragment = new LoginFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FragmentActivity activity = requireActivity();
		AppCompatButton enterB = activity.findViewById(R.id.logEnterB);
		AppCompatEditText editText = activity.findViewById(R.id.logPwdET);
		AppCompatTextView warningTV = activity.findViewById(R.id.logWarningTV);

		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(activity);

		enterB.setOnClickListener(myView -> {
			String inputPass = editText.getText().toString();
			try {
				if (encryptedStorageController.checkMasterPassword(inputPass)) {
					Toast.makeText(activity, "Password accepted", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(activity, PrimaryActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(activity, "Password rejected", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				Toast.makeText(activity, "Could not find the file containing your set password", Toast.LENGTH_LONG).show();
			}
		});

		//TODO: DELETE THIS
		warningTV.setOnClickListener(myView -> encryptedStorageController.wipeMasterPassword());
	}
}
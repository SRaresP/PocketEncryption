package com.example.offlinepasswordmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CreationActivity extends AppCompatActivity {
	private static final String TAG = "CREATIONACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation);

		Intent intent = getIntent();
		String internalPath = intent.getStringExtra("InternalPath");

		ArrayList<Callable<EncryptedStorageController>> tasks = new ArrayList<>(1);
		tasks.add(() -> {
			return EncryptedStorageController.getInstance(this);
		});
		Future<EncryptedStorageController> future = PocketEncryptionApp.getInstance().getExecutorService().submit(tasks.get(0));

		AppCompatButton cancelButton = findViewById(R.id.crExitB);
		AppCompatButton saveButton = findViewById(R.id.crCreateB);
		AppCompatEditText fileNameET = findViewById(R.id.crEntryNameET);
		AppCompatEditText contentET = findViewById(R.id.crEntryContentET);
		RadioGroup fileTypeRD = findViewById(R.id.crFileTypeRD);
		RadioButton textFileRB = findViewById(R.id.crTextFileRB);
		RadioButton folderRB = findViewById(R.id.crFolderRB);

		textFileRB.setOnClickListener(view -> {
			contentET.setEnabled(true);
			saveButton.setEnabled(true);
		});
		folderRB.setOnClickListener(view -> {
			contentET.setEnabled(false);
			saveButton.setEnabled(true);
		});

		cancelButton.setOnClickListener(view -> finish());

		try {
			EncryptedStorageController encryptedStorageController = future.get();
			saveButton.setOnClickListener(view -> {
				if (fileNameET.getText().toString().equals("")) {
					Toast.makeText(this, "Give the entry a name, please", Toast.LENGTH_LONG).show();
					return;
				}
				int checkedId = fileTypeRD.getCheckedRadioButtonId();
				if (checkedId == textFileRB.getId()) {
					try {
						encryptedStorageController.add(fileNameET.getText().toString(), contentET.getText().toString(), internalPath);
						Toast.makeText(this, "Created your file!", Toast.LENGTH_LONG).show();
						fileNameET.setText("");
						contentET.setText("");
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
						Toast.makeText(this, "Failed to create your file, it may already exist", Toast.LENGTH_LONG).show();
					}
				} else if (checkedId == folderRB.getId()) {
					try {
						encryptedStorageController.createDirectory(fileNameET.getText().toString(), internalPath);
						Toast.makeText(this, "Created your folder!", Toast.LENGTH_LONG).show();
						fileNameET.setText("");
						contentET.setText("");
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
						Toast.makeText(this, "Failed to create your directory, it may already exist", Toast.LENGTH_LONG).show();
					}
				} else {
					Log.i(TAG, "No radio button selected on creation button click");
					Toast.makeText(this, "You need to select an entry type above", Toast.LENGTH_LONG).show();
				}
			});
		} catch (ExecutionException | InterruptedException e) {
			Log.e(TAG, e.getMessage(), e);
			Toast.makeText(this, "A thread was interrupted, exiting activity, please retry", Toast.LENGTH_LONG).show();
			finish();
		}
	}
}
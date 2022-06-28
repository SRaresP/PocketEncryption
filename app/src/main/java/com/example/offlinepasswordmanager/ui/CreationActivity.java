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
	private static final String TAG = "CreationActivity";

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
					Toast.makeText(this, R.string.give_the_entry_a_name, Toast.LENGTH_LONG).show();
					return;
				}
				int checkedId = fileTypeRD.getCheckedRadioButtonId();
				if (checkedId == textFileRB.getId()) {
					try {
						encryptedStorageController.add(fileNameET.getText().toString(), contentET.getText().toString(), internalPath);
						Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
						fileNameET.setText("");
						contentET.setText("");
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
						Toast.makeText(this, R.string.could_not_save_your_file, Toast.LENGTH_LONG).show();
					}
				} else if (checkedId == folderRB.getId()) {
					try {
						encryptedStorageController.createDirectory(fileNameET.getText().toString(), internalPath);
						Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
						fileNameET.setText("");
						contentET.setText("");
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
						Toast.makeText(this, R.string.failed_to_create_your_directory, Toast.LENGTH_LONG).show();
					}
				} else {
					Log.i(TAG, "No radio button selected on creation button click");
					Toast.makeText(this, R.string.you_need_to_select_an_entry_type_above, Toast.LENGTH_LONG).show();
				}
			});
		} catch (ExecutionException | InterruptedException e) {
			Log.e(TAG, e.getMessage(), e);
			Toast.makeText(this, R.string.a_thread_was_interrupted, Toast.LENGTH_LONG).show();
			finish();
		}
	}
}
package com.example.offlinepasswordmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.ui.custom.LoadingView;

import java.io.File;
import java.io.IOException;

public class TextEditActivity extends AppCompatActivity {
	private static final String TAG = "TextEditActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_edit);

		Intent intent = getIntent();
		String currentPath = intent.getStringExtra("CurrentPath");

		File file = new File(intent.getStringExtra("FilePath"));

		EditText contentET = findViewById(R.id.editContentET);
		EditText fileNameET = findViewById(R.id.editFileNameET);
		Button saveB = findViewById(R.id.editSaveB);
		Button exitB = findViewById(R.id.editExitButton);
		LinearLayoutCompat buttonL = findViewById(R.id.editButtonLayout);

		PocketEncryptionApp pocketEncryptionApp = PocketEncryptionApp.getInstance();
		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);

		pocketEncryptionApp.getExecutorService().execute(() -> {
			try {
				String toSet = encryptedStorageController.get(file.getName(), currentPath);
				pocketEncryptionApp.getMainThreadHandler().post(() -> {
					contentET.setText(toSet);
				});
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				pocketEncryptionApp.getMainThreadHandler().post(() -> {
					Toast.makeText(this, R.string.failed_to_open_file_to_read_from, Toast.LENGTH_LONG).show();
					finish();
				});
			}
		});

		fileNameET.setText(file.getName());

		saveB.setOnClickListener(view -> {
			LoadingView loadingView = new LoadingView(buttonL, TextEditActivity.this, getString(R.string.creating), saveB, false).show();

			String fileName = fileNameET.getText().toString();
			String fileContent = contentET.getText().toString();

			pocketEncryptionApp.getExecutorService().execute(() -> {
				if (!file.delete()) {
					Log.i(TAG, "Failed to delete old file after editing.");
					pocketEncryptionApp.getMainThreadHandler().post(() -> {
						Toast.makeText(this, getString(R.string.failed_to_delete_the_old_file), Toast.LENGTH_SHORT).show();
					});
				}

				try {
					encryptedStorageController.add(fileName, fileContent, currentPath);
					pocketEncryptionApp.getMainThreadHandler().post(() -> {
						Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
					});
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
					pocketEncryptionApp.getMainThreadHandler().post(() -> {
						Toast.makeText(this, R.string.could_not_save_your_file, Toast.LENGTH_LONG).show();
					});
				} finally {
					pocketEncryptionApp.getMainThreadHandler().post(loadingView::terminate);
				}
			});
		});

		exitB.setOnClickListener(view -> finish());

	}
}
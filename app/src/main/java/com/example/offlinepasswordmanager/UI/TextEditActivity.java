package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

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

		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);

		try {
			contentET.setText(encryptedStorageController.get(file.getName(), currentPath));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			Toast.makeText(this, "Failed to open file to read from, going back to previous activity", Toast.LENGTH_LONG).show();
			finish();
		}

		fileNameET.setText(file.getName());

		saveB.setOnClickListener(view -> {
			file.delete();

			try {
				encryptedStorageController.add(fileNameET.getText().toString(), contentET.getText().toString(), currentPath);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				Toast.makeText(this, "Something went wrong while saving your file. Contact the app developer.", Toast.LENGTH_LONG).show();
			}
		});

		exitB.setOnClickListener(view -> {
			finish();
			//TODO: refresh DataFragment after going back to it to replace the old file with the new one, maybe in the save method?
		});

	}
}
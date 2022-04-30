package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.Storage.StorageController;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.security.InvalidParameterException;

//TODO: Implement UI
//TODO: Warn user whenever something goes wrong
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);

        AppCompatButton readButton = findViewById(R.id.readButton);
        AppCompatButton readFromFolderButton = findViewById(R.id.readFromFolderButton);
        AppCompatButton writeButton = findViewById(R.id.writeButton);
        AppCompatButton writeToFolderButton = findViewById(R.id.writeToFolderButton);
        AppCompatButton clearButton = findViewById(R.id.clearButton);

        EditText etFileName = findViewById(R.id.etName);
        EditText etText = findViewById(R.id.etText);
        EditText etFolder = findViewById(R.id.etFolder);

        TextView tvOutput = findViewById(R.id.tvOutput);

        writeButton.setOnClickListener(view -> {
            try {
                encryptedStorageController.add(etFileName.getText().toString(), etText.getText().toString());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
            Toast.makeText(MainActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        writeToFolderButton.setOnClickListener(view -> {
            try {
                encryptedStorageController.add(etFileName.getText().toString(), etText.getText().toString(), etFolder.getText().toString());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
            Toast.makeText(MainActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        readButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(encryptedStorageController.get(etFileName.getText().toString()));
                Toast.makeText(MainActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "Folder to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        readFromFolderButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(encryptedStorageController.get(etFileName.getText().toString(), etFolder.getText().toString()));
                Toast.makeText(MainActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "Folder to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        clearButton.setOnClickListener(view ->
        {
            StorageController.getInstance().deleteDirContents(getDataDir());
            Toast.makeText(MainActivity.this, "Poof", Toast.LENGTH_SHORT).show();
        });
    }
}
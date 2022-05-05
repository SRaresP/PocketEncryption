package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlinepasswordmanager.Cryptography.CryptoHandler;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.Storage.StorageController;

//TODO: Implement UI
//TODO: Warn user whenever something goes wrong
public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "DebugActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(this);

        Toast.makeText(DebugActivity.this, "Done initialising", Toast.LENGTH_SHORT).show();

        AppCompatButton readButton = findViewById(R.id.debugReadButton);
        AppCompatButton readFromFolderButton = findViewById(R.id.debugReadFromFolderButton);
        AppCompatButton writeButton = findViewById(R.id.debugWriteButton);
        AppCompatButton writeToFolderButton = findViewById(R.id.debugWriteToFolderButton);
        AppCompatButton clearButton = findViewById(R.id.debugClearButton);

        EditText etFileName = findViewById(R.id.debugEtName);
        EditText etText = findViewById(R.id.debugEtText);
        EditText etFolder = findViewById(R.id.debugEtFolder);

        TextView tvOutput = findViewById(R.id.debugTvOutput);

        writeButton.setOnClickListener(view -> {
            try {
                encryptedStorageController.add(etFileName.getText().toString(), etText.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Toast.makeText(DebugActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        writeToFolderButton.setOnClickListener(view -> {
            try {
                encryptedStorageController.add(etFileName.getText().toString(), etText.getText().toString(), etFolder.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Toast.makeText(DebugActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        readButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(encryptedStorageController.get(etFileName.getText().toString()));
                Toast.makeText(DebugActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                Toast.makeText(DebugActivity.this, "Folder to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        readFromFolderButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(encryptedStorageController.get(etFileName.getText().toString(), etFolder.getText().toString()));
                Toast.makeText(DebugActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                Toast.makeText(DebugActivity.this, "Folder to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        clearButton.setOnClickListener(view ->
        {
            StorageController.getInstance().deleteDirContents(getDataDir());
            Toast.makeText(DebugActivity.this, "Poof", Toast.LENGTH_SHORT).show();
        });
    }
}
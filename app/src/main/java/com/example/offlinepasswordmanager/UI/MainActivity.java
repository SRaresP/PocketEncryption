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
import com.example.offlinepasswordmanager.Storage.PwdStorageController;

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

        PwdStorageController pwdStorageController = PwdStorageController.getInstance();

        AppCompatButton readButton = findViewById(R.id.readButton);
        AppCompatButton readFromFileButton = findViewById(R.id.readFromFileButton);
        AppCompatButton writeButton = findViewById(R.id.writeButton);
        AppCompatButton writeToFileButton = findViewById(R.id.writeToFileButton);
        AppCompatButton clearButton = findViewById(R.id.clearButton);

        EditText etPwdName = findViewById(R.id.etPwdName);
        EditText etPwdHash = findViewById(R.id.etPwdHash);
        EditText etFile = findViewById(R.id.etFile);

        TextView tvOutput = findViewById(R.id.tvOutput);

        try {
            pwdStorageController.setup(getDir("Passwords", MODE_PRIVATE));
        } catch (NotDirectoryException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        writeButton.setOnClickListener(view -> {
            try {
                pwdStorageController.addPassword(etPwdName.getText().toString(), etPwdHash.getText().toString());
            } catch(InvalidParameterException e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "We couldn't add your password", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "The chance of this happening was approximately", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "1 in 10 9-illion", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(MainActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        writeToFileButton.setOnClickListener(view -> {
            try {
                pwdStorageController.addPassword(etPwdName.getText().toString(), etPwdHash.getText().toString(), etFile.getText().toString());
            } catch(InvalidParameterException e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "We couldn't add your password", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "The chance of this happening was approximately", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "1 in 10 9-illion", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(MainActivity.this, "Wrote to file", Toast.LENGTH_SHORT).show();
        });

        readButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(pwdStorageController.getPassword(etPwdName.getText().toString()));
                Toast.makeText(MainActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "File to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        readFromFileButton.setOnClickListener(view -> {
            try {
                tvOutput.setText(pwdStorageController.getPassword(etPwdName.getText().toString(), etFile.getText().toString()));
                Toast.makeText(MainActivity.this, "Read from file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                Toast.makeText(MainActivity.this, "File to get password from was not found", Toast.LENGTH_LONG).show();
            }
        });

        clearButton.setOnClickListener(view -> pwdStorageController.clearPasswords());

        CryptoHandler cryptoHandler = CryptoHandler.getInstance(this);
        cryptoHandler.dostuff("this is just a n");
    }
}
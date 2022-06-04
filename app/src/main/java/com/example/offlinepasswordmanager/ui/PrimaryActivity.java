package com.example.offlinepasswordmanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.offlinepasswordmanager.R;

public class PrimaryActivity extends AppCompatActivity {
    private static final String TAG = "PrimaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        DataFragment dataFragment = new DataFragment("");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
        fragmentTransaction.commit();

        AppCompatImageButton settingsB = findViewById(R.id.primaryNavSettingsB);
        AppCompatImageButton entriesB = findViewById(R.id.primaryNavEntriesB);

        settingsB.setOnClickListener(view -> {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentTransaction setFrTransaction = getSupportFragmentManager().beginTransaction();
            setFrTransaction.replace(R.id.primaryWorkLayout, settingsFragment);
            setFrTransaction.commit();
        });

        entriesB.setOnClickListener(view -> {
            DataFragment dataFragmentFresh = new DataFragment("");
            FragmentTransaction entriesFrTransaction = getSupportFragmentManager().beginTransaction();
            entriesFrTransaction.replace(R.id.primaryWorkLayout, dataFragmentFresh);
            entriesFrTransaction.commit();
        });
    }
}
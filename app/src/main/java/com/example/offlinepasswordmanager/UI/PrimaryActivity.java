package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;

import com.example.offlinepasswordmanager.R;

public class PrimaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        DataFragment dataFragment = new DataFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
        fragmentTransaction.commitNow();
        dataFragment.setup(this, "");
    }
}
package com.example.offlinepasswordmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;

import com.example.offlinepasswordmanager.R;

public class CreationActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation);

		AppCompatButton cancelButton = findViewById(R.id.crCancelB);
		//TODO: pass the internal path using an intent to know where to save the created entry


		cancelButton.setOnClickListener(view -> {
			finish();
		});
	}
}
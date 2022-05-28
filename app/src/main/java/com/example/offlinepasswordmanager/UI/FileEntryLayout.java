package com.example.offlinepasswordmanager.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.io.File;

public class FileEntryLayout extends LinearLayoutCompat {
	public FileEntryLayout(@NonNull Context context) {
		super(context);
	}

	public FileEntryLayout(@NonNull AppCompatActivity activity, final String filePath, final String currentPath) {
		super(activity);
		File file = new File(filePath);
		EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(activity);

		setOrientation(LinearLayoutCompat.HORIZONTAL);
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		setGravity(Gravity.CENTER);

		ImageView entryTypeIV = new ImageView(activity);
		entryTypeIV.setLayoutParams(new LinearLayoutCompat.LayoutParams(50, 50));
		TextView entryNameTV = new TextView(activity);
		entryNameTV.setLayoutParams(new LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		entryNameTV.setTextSize(30);
		entryNameTV.setText(new File(filePath).getName());

		if (file.isDirectory()) {
			entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_folder_24));

			setOnClickListener(view -> {
				String internalDirPath = filePath.replaceAll(encryptedStorageController.getEncryptedStorageRootPath(), "");
				DataFragment dataFragment = new DataFragment(internalDirPath);
				FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

				fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
				fragmentTransaction.commitNow();
			});
		} else {
			entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_insert_drive_file_40));

			setOnClickListener(view -> {
				Intent intent = new Intent(activity, TextEditActivity.class);
				intent.putExtra("FilePath", filePath);
				intent.putExtra("CurrentPath", currentPath);
				activity.startActivity(intent);
			});
		}

		setPadding(10, 10, 10, 10);

		addView(entryTypeIV);
		addView(entryNameTV);
	}

	public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


}

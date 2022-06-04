package com.example.offlinepasswordmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;

import java.io.File;

public class FileEntryLayout extends LinearLayoutCompat {
	private File file;

	public FileEntryLayout(@NonNull Context context) {
		super(context);
	}

	public FileEntryLayout(@NonNull final AppCompatActivity activity, @NonNull final DataFragment dataFragment, final String filePath, final String currentPath) {
		super(activity);
		file = new File(filePath);
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

		AppCompatCheckBox checkBox = new AppCompatCheckBox(activity);
		checkBox.setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		checkBox.setOnClickListener(view -> {
			if (checkBox.isChecked()) {
				dataFragment.addToSelection(file);
			} else {
				dataFragment.removeFromSelection(file);
			}
		});

		if (file.isDirectory()) {
			entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_folder_24));

			entryNameTV.setOnClickListener(view -> {
				String internalDirPath = filePath.replaceAll(encryptedStorageController.getEncryptedStorageRootPath(), "");
				DataFragment dataFragmentNew = new DataFragment(internalDirPath);
				FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

				fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragmentNew);
				fragmentTransaction.commitNow();
			});
		} else {
			entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_insert_drive_file_40));

			entryNameTV.setOnClickListener(view -> {
				Intent intent = new Intent(activity, TextEditActivity.class);
				intent.putExtra("FilePath", filePath);
				intent.putExtra("CurrentPath", currentPath);
				activity.startActivity(intent);
			});
		}

		setPadding(10, 10, 10, 10);

		addView(entryTypeIV);
		addView(entryNameTV);
		addView(checkBox);
	}

	public FileEntryLayout(@NonNull AppCompatActivity activity, final String currentPath) {
		super(activity);

		setOrientation(LinearLayoutCompat.HORIZONTAL);
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		setGravity(Gravity.CENTER);

		ImageView entryTypeIV = new ImageView(activity);
		entryTypeIV.setLayoutParams(new LinearLayoutCompat.LayoutParams(50, 50));

		TextView entryNameTV = new TextView(activity);
		entryNameTV.setLayoutParams(new LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		entryNameTV.setTextSize(30);
		entryNameTV.setText(R.string.create_new_entry);

		entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_add_24));

		setOnClickListener(view -> {
			Intent intent = new Intent(activity, CreationActivity.class);
			intent.putExtra("InternalPath", currentPath);
			activity.startActivity(intent);
		});

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

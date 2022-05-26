package com.example.offlinepasswordmanager.UI;

import android.app.Activity;
import android.content.Context;
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
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.io.File;

public class FileEntryLayout extends LinearLayoutCompat {

    private void setup(final AppCompatActivity activity, final String filePath) {
        setOrientation(LinearLayoutCompat.HORIZONTAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.START);

        ImageView entryTypeIV = new ImageView(activity);
        entryTypeIV.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView entryNameTV = new TextView(activity);
        entryNameTV.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        entryNameTV.setTextSize(30);
        entryNameTV.setText(new File(filePath).getName());

        if (new File(filePath).isDirectory()) {
            entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_folder_24));

            setOnClickListener(view -> {
                //TODO: navigate to the folder specified by this FileEntryLayout
                DataFragment dataFragment = new DataFragment(filePath.replaceAll(EncryptedStorageController.getInstance(activity).getEncryptedStorageRootPath(), ""));
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
                fragmentTransaction.commitNow();
            });
        }
        else {
            entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_insert_drive_file_40));

            setOnClickListener(view -> {
                //TODO: start a new activity to view and edit this file if it is a text file
            });
        }

        addView(entryTypeIV);
        addView(entryNameTV);
    }

    public FileEntryLayout(@NonNull Context context) {
        super(context);
    }

    public FileEntryLayout(@NonNull AppCompatActivity activity, final String filePath) {
        super(activity);
        setup(activity, filePath);
    }

    public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

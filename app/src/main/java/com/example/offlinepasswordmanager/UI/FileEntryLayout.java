package com.example.offlinepasswordmanager.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

import com.example.offlinepasswordmanager.R;

public class FileEntryLayout extends LinearLayoutCompat {

    private void setup(Context context, String filePath) {
        setOrientation(LinearLayoutCompat.HORIZONTAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.START);

        ImageView entryTypeIV = new ImageView(context);
        entryTypeIV.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        entryTypeIV.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_insert_drive_file_40));
        TextView entryNameTV = new TextView(context);
        entryNameTV.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        entryNameTV.setTextSize(30);

        addView(entryTypeIV);
        addView(entryNameTV);

        setOnClickListener(view -> {
            //TODO: start a new activity to view, edit or delete the content
        });
    }

    public FileEntryLayout(@NonNull Context context) {
        super(context);
    }

    public FileEntryLayout(@NonNull Context context, String filePath) {
        super(context);
        setup(context, filePath);
    }

    public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FileEntryLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

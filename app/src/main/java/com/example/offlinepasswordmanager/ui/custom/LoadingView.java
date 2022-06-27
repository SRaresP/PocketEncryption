package com.example.offlinepasswordmanager.ui.custom;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.offlinepasswordmanager.R;

public class LoadingView {
	TextView textView;
	ProgressBar progressBar;
	ViewGroup layout;

	public LoadingView(ViewGroup layout, Context context, String message) {
		this.layout = layout;
		progressBar = new ProgressBar(context);
		progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView = new TextView(context);
		textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setText(message);
		textView.setTextSize(20);
		layout.addView(progressBar);
		layout.addView(textView);
	}

	public void terminate() {
		layout.removeView(progressBar);
		layout.removeView(textView);
	}
}

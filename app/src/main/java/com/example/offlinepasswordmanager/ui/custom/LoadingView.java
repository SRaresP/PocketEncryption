package com.example.offlinepasswordmanager.ui.custom;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

public class LoadingView {
	private final ViewGroup layoutToAddTo;
	private final LinearLayoutCompat containerLayout;
	private final View[] children;
	private final View toReplace;

	public LoadingView(final ViewGroup layoutToAddTo, final Context context, final String message, final @Nullable View toReplace, final boolean vertical) {
		this.layoutToAddTo = layoutToAddTo;
		this.toReplace = toReplace;
		ProgressBar progressBar = new ProgressBar(context);
		TextView textView = new TextView(context);

		int childCount = layoutToAddTo.getChildCount();
		children = new View[childCount];
		for (int i = 0; i < childCount; ++i) {
			children[i] = layoutToAddTo.getChildAt(i);
		}

		containerLayout = new LinearLayoutCompat(context);
		if (vertical) containerLayout.setOrientation(LinearLayoutCompat.VERTICAL);
		if (toReplace != null) {
			progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, toReplace.getHeight()));
			containerLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(toReplace.getWidth(), toReplace.getHeight()));
		}
		else {
			progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			containerLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		containerLayout.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
		textView.setText(message);
		containerLayout.addView(progressBar);
		containerLayout.addView(textView);
	}

	public LoadingView show() {
		layoutToAddTo.removeAllViewsInLayout();
		if (children.length > 0) {
			for (View c : children) {
				if (c.equals(toReplace)) layoutToAddTo.addView(containerLayout);
				else layoutToAddTo.addView(c);
			}
		}
		else layoutToAddTo.addView(containerLayout);
		return this;
	}

	public void terminate() {
		layoutToAddTo.removeView(containerLayout);
	}
}

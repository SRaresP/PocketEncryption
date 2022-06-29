package com.example.offlinepasswordmanager.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.offlinepasswordmanager.PocketEncryptionApp;
import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.storage.EncryptedStorageController;
import com.example.offlinepasswordmanager.ui.custom.LoadingView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class DataFragment extends Fragment {
	private static final String TAG = "DataFragment";

	private String internalDirPath;
	private TextView currentPathTV;
	private LinkedList<File> selectedEntries;
	private EncryptedStorageController encryptedStorageController;

	public DataFragment() {
		// Required empty public constructor
	}

	public DataFragment(String internalDirPath) {
		this.internalDirPath = internalDirPath;
	}

	public static DataFragment newInstance() {
		return new DataFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_data, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		currentPathTV = view.findViewById(R.id.dataTVCurrentPath);
		currentPathTV.setText(internalDirPath);
		AppCompatButton deleteSelectedB = view.findViewById(R.id.dataDeleteSelectedB);
		LinearLayoutCompat layout = view.findViewById(R.id.dataLayout);

		PocketEncryptionApp pocketEncryptionApp = PocketEncryptionApp.getInstance();
		FragmentActivity fragmentActivity = requireActivity();
		encryptedStorageController = EncryptedStorageController.getInstance(getActivity());

		String currentPath = currentPathTV.getText().toString();
		if (!currentPath.equals("")) {
			OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
				@Override
				public void handleOnBackPressed() {
					DataFragment dataFragment = new DataFragment(currentPath.substring(0, currentPath.lastIndexOf("/")));
					FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();

					fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
					fragmentTransaction.commitNow();
					remove();
				}
			};
			fragmentActivity.getOnBackPressedDispatcher().addCallback(fragmentActivity, onBackPressedCallback);
		} else {
			OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
				@Override
				public void handleOnBackPressed() {
					fragmentActivity.moveTaskToBack(true);
					remove();
				}
			};
			fragmentActivity.getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
		}

		deleteSelectedB.setOnClickListener(myView -> {
			LoadingView loadingView = new LoadingView(layout, fragmentActivity, getString(R.string.deleting), deleteSelectedB, false).show();
			pocketEncryptionApp.getExecutorService().execute(() -> {
				selectedEntries.forEach(file -> {
					pocketEncryptionApp.getExecutorService().execute(() -> {
						if (file.isDirectory()) {
							encryptedStorageController.deleteDir(file);
						} else {
							if (!file.delete()) {
								Log.i(TAG, "Failed to delete file " + file.getName());
							}
						}
					});
				});
				pocketEncryptionApp.getMainThreadHandler().post(() -> {
					loadingView.terminate();
					DataFragment dataFragmentNew = new DataFragment(internalDirPath);
					FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragmentNew);
					fragmentTransaction.commit();

					Toast.makeText(fragmentActivity, R.string.successfully_deleted_entries, Toast.LENGTH_LONG).show();
				});
			});
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		ViewGroup dataFilesLayout = getView().findViewById(R.id.dataFilesLayout);
		dataFilesLayout.removeAllViews();

		FragmentActivity fragmentActivity = requireActivity();

		ArrayList<String> filePaths = encryptedStorageController.getFilePathsFrom(internalDirPath);

		selectedEntries = new LinkedList<>();

		FileEntryLayout createNewFEL = new FileEntryLayout((AppCompatActivity) fragmentActivity, currentPathTV.getText().toString());
		dataFilesLayout.addView(createNewFEL);
		for (String filePath : filePaths) {
			FileEntryLayout fileEntryLayout = new FileEntryLayout((AppCompatActivity) fragmentActivity, this, filePath, internalDirPath);
			dataFilesLayout.addView(fileEntryLayout);
		}
	}

	public void addToSelection(final File file) {
		selectedEntries.add(file);
	}

	public void removeFromSelection(final File file) {
		selectedEntries.remove(file);
	}
}
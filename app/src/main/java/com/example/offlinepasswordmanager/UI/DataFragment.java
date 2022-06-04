package com.example.offlinepasswordmanager.UI;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

public class DataFragment extends Fragment {
    private static final String TAG = "DataFragment";

    private String internalDirPath;
    private TextView currentPathTV;
    private AppCompatButton deleteSelectedB;
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
        currentPathTV = getView().findViewById(R.id.dataTVCurrentPath);
        currentPathTV.setText(internalDirPath);
        deleteSelectedB = getView().findViewById(R.id.dataDeleteSelectedB);

        encryptedStorageController = EncryptedStorageController.getInstance(getActivity());

        String currentPath = currentPathTV.getText().toString();
        if (!currentPath.equals("")) {
            OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    DataFragment dataFragment = new DataFragment(currentPath.substring(0, currentPath.lastIndexOf("/")));
                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
                    fragmentTransaction.commitNow();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
        }

        deleteSelectedB.setOnClickListener(myView -> {
            selectedEntries.forEach(file -> {
                if (file.isDirectory()) {
                    encryptedStorageController.deleteDir(file);
                }
                else {
                    if (!file.delete()) {
                        Log.i(TAG, "Failed to delete file " + file.getName());
                    }
                }
            });

            DataFragment dataFragmentNew = new DataFragment(internalDirPath);
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragmentNew);
            fragmentTransaction.commit();

            Toast.makeText(requireActivity(), "Successfully deleted entries", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: move all this to a new method called "update()" to allow keeping state when switching back from other fragments
        ViewGroup dataFilesLayout = getView().findViewById(R.id.dataFilesLayout);
        dataFilesLayout.removeAllViews();

        ArrayList<String> filePaths = encryptedStorageController.getFilePathsFrom(internalDirPath);

        selectedEntries = new LinkedList<File>();

        FileEntryLayout createNewFEL = new FileEntryLayout((AppCompatActivity)requireActivity(), currentPathTV.getText().toString());
        dataFilesLayout.addView(createNewFEL);
        for (String filePath : filePaths) {
            FileEntryLayout fileEntryLayout = new FileEntryLayout((AppCompatActivity)requireActivity(), this, filePath, internalDirPath);
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
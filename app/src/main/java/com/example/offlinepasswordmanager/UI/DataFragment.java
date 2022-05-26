package com.example.offlinepasswordmanager.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.util.ArrayList;

//TODO: Add an "add" button somewhere
public class DataFragment extends Fragment {
    private String internalDirPath;

    public DataFragment() {
        // Required empty public constructor
    }
    public DataFragment(String internalDirPath) {
        this.internalDirPath = internalDirPath;
    }

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        ViewGroup dataFilesLayout =  getView().findViewById(R.id.dataFilesLayout);

        EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(getActivity());
        ArrayList<String> filePaths = encryptedStorageController.getFilePathsFrom(internalDirPath);
        for (String filePath : filePaths) {
            FileEntryLayout fileEntryLayout = new FileEntryLayout((AppCompatActivity)getActivity(), filePath);
            dataFilesLayout.addView(fileEntryLayout);
        }
    }
}
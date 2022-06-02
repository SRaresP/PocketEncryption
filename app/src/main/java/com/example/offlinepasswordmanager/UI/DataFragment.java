package com.example.offlinepasswordmanager.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.offlinepasswordmanager.R;
import com.example.offlinepasswordmanager.Storage.EncryptedStorageController;

import java.util.ArrayList;

//TODO: Add an "add" button somewhere
public class DataFragment extends Fragment {

    private String internalDirPath;
    private TextView currentPathTV;

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
        currentPathTV = getView().findViewById(R.id.dataTVCurrentPath);
        currentPathTV.setText(internalDirPath);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String currentPath = currentPathTV.getText().toString();
                DataFragment dataFragment = new DataFragment(currentPath.substring(0, currentPath.lastIndexOf("/")));
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.primaryWorkLayout, dataFragment);
                fragmentTransaction.commitNow();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup dataFilesLayout = getView().findViewById(R.id.dataFilesLayout);
        dataFilesLayout.removeAllViews();

        EncryptedStorageController encryptedStorageController = EncryptedStorageController.getInstance(getActivity());
        ArrayList<String> filePaths = encryptedStorageController.getFilePathsFrom(internalDirPath);

        FileEntryLayout createNewFEL = new FileEntryLayout((AppCompatActivity)requireActivity(), currentPathTV.getText().toString());
        dataFilesLayout.addView(createNewFEL);
        for (String filePath : filePaths) {
            FileEntryLayout fileEntryLayout = new FileEntryLayout((AppCompatActivity)requireActivity(), filePath, internalDirPath);
            dataFilesLayout.addView(fileEntryLayout);
        }
    }
}
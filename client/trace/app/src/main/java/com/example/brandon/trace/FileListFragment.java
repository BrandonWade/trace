package com.example.brandon.trace;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Fragment containing a list of files.
 */
public class FileListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (FileUtils.fileList == null) {
            FileUtils.fileList = new ArrayList<>();
        }

        if (FileUtils.fileListAdapter == null) {
            FileUtils.fileListAdapter = new FileListItemAdapter(getActivity().getApplicationContext(), R.layout.file_list_row, FileUtils.fileList);
        }

        setListAdapter(FileUtils.fileListAdapter);
    }
}

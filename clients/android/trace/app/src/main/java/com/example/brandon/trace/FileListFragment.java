package com.example.brandon.trace;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Brandon on 10/1/2016.
 */
public class FileListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FileUtils.fileList = new ArrayList<>();
        FileUtils.fileListAdapter = new FileListItemAdapter(getActivity().getApplicationContext(), R.layout.file_list_row, FileUtils.fileList);
        setListAdapter(FileUtils.fileListAdapter);
    }
}

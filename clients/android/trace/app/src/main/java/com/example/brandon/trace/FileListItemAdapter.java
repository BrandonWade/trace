package com.example.brandon.trace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter used to inflate a list of files.
 */
public class FileListItemAdapter extends ArrayAdapter<FileListItem> {

    private LayoutInflater inflater;
    private ArrayList<FileListItem> files;

    public FileListItemAdapter(Context context, int layoutResourceID, ArrayList<FileListItem> files) {
        super(context, layoutResourceID, files);

        this.inflater = LayoutInflater.from(context);
        this.files = files;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FileListItemHolder holder;

        if (convertView == null) {
            holder = new FileListItemHolder();
            convertView = inflater.inflate(R.layout.file_list_row, null);

            holder.mainText = (TextView)convertView.findViewById(R.id.row_main_text);
            holder.subText = (TextView)convertView.findViewById(R.id.row_sub_text);
            holder.progress = (ProgressBar)convertView.findViewById(R.id.row_progress);

            convertView.setTag(holder);
        } else {
            holder = (FileListItemHolder)convertView.getTag();
        }

        FileListItem file = files.get(position);
        int progress = (int)(100 * ((double)file.progress / (double)file.size));
        holder.mainText.setText(file.fileName);
        holder.subText.setText(file.status);
        holder.progress.setProgress(progress);

        if (!file.showProgress) {
            holder.progress.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class FileListItemHolder {
        TextView mainText;
        TextView subText;
        ProgressBar progress;
    }
}

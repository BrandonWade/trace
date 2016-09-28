package com.example.brandon.trace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brandon on 9/27/2016.
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

//            holder.icon = (ImageView)convertView.findViewById(R.id.row_icon);
            holder.mainText = (TextView)convertView.findViewById(R.id.row_main_text);
            holder.subText = (TextView)convertView.findViewById(R.id.row_sub_text);

            convertView.setTag(holder);
        } else {
            holder = (FileListItemHolder)convertView.getTag();
        }

        FileListItem file = files.get(position);
//        holder.icon.setImageBitmap(file.getIcon());
        holder.mainText.setText(file.fileName);
        holder.subText.setText(file.status);

        return convertView;
    }

    private static class FileListItemHolder {
//        ImageView icon;
        TextView mainText;
        TextView subText;
    }
}

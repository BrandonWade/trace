package com.example.brandon.trace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to inflate a list of files.
 */
public class FileListItemAdapter extends ArrayAdapter<FileListItem> {

    private LayoutInflater inflater;
    private List<FileListItem> files;

    public FileListItemAdapter(Context context, int layoutResourceID, List<FileListItem> files) {
        super(context, layoutResourceID, files);

        this.inflater = LayoutInflater.from(context);
        this.files = files;
    }

    @Override
    public @NonNull View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        FileListItemHolder holder;

        if (convertView == null) {
            holder = new FileListItemHolder();
            convertView = inflater.inflate(R.layout.file_list_row, null);

            holder.progress = convertView.findViewById(R.id.row_progress_complete);
            holder.selected = (CheckBox)convertView.findViewById(R.id.row_checkbox);
            holder.mainText = (TextView)convertView.findViewById(R.id.row_main_text);
            holder.subText = (TextView)convertView.findViewById(R.id.row_sub_text);

            convertView.setTag(holder);
        } else {
            holder = (FileListItemHolder)convertView.getTag();
        }

        holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                files.get(position).checked = isChecked;
                UIUtils.toggleConfirmButton(UIUtils.canConfirmDownload());
            }

        });

        FileListItem file = files.get(position);
        holder.selected.setChecked(file.checked);
        holder.selected.setClickable(file.enabled);
        holder.selected.setVisibility(file.selectable ? View.VISIBLE : View.INVISIBLE);
        holder.mainText.setText(file.displayName);
        holder.subText.setText(file.status);

        float progress = (float)(file.progress / file.size);
        LinearLayout.LayoutParams progressCompleteParams = (LinearLayout.LayoutParams) holder.progress.getLayoutParams();
        progressCompleteParams.weight = progress;
        holder.progress.setLayoutParams(progressCompleteParams);

        return convertView;
    }

    private static class FileListItemHolder {
        View progress;
        CheckBox selected;
        TextView mainText;
        TextView subText;
    }
}

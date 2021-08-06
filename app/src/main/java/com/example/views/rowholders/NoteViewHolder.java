package com.example.views.rowholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_project_10_mynotes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_title, txt_note, txt_date_time;
    public ViewGroup root;

    public NoteViewHolder(View itemView) {
        super(itemView);

        txt_title = itemView.findViewById(R.id.txt_title);
        txt_note = itemView.findViewById(R.id.txt_note);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        root = itemView.findViewById(R.id.root);
    }
}

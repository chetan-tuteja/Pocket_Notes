package com.chetantuteja.pocketnotes.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chetantuteja.pocketnotes.R;
import com.chetantuteja.pocketnotes.models.Note;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotesRecyclerAdapter";

    private ArrayList<Note> mNotes;
    private View.OnClickListener myClickListener;

    public NotesRecyclerAdapter(ArrayList<Note> mNotes) {
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.noteTitle.setText(mNotes.get(position).getTitle());
            holder.noteTimeStamp.setText(mNotes.get(position).getTimeStamp());
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: Error"+e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setMyClickListener(View.OnClickListener clickListener){
        myClickListener = clickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView noteTitle, noteTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteTimeStamp = itemView.findViewById(R.id.noteTimeStamp);
            itemView.setTag(this);
            itemView.setOnClickListener(myClickListener);
        }
    }




}

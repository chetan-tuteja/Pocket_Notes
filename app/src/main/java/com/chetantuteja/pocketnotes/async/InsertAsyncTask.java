package com.chetantuteja.pocketnotes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.chetantuteja.pocketnotes.models.Note;
import com.chetantuteja.pocketnotes.persistence.NoteDAO;
import com.chetantuteja.pocketnotes.persistence.NoteDatabase;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "InsertAsyncTask";

    private NoteDAO mNoteDAO;

    public InsertAsyncTask(NoteDAO mNoteDAO) {
        this.mNoteDAO = mNoteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: Thread Used = "+Thread.currentThread().getName());
        mNoteDAO.insertNotes(notes);
        return null;
    }
}

package com.chetantuteja.pocketnotes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.chetantuteja.pocketnotes.models.Note;
import com.chetantuteja.pocketnotes.persistence.NoteDAO;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "DeleteAsyncTask";

    private NoteDAO mNoteDAO;

    public DeleteAsyncTask(NoteDAO mNoteDAO) {
        this.mNoteDAO = mNoteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: Thread Used = "+Thread.currentThread().getName());
        mNoteDAO.deleteNotes(notes);
        return null;
    }
}

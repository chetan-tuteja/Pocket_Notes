package com.chetantuteja.pocketnotes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.chetantuteja.pocketnotes.async.DeleteAsyncTask;
import com.chetantuteja.pocketnotes.async.InsertAsyncTask;
import com.chetantuteja.pocketnotes.async.UpdateAsyncTask;
import com.chetantuteja.pocketnotes.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context mContext) {
        mNoteDatabase = NoteDatabase.getInstance(mContext);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDAO()).execute(note);
    }

    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDAO()).execute(note);
    }

    public LiveData<List<Note>> fetchNoteTask(){
        return mNoteDatabase.getNoteDAO().getNotes();
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDAO()).execute(note);
    }
}

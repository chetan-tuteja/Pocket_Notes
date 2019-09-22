package com.chetantuteja.pocketnotes.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.chetantuteja.pocketnotes.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    long[] insertNotes(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Delete
    int deleteNotes(Note... notes);

    @Update
    int updateNotes(Note... notes);
}

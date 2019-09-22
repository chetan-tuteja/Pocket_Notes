package com.chetantuteja.pocketnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chetantuteja.pocketnotes.adapters.NotesRecyclerAdapter;
import com.chetantuteja.pocketnotes.models.Note;
import com.chetantuteja.pocketnotes.persistence.NoteRepository;
import com.chetantuteja.pocketnotes.utility.VerticalSpacingDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //UI
    private RecyclerView mReyclerView;
    private FloatingActionButton mFabAdd;

    //Variables
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mAdapter;
    private NoteRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupRecyclerView();
        //addTempNotes();
        fetchNotes();

        setSupportActionBar((Toolbar) findViewById(R.id.noteToolbar));
        getSupportActionBar().setIcon(R.drawable.ic_note);
        getSupportActionBar().setTitle("  Pocket Notes");

    }

    private View.OnClickListener recyclerViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder vh = (RecyclerView.ViewHolder) view.getTag();
            int position = vh.getAdapterPosition();
            Log.d(TAG, "onClick: Reycler View Clicked");

            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            intent.putExtra("selectedNote",mNotes.get(position));
            startActivity(intent);
            
        }
    };

    private void setupViews(){
        mReyclerView = findViewById(R.id.mainRecyclerView);
        mFabAdd = findViewById(R.id.fabAdd);
        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });
        mRepo = new NoteRepository(this);
    }

    private void fetchNotes(){
        mRepo.fetchNoteTask().observe(this, notes -> {
            if(mNotes.size()>0){
                mNotes.clear();
            }
            if(notes!=null){
                mNotes.addAll(notes);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void setupRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mReyclerView.setLayoutManager(manager);
        VerticalSpacingDecorator dec = new VerticalSpacingDecorator(10);
        mReyclerView.addItemDecoration(dec);
        mAdapter = new NotesRecyclerAdapter(mNotes);
        mAdapter.setMyClickListener(recyclerViewClick);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mReyclerView);
        mReyclerView.setAdapter(mAdapter);
    }

    private void addTempNotes(){
        for(int i=0;i<20;i++){
            Note note = new Note();
            note.setTitle("Title #"+i);
            note.setContent("Content #"+i);
            note.setTimeStamp("Sep 2019");

            mNotes.add(note);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void removeNotes(Note note) {
        mNotes.remove(note);
        mAdapter.notifyDataSetChanged();

        mRepo.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            removeNotes(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };

}

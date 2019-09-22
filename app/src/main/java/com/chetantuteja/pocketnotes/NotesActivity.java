package com.chetantuteja.pocketnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chetantuteja.pocketnotes.models.Note;
import com.chetantuteja.pocketnotes.persistence.NoteRepository;
import com.chetantuteja.pocketnotes.utility.TimestampUtil;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NotesActivity";


    public enum NoteMode{
        EDIT_MODE_ENABLED, EDIT_MODE_DISABLED
    }

    //UI
    private EditText noteContent;
    private EditText noteToolbarET;
    private TextView noteToolbarTV;
    private RelativeLayout mBackBtnContainer, mCheckContainer;
    private ImageButton mArrowBtn, mCheckBtn;
    //Variables
    private boolean mIsNewNote;
    private Note mInitialNote;
    private Note mFinalNote;
    private NoteMode mMode;
    private NoteRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setupViews();
        setupFunctionality();
        setupListeners();
    }

    private void setupViews(){
        noteContent = findViewById(R.id.noteContent);
        noteToolbarET = findViewById(R.id.noteEditTitleToolbar);
        noteToolbarTV = findViewById(R.id.noteTitleToolbar);
        mBackBtnContainer = findViewById(R.id.backBtnContainer);
        mCheckContainer = findViewById(R.id.checkBtnContainer);
        mArrowBtn = findViewById(R.id.toolbarBackBtn);
        mCheckBtn = findViewById(R.id.toolbarCheckBtn);
        mRepo = new NoteRepository(this);
    }

    private boolean fetchIntent(){
        if(getIntent().hasExtra("selectedNote")){
            mInitialNote = getIntent().getParcelableExtra("selectedNote");
            //mFinalNote = getIntent().getParcelableExtra("selectedNote");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimeStamp(mInitialNote.getTimeStamp());
            mFinalNote.setId(mInitialNote.getId());

            //Log.d(TAG, "fetchIntent: "+mInitialNote.toString());
            mMode = NoteMode.EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = NoteMode.EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void setupFunctionality(){
        if(fetchIntent()){
            setupNewNote();
            enableEditMode();
        }
        else {
            setupExistingNote();
            disableContentInteraction();
        }
    }

    private void setupNewNote(){
        /*noteToolbarTV.setText("Note Title");
        noteToolbarET.setText("Note Title");*/

        mInitialNote = new Note();
        mFinalNote = new Note();

        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");

        noteToolbarET.setText(mFinalNote.getTitle());
        noteToolbarTV.setText(mFinalNote.getTitle());

    }

    private void setupExistingNote(){
        noteToolbarET.setText(mInitialNote.getTitle());
        noteToolbarTV.setText(mInitialNote.getTitle());
        noteContent.setText(mInitialNote.getContent());
    }

    private void saveChanges(){
        if(mIsNewNote){
            saveNewNote();
        } else {
            updateNote();
        }
    }

    private void updateNote(){
        mRepo.updateNote(mFinalNote);
    }

    private void saveNewNote(){
        mRepo.insertNoteTask(mFinalNote);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners(){
        noteContent.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "onDoubleTap: doubled tapped");
                    enableEditMode();
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        noteToolbarTV.setOnClickListener(this);
        mCheckBtn.setOnClickListener(this);
        mArrowBtn.setOnClickListener(this);
        noteToolbarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteToolbarTV.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void enableEditMode(){
        mBackBtnContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        noteToolbarTV.setVisibility(View.GONE);
        noteToolbarET.setVisibility(View.VISIBLE);


        mMode = NoteMode.EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode(){
        mBackBtnContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        noteToolbarTV.setVisibility(View.VISIBLE);
        noteToolbarET.setVisibility(View.GONE);

        mMode = NoteMode.EDIT_MODE_DISABLED;
        disableContentInteraction();
        if(!trimContent().isEmpty()){
            mFinalNote.setTitle(noteToolbarET.getText().toString());
            mFinalNote.setContent(noteContent.getText().toString());
            mFinalNote.setTimeStamp(retTime());

            if(!mFinalNote.getContent().equals(mInitialNote.getContent())|| !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                saveChanges();
            }
        }

    }

    private String retTime(){
        if(TimestampUtil.getCurrentTimestamp() != null){
            return TimestampUtil.getCurrentTimestamp().replace("-"," ");
        }
        return "Error";
    }

    private String trimContent(){
        String temp = noteContent.getText().toString();
        temp = temp.trim().replace("\n","");
        temp = temp.replace(" ","");
        return temp;
    }

    private void hideSoftKeyBoard(){
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void showEditTextKeyboard(){
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(noteToolbarET,InputMethodManager.SHOW_IMPLICIT);
    }

    private void disableContentInteraction(){
        noteContent.setKeyListener(null);
        noteContent.setFocusable(false);
        noteContent.setFocusableInTouchMode(false);
        noteContent.setCursorVisible(false);
        noteContent.clearFocus();
    }

    private void enableContentInteraction(){
        noteContent.setKeyListener(new EditText(this).getKeyListener());
        noteContent.setFocusable(true);
        noteContent.setFocusableInTouchMode(true);
        noteContent.setCursorVisible(true);
        noteContent.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbarCheckBtn: {
                hideSoftKeyBoard();
                disableEditMode();
                break;
            }

            case R.id.noteTitleToolbar: {
                enableEditMode();
                noteToolbarET.requestFocus();
                noteToolbarET.setSelection(noteToolbarET.length());
                showEditTextKeyboard();
                break;
            }

            case R.id.toolbarBackBtn:{
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mMode == NoteMode.EDIT_MODE_ENABLED){
            onClick(mCheckBtn);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mode",mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = (NoteMode) savedInstanceState.getSerializable("mode");
        if(mMode == NoteMode.EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }
}

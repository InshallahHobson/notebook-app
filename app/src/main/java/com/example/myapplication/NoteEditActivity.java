package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityNoteEditBinding;
import com.example.myapplication.databinding.ActivityScrollingBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class NoteEditActivity extends MainActivity implements View.OnClickListener {

    private ActivityNoteEditBinding binding;
    private ImageButton arrowButton;
    private FloatingActionButton saveBtn;
    private String mode;
    private EditText content, title;
    private String id;
    private Uri uri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrowButton = findViewById(R.id.arrow);
        setArrowButton(arrowButton);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        uri = getIntent().getData();

        FloatingActionButton viewBtn = binding.viewNote;
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteEditActivity.this, NoteViewActivity.class);
                i.setData(uri);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        //content = (EditText) findViewById(R.id.content);
        //title = (EditText) findViewById(R.id.title);

        if (this.getIntent().getData() != null) {
            Uri uri = this.getIntent().getData();
            loadNoteInfo(uri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setArrowButton(ImageButton arrowButton) {
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //will close the moment activity and return to
                //the last activity
            }
        });
    }

    @Override
    public void onClick(View view) {
        //Retrieve save data (passed from intent instead?)
        String myContent = content.getText().toString();
        String myTitle = title.getText().toString();

        //Save new note data
        ContentValues values = new ContentValues();
        values.put(NotesDB.KEY_CONTENT, myContent);
        values.put(NotesDB.KEY_TITLE, myTitle);

        //Update note record in db
        Uri uri = Uri.parse(ContentProvider.CONTENT_URI + "/" + id);
        getContentResolver().update(uri, values, null, null);
        finish();

        //Note note = new Note();
        //Intent i = new Intent(NoteEditActivity.this, NoteViewActivity.class);
        //i.putExtra("com.example.myapplication.Note", note);
        //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //startActivity(i);
        //finish();
    }

    //Use rowId to get all info from Content Provider about the Note
    private void loadNoteInfo(Uri uri) {
        String[] projection = {
                NotesDB.KEY_ROWID,
                NotesDB.KEY_TITLE,
                NotesDB.KEY_CONTENT
        };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow(NotesDB.KEY_TITLE));
            String noteContent = cursor.getString(cursor.getColumnIndexOrThrow(NotesDB.KEY_CONTENT));
            title.setText(noteTitle);
            content.setText(noteContent);

        }
        cursor.close();
    }
}

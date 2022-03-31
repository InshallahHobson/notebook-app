package com.example.myapplication;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myapplication.databinding.ActivityScrollingBinding;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements LoaderManager .LoaderCallbacks<Cursor> {

    private ActivityScrollingBinding binding;
    //private EditText content, title;
    private String id;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private final LinkedList<String> mNoteList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton newNoteBtn = binding.newNote;
        newNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(NotesDB.KEY_CONTENT, "");
                values.put(NotesDB.KEY_TITLE, "Untitled");
                Uri uri = getContentResolver().insert(ContentProvider.CONTENT_URI, values);

                Intent i = new Intent(MainActivity.this, NoteEditActivity.class);
                i.setData(uri);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        Bundle notes = getIntent().getExtras();
        //Note obj = notes.getParcelable("com.example.myapplication.Note");





        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new NoteListAdapter(this, mNoteList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    //Use rowId to get all info from Content Provider about the Note
    private void loadAllNotes() {
        String[] projection = {
                NotesDB.KEY_ROWID,
                NotesDB.KEY_TITLE,
                NotesDB.KEY_CONTENT
        };
        //for all ids
        Uri uri = Uri.parse(ContentProvider.CONTENT_URI.toString());
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow(NotesDB.KEY_TITLE));
                String noteId = cursor.getString(cursor.getColumnIndexOrThrow(NotesDB.KEY_ROWID));
                //mWordList.addLast(noteTitle);
                //title.setText(noteTitle);
                //Button goToNoteBtn = binding.goToNote;
                //goToNoteBtn.setText(noteTitle);
                //goToNoteBtn.setOnClickListener(new View.OnClickListener() {
                    //@Override
                    //public void onClick(View view) {
                        //Redirect to edit
                        // pass KEYROWID in intent to other activity
                        //Intent i = new Intent(MainActivity.this, NoteEditActivity.class);
                        //i.putExtra("com.example.myapplication.Note", note);
                        //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(i);
                    //}
                //});
            }
        }
        cursor.close();
    }

    public loadNote () {



    }
}


//onClick for delete buttons:
//Uri uri = Uri.parse(ContentProvider.CONTENT_URI  + "/" + id);
//getContentResolver().delete(uri, null, null);
//finish();

//<Button
    //android:id="@+id/goToNote"
    //android:layout_width="match_parent"
    //android:layout_height="wrap_content"
    //android:text="@string/note_title" />



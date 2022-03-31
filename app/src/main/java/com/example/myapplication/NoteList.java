/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

/**
 * Implements a basic RecyclerView that displays a list of generated words.
 * - Clicking an item marks it as clicked.
 * - Clicking the fab button adds a new note to the list.
 */
public class NoteList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private final LinkedList<String> mNoteList = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        // Put initial data into the note list.
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
                mNoteList.addLast(noteId + ": " + noteTitle);
            }
        }

        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new NoteListAdapter(this, mNoteList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
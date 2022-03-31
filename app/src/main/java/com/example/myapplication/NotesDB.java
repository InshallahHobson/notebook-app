package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class NotesDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private static final String TAG = "NotesDb";
    public static final String SQLITE_TABLE = "Note";

    private static final String TABLE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                    KEY_TITLE +
                    ");";

    public static void onCreate (SQLiteDatabase db) {
        Log.w(TAG, "onCreate: TABLE_CREATE");
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from Version " +oldVersion + " to " +
                newVersion + " , which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}

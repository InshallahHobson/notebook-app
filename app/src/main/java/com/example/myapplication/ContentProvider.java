package com.example.myapplication;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentProvider extends android.content.ContentProvider {
    private DatabaseHelper dbHelper;
    private static final int ALL_NOTES = 1;
    private static final int SINGLE_NOTE = 2;

    // Authority is the symbolic name of the provider
    // Internet domain ownership (in reverse) is used for the basis of the provider authority
    private static final String AUTHORITY = "com.example.myapplication";

    // Create content URIs from the authority by appending path to database table
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

    // Content URI pattern matches content URIs using wildcard characters
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher((UriMatcher.NO_MATCH));
        uriMatcher.addURI(AUTHORITY, "notes", ALL_NOTES);
        uriMatcher.addURI(AUTHORITY, "notes/#", SINGLE_NOTE);
    }

    @Override
    public boolean onCreate() {
        // Get access to database helper
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    // The query() method must return a Cursor object, or if it fails, throw an exception.
    // Returns Cursor returned by one of the query() methods of the SQLiteDatabase class.
    // Returns null if an internal error occurred during the query process.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NotesDB.SQLITE_TABLE);
        switch (uriMatcher.match(uri)) {
            case ALL_NOTES:
                break;
            case SINGLE_NOTE:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(NotesDB.KEY_ROWID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_NOTES:
                return "vnd.android.cursor.dir/vnd.com.example.myapplication.contentprovider.notes";
            case SINGLE_NOTE:
                return "vnd.android.cursor.item/vnd.com.example.myapplication.contentprovider.notes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_NOTES:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(NotesDB.SQLITE_TABLE, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }


    // The delete() method deletes rows based on the selection or if an id is provided then it
    // deletes a single row. The method returns the number of records affected from db.
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_NOTES:
                break;
            case SINGLE_NOTE:
                String id = uri.getPathSegments().get(1);
                selection = NotesDB.KEY_ROWID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(NotesDB.SQLITE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    // The update method() is same as delete() which updates multiple rows based on the selection
    // or a single row if the row id is provided.
    // The update method returns the number of updated rows.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch  (uriMatcher.match(uri)) {
            case ALL_NOTES:
                break;
            case SINGLE_NOTE:
                String id = uri.getPathSegments().get(1);
                selection = NotesDB.KEY_ROWID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int updateCount = db.update(NotesDB.SQLITE_TABLE, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}

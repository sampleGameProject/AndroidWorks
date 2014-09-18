package com.example.admin.labs.models.helpers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by admin on 17.09.2014.
 */
public abstract class ContentHelper  {

    protected abstract String getTable();
    protected abstract Uri getContentUri();

    public Cursor query(SQLiteDatabase db, ContentResolver contentResolver, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = db.query(getTable(), projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(contentResolver, getContentUri());
        return cursor;
    }

    public Uri insert(SQLiteDatabase db, ContentResolver contentResolver, ContentValues values) {

        long rowID = db.insert(getTable(), null, values);
        Uri resultUri = ContentUris.withAppendedId(getContentUri(), rowID);
        contentResolver.notifyChange(resultUri, null);
        return resultUri;

    }

    public int delete(SQLiteDatabase db, ContentResolver contentResolver,Uri uri, String selection, String[] selectionArgs) {
        int cnt = db.delete(getTable(), selection, selectionArgs);
        contentResolver.notifyChange(uri, null);
        return cnt;
    }

    public int update(SQLiteDatabase db, ContentResolver contentResolver,Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int cnt = db.update(getTable(), values, selection, selectionArgs);
        contentResolver.notifyChange(uri, null);
        return cnt;
    }




}

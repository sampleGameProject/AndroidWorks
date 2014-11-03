package pigareva_10moa.lab45.data.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by admin on 28.10.2014.
 */public class ContentHelper  {

    protected String table;
    protected Uri contentUri;

    public ContentHelper(String table, Uri uri){
        this.table = table;
        this.contentUri = uri;
    }

    public Cursor query(SQLiteDatabase db, ContentResolver contentResolver, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = db.query(table, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(contentResolver, contentUri);
        return cursor;
    }

    public Uri insert(SQLiteDatabase db, ContentResolver contentResolver, ContentValues values) {

        long rowID = db.insert(table, null, values);
        Uri resultUri = ContentUris.withAppendedId(contentUri, rowID);
        contentResolver.notifyChange(resultUri, null);
        return resultUri;

    }

    public int delete(SQLiteDatabase db, ContentResolver contentResolver,Uri uri, String selection, String[] selectionArgs) {
        int cnt = db.delete(table, selection, selectionArgs);
        contentResolver.notifyChange(uri, null);
        return cnt;
    }

    public int update(SQLiteDatabase db, ContentResolver contentResolver,Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int cnt = db.update(table, values, selection, selectionArgs);
        contentResolver.notifyChange(uri, null);
        return cnt;
    }




}

package pigareva_10moa.lab45.data;

/**
 * Created by admin on 28.10.2014.
 */

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import pigareva_10moa.lab45.data.models.ContentHelper;


/**
 * Created by admin on 17.09.2014.
 */

// контент провайдер предоставляет 4 операции - query, insert, update, delete
public class PosterContentProvider extends ContentProvider {

    // необходимые константы

    private static final String LOG_TAG = "CONTENT_PROVIDER";

    private static final String AUTHORITY = "pigareva_10moa.contentprovider";

    private static final String THEATERS_PATH = DB.TABLE_THEATRES;
    private static final String MOVIES_PATH = DB.TABLE_MOVIES;
    private static final String SEANCES_PATH = DB.TABLE_SEANCES;

    public static final Uri THEATERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + THEATERS_PATH);

    public static final Uri MOVIES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + MOVIES_PATH);

    public static final Uri SEANCES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + SEANCES_PATH);

    public static final String THEATERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + THEATERS_PATH;

    public static final String THEATERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + THEATERS_PATH;

    public static final String MOVIES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + MOVIES_PATH;

    public static final String MOVIES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + MOVIES_PATH;

    public static final String SEANCES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + SEANCES_PATH;

    public static final String SEANCES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + SEANCES_PATH;

    public static final int URI_THEATERS = 1;
    public static final int URI_THEATER_ID = 2;

    public static final int URI_MOVIES = 3;
    public static final int URI_MOVIE_ID = 4;

    public static final int URI_SEANCES = 5;
    public static final int URI_SEANCE_ID = 6;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, THEATERS_PATH, URI_THEATERS);
        uriMatcher.addURI(AUTHORITY, THEATERS_PATH + "/#", URI_THEATER_ID);
        uriMatcher.addURI(AUTHORITY, MOVIES_PATH, URI_MOVIES);
        uriMatcher.addURI(AUTHORITY, MOVIES_PATH + "/#", URI_MOVIE_ID);
        uriMatcher.addURI(AUTHORITY, SEANCES_PATH, URI_SEANCES);
        uriMatcher.addURI(AUTHORITY, SEANCES_PATH + "/#", URI_SEANCE_ID);
    }

    private PosterSQLiteHepler dbHelper;
    private SQLiteDatabase db;
    private ContentHelper theatersContentHelper, moviesContentHelper, seancesContentHelper;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new PosterSQLiteHepler(getContext());

        // хелперы для обработки запросов
        theatersContentHelper = new ContentHelper(DB.TABLE_THEATRES,THEATERS_CONTENT_URI );
        moviesContentHelper = new ContentHelper(DB.TABLE_MOVIES ,MOVIES_CONTENT_URI );
        seancesContentHelper = new ContentHelper(DB.TABLE_SEANCES,SEANCES_CONTENT_URI );

        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_MOVIES:
            case URI_SEANCES:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DB.COLUMN_ID + " ASC";
                break;

            case URI_MOVIE_ID:
            case URI_SEANCE_ID:
            case URI_THEATER_ID:
            {
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = DB.COLUMN_ID  + " = " + id;
                } else {
                    selection = selection + " AND " + DB.COLUMN_ID + " = " + id;
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_THEATER_ID:
                return theatersContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_MOVIES:
            case URI_MOVIE_ID:
                return moviesContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_SEANCES:
            case URI_SEANCE_ID:{
                final String seances = "select " +
                        "* " +
                        "from movies join seances " +
                        "on movies._id = seances.movie_id " +
                        "where " +
                        selection;

                return db.rawQuery( seances,null );
            }

        }

        return null;
    }


    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch){
            case URI_THEATERS:
            case URI_SEANCES:
            case URI_MOVIES:
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch){
            case URI_THEATERS:
                return theatersContentHelper.insert(db,resolver,values);
            case URI_MOVIES:
                return moviesContentHelper.insert(db,resolver,values);
            case URI_SEANCES:
                return seancesContentHelper.insert(db,resolver,values);
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_MOVIES:
            case URI_SEANCES:
                break;

            case URI_MOVIE_ID:
            case URI_SEANCE_ID:
            case URI_THEATER_ID:
            {
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = DB.COLUMN_ID  + " = " + id;
                } else {
                    selection = selection + " AND " + DB.COLUMN_ID + " = " + id;
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_THEATER_ID:
                return theatersContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_MOVIES:
            case URI_MOVIE_ID:
                return moviesContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_SEANCES:
            case URI_SEANCE_ID:
                return seancesContentHelper.delete(db,resolver,uri,selection,selectionArgs);
        }

        return -1;

    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_MOVIES:
            case URI_SEANCES:
                break;

            case URI_MOVIE_ID:
            case URI_SEANCE_ID:
            case URI_THEATER_ID:
            {
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = DB.COLUMN_ID  + " = " + id;
                } else {
                    selection = selection + " AND " + DB.COLUMN_ID + " = " + id;
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_THEATERS:
            case URI_THEATER_ID:
                return theatersContentHelper.update(db, resolver, uri,values, selection, selectionArgs);
            case URI_MOVIES:
            case URI_MOVIE_ID:
                return moviesContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            case URI_SEANCES:
            case URI_SEANCE_ID:
                return seancesContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_THEATERS:
                return THEATERS_CONTENT_TYPE;
            case URI_THEATER_ID:
                return THEATERS_CONTENT_ITEM_TYPE;
            case URI_SEANCES:
                return SEANCES_CONTENT_TYPE;
            case URI_SEANCE_ID:
                return SEANCES_CONTENT_ITEM_TYPE;
            case URI_MOVIES:
                return MOVIES_CONTENT_TYPE;
            case URI_MOVIE_ID:
                return MOVIES_CONTENT_ITEM_TYPE;
        }
        return null;
    }

}

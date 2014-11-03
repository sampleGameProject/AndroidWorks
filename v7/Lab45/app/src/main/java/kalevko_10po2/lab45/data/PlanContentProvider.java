package kalevko_10po2.lab45.data;

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

import kalevko_10po2.lab45.data.models.ContentHelper;


/**
 * Created by admin on 17.09.2014.
 */

// контент провайдер предоставляет 4 операции - query, insert, update, delete
public class PlanContentProvider extends ContentProvider {

    // необходимые константы

    private static final String LOG_TAG = "CONTENT_PROVIDER";

    private static final String AUTHORITY = "kalevko_10po2.contentprovider";

    private static final String TEACHERS_PATH = DB.TABLE_TEACHERS;
    private static final String SUBJECTS_PATH = DB.TABLE_SUBJECTS;
    private static final String PLAN_ITEMS_PATH = DB.TABLE_PLAN_ITEMS;

    public static final Uri TEACHERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TEACHERS_PATH);

    public static final Uri SUBJECTS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + SUBJECTS_PATH);

    public static final Uri PLAN_ITEMS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PLAN_ITEMS_PATH);

    public static final String TEACHERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + TEACHERS_PATH;

    public static final String TEACHERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + TEACHERS_PATH;

    public static final String SUBJECTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + SUBJECTS_PATH;

    public static final String SUBJECTS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + SUBJECTS_PATH;

    public static final String PLAN_ITEMS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PLAN_ITEMS_PATH;

    public static final String PLAN_ITEMS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PLAN_ITEMS_PATH;

    public static final int URI_TEACHERS = 1;
    public static final int URI_TEACHERS_ID = 2;

    public static final int URI_SUBJECTS = 3;
    public static final int URI_SUBJECTS_ID = 4;

    public static final int URI_PLAN_ITEMS = 5;
    public static final int URI_PLAN_ITEM_ID = 6;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TEACHERS_PATH, URI_TEACHERS);
        uriMatcher.addURI(AUTHORITY, TEACHERS_PATH + "/#", URI_TEACHERS_ID);
        uriMatcher.addURI(AUTHORITY, SUBJECTS_PATH, URI_SUBJECTS);
        uriMatcher.addURI(AUTHORITY, SUBJECTS_PATH + "/#", URI_SUBJECTS_ID);
        uriMatcher.addURI(AUTHORITY, PLAN_ITEMS_PATH, URI_PLAN_ITEMS);
        uriMatcher.addURI(AUTHORITY, PLAN_ITEMS_PATH + "/#", URI_PLAN_ITEM_ID);
    }

    private PlanSQLiteHepler dbHelper;
    private SQLiteDatabase db;
    private ContentHelper teachersContentHelper, subjectsContentHelper, planItemsContentHelper;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new PlanSQLiteHepler(getContext());

        // хелперы для обработки запросов
        teachersContentHelper = new ContentHelper(DB.TABLE_TEACHERS, TEACHERS_CONTENT_URI);
        subjectsContentHelper = new ContentHelper(DB.TABLE_SUBJECTS, SUBJECTS_CONTENT_URI);
        planItemsContentHelper = new ContentHelper(DB.TABLE_PLAN_ITEMS, PLAN_ITEMS_CONTENT_URI);

        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_TEACHERS:
            case URI_SUBJECTS:
            case URI_PLAN_ITEMS:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DB.COLUMN_ID + " ASC";
                break;

            case URI_SUBJECTS_ID:
            case URI_PLAN_ITEM_ID:
            case URI_TEACHERS_ID:
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
            case URI_TEACHERS:
            case URI_TEACHERS_ID:
                return teachersContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_SUBJECTS:
            case URI_SUBJECTS_ID:
                return subjectsContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_PLAN_ITEMS:
            case URI_PLAN_ITEM_ID:{
                final String items = "select * " +
                        "from plan_items " +
                        " join subjects " +
                        "on subjects._id = plan_items.subject_id " +
                        " join teachers " +
                        "on teachers._id = plan_items.teacher_id ";

                return db.rawQuery(items,null );
            }

        }

        return null;
    }


    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch){
            case URI_TEACHERS:
            case URI_PLAN_ITEMS:
            case URI_SUBJECTS:
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch){
            case URI_TEACHERS:
                return teachersContentHelper.insert(db,resolver,values);
            case URI_SUBJECTS:
                return subjectsContentHelper.insert(db,resolver,values);
            case URI_PLAN_ITEMS:
                return planItemsContentHelper.insert(db,resolver,values);
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_TEACHERS:
            case URI_SUBJECTS:
            case URI_PLAN_ITEMS:
                break;

            case URI_SUBJECTS_ID:
            case URI_PLAN_ITEM_ID:
            case URI_TEACHERS_ID:
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
            case URI_TEACHERS:
            case URI_TEACHERS_ID:
                return teachersContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_SUBJECTS:
            case URI_SUBJECTS_ID:
                return subjectsContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_PLAN_ITEMS:
            case URI_PLAN_ITEM_ID:
                return planItemsContentHelper.delete(db,resolver,uri,selection,selectionArgs);
        }

        return -1;

    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_TEACHERS:
            case URI_SUBJECTS:
            case URI_PLAN_ITEMS:
                break;

            case URI_SUBJECTS_ID:
            case URI_PLAN_ITEM_ID:
            case URI_TEACHERS_ID:
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
            case URI_TEACHERS:
            case URI_TEACHERS_ID:
                return teachersContentHelper.update(db, resolver, uri,values, selection, selectionArgs);
            case URI_SUBJECTS:
            case URI_SUBJECTS_ID:
                return subjectsContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            case URI_PLAN_ITEMS:
            case URI_PLAN_ITEM_ID:
                return planItemsContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TEACHERS:
                return TEACHERS_CONTENT_TYPE;
            case URI_TEACHERS_ID:
                return TEACHERS_CONTENT_ITEM_TYPE;
            case URI_PLAN_ITEMS:
                return PLAN_ITEMS_CONTENT_TYPE;
            case URI_PLAN_ITEM_ID:
                return PLAN_ITEMS_CONTENT_ITEM_TYPE;
            case URI_SUBJECTS:
                return SUBJECTS_CONTENT_TYPE;
            case URI_SUBJECTS_ID:
                return SUBJECTS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

}

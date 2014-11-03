package kalashnikov_10po2.lab45.data;

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

import kalashnikov_10po2.lab45.data.models.ContentHelper;


/**
 * Created by admin on 17.09.2014.
 */

// контент провайдер предоставляет операции query, insert, update, delete
public class AutoServiceContentProvider extends ContentProvider {

    // необходимые константы

    private static final String LOG_TAG = "CONTENT_PROVIDER";

    private static final String AUTHORITY = "kalashnikov_10po2.contentprovider";

    private static final String EMPLOYEES_PATH = DB.TABLE_EMPLOYEES;
    private static final String CARS_PATH = DB.TABLE_CARS;
    private static final String TASKS_PATH = DB.TABLE_TASKS;

    public static final Uri EMPLOYEES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + EMPLOYEES_PATH);

    public static final Uri CARS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CARS_PATH);

    public static final Uri TASKS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TASKS_PATH);

    public static final String EMPLOYEES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + EMPLOYEES_PATH;

    public static final String EMPLOYEES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + EMPLOYEES_PATH;

    public static final String CARS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CARS_PATH;

    public static final String CARS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CARS_PATH;

    public static final String TASKS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + TASKS_PATH;

    public static final String TASKS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + TASKS_PATH;

    public static final int URI_EMPLOYEES = 1;
    public static final int URI_EMPLOYEE_ID = 2;

    public static final int URI_CARS = 3;
    public static final int URI_CAR_ID = 4;

    public static final int URI_TASKS = 5;
    public static final int URI_TASK_ID = 6;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, EMPLOYEES_PATH, URI_EMPLOYEES);
        uriMatcher.addURI(AUTHORITY, EMPLOYEES_PATH + "/#", URI_EMPLOYEE_ID);
        uriMatcher.addURI(AUTHORITY, CARS_PATH, URI_CARS);
        uriMatcher.addURI(AUTHORITY, CARS_PATH + "/#", URI_CAR_ID);
        uriMatcher.addURI(AUTHORITY, TASKS_PATH, URI_TASKS);
        uriMatcher.addURI(AUTHORITY, TASKS_PATH + "/#", URI_TASK_ID);
    }

    private AutoServiceSQLiteHepler dbHelper;
    private SQLiteDatabase db;
    private ContentHelper employeeContentHelper, carsContentHelper, tasksContentHelper;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new AutoServiceSQLiteHepler(getContext());

        // хелперы для обработки запросов
        employeeContentHelper = new ContentHelper(DB.TABLE_EMPLOYEES,EMPLOYEES_CONTENT_URI );
        carsContentHelper = new ContentHelper(DB.TABLE_CARS,CARS_CONTENT_URI );
        tasksContentHelper = new ContentHelper(DB.TABLE_TASKS,TASKS_CONTENT_URI );

        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_EMPLOYEES:
            case URI_CARS:
            case URI_TASKS:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DB.COLUMN_ID + " ASC";
                break;

            case URI_CAR_ID:
            case URI_TASK_ID:
            case URI_EMPLOYEE_ID:
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
            case URI_EMPLOYEES:
            case URI_EMPLOYEE_ID:
                return employeeContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_CARS:
            case URI_CAR_ID:
                return carsContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_TASKS:
            case URI_TASK_ID:{
                final String tasks = "select " +
                        "* " +
                        "from tasks " +
                            "join cars " +
                                "on cars._id = tasks.car_id " +
                            "join employees " +
                                "on employees._id = tasks.employee_id " ;

                return db.rawQuery( tasks,null );
            }

        }

        return null;
    }


    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch){
            case URI_EMPLOYEES:
            case URI_TASKS:
            case URI_CARS:
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch){
            case URI_EMPLOYEES:
                return employeeContentHelper.insert(db,resolver,values);
            case URI_CARS:
                return carsContentHelper.insert(db,resolver,values);
            case URI_TASKS:
                return tasksContentHelper.insert(db,resolver,values);
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_EMPLOYEES:
            case URI_CARS:
            case URI_TASKS:
                break;

            case URI_CAR_ID:
            case URI_TASK_ID:
            case URI_EMPLOYEE_ID:
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
            case URI_EMPLOYEES:
            case URI_EMPLOYEE_ID:
                return employeeContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_CARS:
            case URI_CAR_ID:
                return carsContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_TASKS:
            case URI_TASK_ID:
                return tasksContentHelper.delete(db,resolver,uri,selection,selectionArgs);
        }

        return -1;

    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_EMPLOYEES:
            case URI_CARS:
            case URI_TASKS:
                break;

            case URI_CAR_ID:
            case URI_TASK_ID:
            case URI_EMPLOYEE_ID:
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
            case URI_EMPLOYEES:
            case URI_EMPLOYEE_ID:
                return employeeContentHelper.update(db, resolver, uri,values, selection, selectionArgs);
            case URI_CARS:
            case URI_CAR_ID:
                return carsContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            case URI_TASKS:
            case URI_TASK_ID:
                return tasksContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_EMPLOYEES:
                return EMPLOYEES_CONTENT_TYPE;
            case URI_EMPLOYEE_ID:
                return EMPLOYEES_CONTENT_ITEM_TYPE;
            case URI_TASKS:
                return TASKS_CONTENT_TYPE;
            case URI_TASK_ID:
                return TASKS_CONTENT_ITEM_TYPE;
            case URI_CARS:
                return CARS_CONTENT_TYPE;
            case URI_CAR_ID:
                return CARS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

}

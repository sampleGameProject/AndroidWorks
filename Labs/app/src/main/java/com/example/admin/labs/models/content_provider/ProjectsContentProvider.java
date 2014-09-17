package com.example.admin.labs.models.content_provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.admin.labs.models.sql.ProjectConstants;
import com.example.admin.labs.models.sql.ProjectsBaseSQLiteHelper;

/**
 * Created by admin on 17.09.2014.
 */
public class ProjectsContentProvider extends ContentProvider {

    private static final String LOG_TAG = "CONTENT_PROVIDER";

    private static final String AUTHORITY = "com.example.admin.labs.ProjectsProvider";

    private static final String PROJECTS_PATH = "projects";
    private static final String TASKS_PATH = "projects";
    private static final String TASK_TYPES_PATH = "projects";

    public static final Uri PROJECTS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PROJECTS_PATH);

    public static final Uri TASKS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TASKS_PATH);

    public static final Uri TASK_TYPES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TASK_TYPES_PATH);

    public static final String PROJECTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PROJECTS_PATH;

    public static final String PROJECTS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PROJECTS_PATH;

    public static final String TASKS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + TASKS_PATH;

    public static final String TASKS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + TASKS_PATH;

    public static final String TASK_TYPES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + TASK_TYPES_PATH;

    public static final String TASK_TYPES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + TASK_TYPES_PATH;

    public static final int URI_PROJECTS = 1;
    public static final int URI_PROJECTS_ID = 2;

    public static final int URI_TASKS= 3;
    public static final int URI_TASKS_ID = 4;

    public static final int URI_TASK_TYPES = 5;
    public static final int URI_TASK_TYPES_ID = 6;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH, URI_PROJECTS);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH + "/#", URI_PROJECTS_ID);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH, URI_TASKS);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH + "/#", URI_TASKS_ID);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH, URI_TASK_TYPES);
        uriMatcher.addURI(AUTHORITY, PROJECTS_PATH + "/#", URI_TASK_TYPES_ID);
    }

    private ProjectsBaseSQLiteHelper dbHelper;
    private SQLiteDatabase db;
    private ContentHelper projectsContentHelper,tasksContentHelper,taskTypesContentHelper;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new ProjectsBaseSQLiteHelper(getContext());

        projectsContentHelper = new ContentHelper() {
            @Override
            protected String getTable() {
                return ProjectConstants.TABLE_PROJECTS;
            }

            @Override
            protected Uri getContentUri() {
                return PROJECTS_CONTENT_URI;
            }
        };

        tasksContentHelper = new ContentHelper() {
            @Override
            protected String getTable() {
                return ProjectConstants.TABLE_TASKS;
            }

            @Override
            protected Uri getContentUri() {
                return TASKS_CONTENT_URI;
            }
        };

        taskTypesContentHelper = new ContentHelper() {
            @Override
            protected String getTable() {
                return ProjectConstants.TABLE_TASK_TYPES;
            }

            @Override
            protected Uri getContentUri() {
                return TASK_TYPES_CONTENT_URI;
            }
        };
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_TASKS:
            case URI_TASK_TYPES:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = ProjectConstants.COLUMN_ID + " ASC";
                break;

            case URI_TASKS_ID:
            case URI_TASK_TYPES_ID:
            case URI_PROJECTS_ID:
                {
                    String id = uri.getLastPathSegment();

                    if (TextUtils.isEmpty(selection)) {
                        selection = ProjectConstants.COLUMN_ID  + " = " + id;
                    } else {
                        selection = selection + " AND " + ProjectConstants.COLUMN_ID + " = " + id;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_PROJECTS_ID:
                return projectsContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_TASKS:
            case URI_TASKS_ID:
                return tasksContentHelper.query(db,resolver,projection,selection,selectionArgs,sortOrder);
            case URI_TASK_TYPES:
            case URI_TASK_TYPES_ID:
                return taskTypesContentHelper.query(db, resolver,projection,selection,selectionArgs,sortOrder);
        }

        return null;
    }


    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch){
            case URI_PROJECTS_ID:
            case URI_TASK_TYPES_ID:
            case URI_TASKS_ID:
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch){
            case URI_PROJECTS_ID:
                return projectsContentHelper.insert(db,resolver,values);
            case URI_TASKS_ID:
                return tasksContentHelper.insert(db,resolver,values);
            case URI_TASK_TYPES_ID:
                return taskTypesContentHelper.insert(db,resolver,values);
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_TASKS:
            case URI_TASK_TYPES:
                break;

            case URI_TASKS_ID:
            case URI_TASK_TYPES_ID:
            case URI_PROJECTS_ID:
                {
                    String id = uri.getLastPathSegment();

                    if (TextUtils.isEmpty(selection)) {
                        selection = ProjectConstants.COLUMN_ID  + " = " + id;
                    } else {
                        selection = selection + " AND " + ProjectConstants.COLUMN_ID + " = " + id;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_PROJECTS_ID:
                return projectsContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_TASKS:
            case URI_TASKS_ID:
                return tasksContentHelper.delete(db,resolver,uri,selection,selectionArgs);
            case URI_TASK_TYPES:
            case URI_TASK_TYPES_ID:
                return taskTypesContentHelper.delete(db,resolver,uri,selection,selectionArgs);
        }

        return -1;

    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriMatch = uriMatcher.match(uri);

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_TASKS:
            case URI_TASK_TYPES:
                break;

            case URI_TASKS_ID:
            case URI_TASK_TYPES_ID:
            case URI_PROJECTS_ID:
            {
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = ProjectConstants.COLUMN_ID  + " = " + id;
                } else {
                    selection = selection + " AND " + ProjectConstants.COLUMN_ID + " = " + id;
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();

        switch (uriMatch) {
            case URI_PROJECTS:
            case URI_PROJECTS_ID:
                return projectsContentHelper.update(db, resolver, uri,values, selection, selectionArgs);
            case URI_TASKS:
            case URI_TASKS_ID:
                return tasksContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
            case URI_TASK_TYPES:
            case URI_TASK_TYPES_ID:
                return taskTypesContentHelper.update(db, resolver, uri, values, selection, selectionArgs);
        }

        return -1;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_PROJECTS:
                return PROJECTS_CONTENT_TYPE;
            case URI_PROJECTS_ID:
                return PROJECTS_CONTENT_ITEM_TYPE;
            case URI_TASK_TYPES:
                return TASK_TYPES_CONTENT_TYPE;
            case URI_TASK_TYPES_ID:
                return TASK_TYPES_CONTENT_ITEM_TYPE;
            case URI_TASKS:
                return TASKS_CONTENT_TYPE;
            case URI_TASKS_ID:
                return TASKS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

 }

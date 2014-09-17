package com.example.admin.labs.models.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 13.09.2014.
 */
public class ProjectsBaseSQLiteHelper extends SQLiteOpenHelper {

    public ProjectsBaseSQLiteHelper(Context context) {
        super(context,ProjectConstants.DATABASE_NAME, null, ProjectConstants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ProjectConstants.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ProjectsBaseSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ProjectConstants.TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectConstants.TABLE_PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectConstants.TABLE_TASK_TYPES);
        onCreate(db);
    }
}

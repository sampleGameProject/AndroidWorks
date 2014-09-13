package com.example.admin.labs.models.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.admin.labs.models.sql.data_models.Project;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 13.09.2014.
 */
public class ProjectsDataSource {
    // Database fields
    protected static  SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private SQLiteDatabase database;
    private ProjectsBaseSQLiteHelper dbHelper;
    private String[] allProjectColumns = {
            ProjectsBaseSQLiteHelper.COLUMN_ID,
            ProjectsBaseSQLiteHelper.COLUMN_NAME,
            ProjectsBaseSQLiteHelper.COLUMN_ABOUT,
            ProjectsBaseSQLiteHelper.COLUMN_START,
            ProjectsBaseSQLiteHelper.COLUMN_FINISH,
            ProjectsBaseSQLiteHelper.COLUMN_DEADLINE,
    };

    public ProjectsDataSource(Context context) {
        dbHelper = new ProjectsBaseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Project createProject(Project project){
        ContentValues values = new ContentValues();
        values.put(ProjectsBaseSQLiteHelper.COLUMN_NAME, project.getName());
        values.put(ProjectsBaseSQLiteHelper.COLUMN_START, dateFormat.format(project.getStart()));
        values.put(ProjectsBaseSQLiteHelper.COLUMN_FINISH,  dateFormat.format(project.getStart()));
        values.put(ProjectsBaseSQLiteHelper.COLUMN_DEADLINE,  dateFormat.format(project.getStart()));
        values.put(ProjectsBaseSQLiteHelper.COLUMN_ABOUT, project.getAbout());

        long insertId = database.insert(ProjectsBaseSQLiteHelper.TABLE_PROJECTS, null,
                values);
        Cursor cursor = database.query(ProjectsBaseSQLiteHelper.TABLE_PROJECTS,
                allProjectColumns, ProjectsBaseSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Project newProject = null;
        try {
            newProject = cursorToProject(cursor);
        } catch (ParseException e) {
            Log.i("MY_TAG",e.toString());
        }
        cursor.close();
        return newProject;
    }



    public void deleteComment(Project project) {
        long id = project.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(ProjectsBaseSQLiteHelper.TABLE_PROJECTS, ProjectsBaseSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Project> getAllProjects() throws ParseException {
        List<Project> projects = new ArrayList<Project>();

        Cursor cursor = database.query(ProjectsBaseSQLiteHelper.TABLE_PROJECTS,
                allProjectColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Project project = cursorToProject(cursor);
            projects.add(project);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return projects;
    }

    private Project cursorToProject(Cursor cursor) throws ParseException{
        Project project = new Project();
        project.setId(cursor.getLong(0));
        project.setName(cursor.getString(1));
        project.setAbout(cursor.getString(2));
        project.setStart(dateFormat.parse(cursor.getString(3)));
        project.setFinish(dateFormat.parse(cursor.getString(4)));
        project.setDeadline(dateFormat.parse(cursor.getString(5)));
        return project;
    }

}

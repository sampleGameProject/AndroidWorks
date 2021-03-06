package com.example.admin.labs.models.data.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.admin.labs.models.helpers.DateHelper;
import com.example.admin.labs.models.data.sql.data_models.Project;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 13.09.2014.
 */
public class ProjectsDataSource {
    // Database fields


    private SQLiteDatabase database;
    private ProjectsBaseSQLiteHelper dbHelper;
    private String[] allProjectColumns = {
            ProjectConstants.COLUMN_ID,
            ProjectConstants.COLUMN_NAME,
            ProjectConstants.COLUMN_ABOUT,
            ProjectConstants.COLUMN_START,
            ProjectConstants.COLUMN_FINISH,
            ProjectConstants.COLUMN_DEADLINE,
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
        ContentValues values = projectToValues(project);

        long insertId = database.insert(ProjectConstants.TABLE_PROJECTS, null,
                values);
        Cursor cursor = database.query(ProjectConstants.TABLE_PROJECTS,
                allProjectColumns, ProjectConstants.COLUMN_ID + " = " + insertId, null,
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



    public void deleteProject(Project project) {
        long id = project.getId();
        System.out.println("Project deleted with id: " + id);
        database.delete(ProjectConstants.TABLE_PROJECTS, ProjectConstants.COLUMN_ID
                + " = " + id, null);
    }

    public List<Project> getAllProjects() throws ParseException {
        List<Project> projects = new ArrayList<Project>();

        Cursor cursor = database.query(ProjectConstants.TABLE_PROJECTS,
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
        project.setStart(DateHelper.stringToDate(cursor.getString(3)));
        project.setFinish(DateHelper.stringToDate(cursor.getString(4)));
        project.setDeadline(DateHelper.stringToDate(cursor.getString(5)));
        return project;
    }

    public void update(Project project) {
        long id = project.getId();

        database.update(ProjectConstants.TABLE_PROJECTS,projectToValues(project), ProjectConstants.COLUMN_ID
                + " = ?", new String[] {Long.toString(id) });

    }

    private ContentValues projectToValues(Project project){
        ContentValues values = new ContentValues();
        values.put(ProjectConstants.COLUMN_NAME, project.getName());
        values.put(ProjectConstants.COLUMN_START, DateHelper.dateToString(project.getStart()));
        values.put(ProjectConstants.COLUMN_FINISH, DateHelper.dateToString(project.getFinish()));
        values.put(ProjectConstants.COLUMN_DEADLINE, DateHelper.dateToString(project.getDeadline()));
        values.put(ProjectConstants.COLUMN_ABOUT, project.getAbout());
        return values;
    }
}

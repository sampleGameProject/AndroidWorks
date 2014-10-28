package kalashnikov_po2.lab34.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import kalashnikov_po2.lab34.data.models.Client;

/**
 * Created by admin on 25.10.2014.
 */
public class AutoserviceDataSource {

    private SQLiteDatabase database;
    private AutoserviceSQLiteHepler dbHelper;

    private String[] clientColumns = {
            DB.COLUMN_ID,
            DB.COLUMN_FIRSTNAME,
            DB.COLUMN_LASTNAME
    };

    private String[] carColumns = {
            DB.COLUMN_ID,
            DB.COLUMN_CLIENT_ID,
            DB.COLUMN_MODEL,
            DB.COLUMN_YEAR
    };

    private String[] orderColumns = {
            DB.COLUMN_ID,
            DB.COLUMN_TASK,
            DB.COLUMN_PRICE,
            DB.COLUMN_CAR_ID
    };
    public AutoserviceDataSource(Context context) {
        dbHelper = new AutoserviceSQLiteHepler(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Client createClient(Client client){
        ContentValues values = client.getConventValues();

        long insertId = database.insert(DB.TABLE_CLIENTS, null,values);
        client.setId(insertId);
        return client;
    }

    public void deleteProject(Client client) {
        long id = client.getId();
        System.out.println("Project deleted with id: " + id);
        database.delete(DB.TABLE_CLIENTS, DB.COLUMN_ID
                + " = " + id, null);
    }

    public void update(Client client) {

        database.update(DB.TABLE_CLIENTS,client.getConventValues(), DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(client.getId()) });

    }

//    public Project createProject(Project project){
//        ContentValues values = projectToValues(project);
//
//        long insertId = database.insert(ProjectConstants.TABLE_PROJECTS, null,
//                values);
//        Cursor cursor = database.query(ProjectConstants.TABLE_PROJECTS,
//                allProjectColumns, ProjectConstants.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        Project newProject = null;
//        try {
//            newProject = cursorToProject(cursor);
//        } catch (ParseException e) {
//            Log.i("MY_TAG", e.toString());
//        }
//        cursor.close();
//        return newProject;
//    }
//
//
//
//    public void deleteProject(Project project) {
//        long id = project.getId();
//        System.out.println("Project deleted with id: " + id);
//        database.delete(ProjectConstants.TABLE_PROJECTS, ProjectConstants.COLUMN_ID
//                + " = " + id, null);
//    }
//
//    public List<Project> getAllProjects() throws ParseException {
//        List<Project> projects = new ArrayList<Project>();
//
//        Cursor cursor = database.query(ProjectConstants.TABLE_PROJECTS,
//                allProjectColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Project project = cursorToProject(cursor);
//            projects.add(project);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
//        return projects;
//    }
//
//    private Project cursorToProject(Cursor cursor) throws ParseException{
//        Project project = new Project();
//        project.setId(cursor.getLong(0));
//        project.setName(cursor.getString(1));
//        project.setAbout(cursor.getString(2));
//        project.setStart(DateHelper.stringToDate(cursor.getString(3)));
//        project.setFinish(DateHelper.stringToDate(cursor.getString(4)));
//        project.setDeadline(DateHelper.stringToDate(cursor.getString(5)));
//        return project;
//    }
//
//    public void update(Project project) {
//        long id = project.getId();
//
//        database.update(ProjectConstants.TABLE_PROJECTS,projectToValues(project), ProjectConstants.COLUMN_ID
//                + " = ?", new String[] {Long.toString(id) });
//
//    }
//
//    private ContentValues projectToValues(Project project){
//        ContentValues values = new ContentValues();
//        values.put(ProjectConstants.COLUMN_NAME, project.getName());
//        values.put(ProjectConstants.COLUMN_START, DateHelper.dateToString(project.getStart()));
//        values.put(ProjectConstants.COLUMN_FINISH, DateHelper.dateToString(project.getFinish()));
//        values.put(ProjectConstants.COLUMN_DEADLINE, DateHelper.dateToString(project.getDeadline()));
//        values.put(ProjectConstants.COLUMN_ABOUT, project.getAbout());
//        return values;
//    }
}

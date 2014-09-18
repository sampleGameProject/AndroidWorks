package com.example.admin.labs.models.data;

import android.content.Context;
import android.os.Environment;

import com.example.admin.labs.models.data.sql.ProjectConstants;
import com.example.admin.labs.models.data.sql.ProjectsDataSource;
import com.example.admin.labs.models.data.sql.data_models.Project;
import com.example.admin.labs.models.data.sql.data_models.Task;
import com.example.admin.labs.models.data.sql.data_models.TaskType;
import com.example.admin.labs.models.helpers.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 14.09.2014.
 */
public class ProjectsRepo {

    IRepoListener listener;
    ProjectsDataSource dataSource;

    List<Project> allProjects;

    public ProjectsRepo(Context context){
        dataSource = new ProjectsDataSource(context);
    }

    public void setListener(IRepoListener listener) {
        this.listener = listener;
        notifyListener();
    }

    public void open(){
        dataSource.open();
        updateProjectsList();
    }

    public void close(){
        dataSource.close();
    }

    public void update(Project project){
        dataSource.update(project);
        updateProjectsList();
    }

    private void updateProjectsList() {
        try {
            allProjects = dataSource.getAllProjects();
        } catch (ParseException e) {
            allProjects = new ArrayList<Project>();
        }
        notifyListener();
    }

    private void notifyListener(){
        if (listener != null)
            listener.onRepoChanged();
    }

    public void delete(Project project){
        dataSource.deleteProject(project);
        updateProjectsList();
    }

    public void add(Project project){
        dataSource.createProject(project);
        updateProjectsList();
    }

    public ArrayList<HashMap<String, String>> getProjectsArrayList(){

        ArrayList<HashMap<String, String>> projectsMapList = new ArrayList<HashMap<String, String>>();

        for(Project p : allProjects){
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            map.put("Id",Long.toString(p.getId()));
            map.put("Name", p.getName());
            map.put("About", p.getAbout());
            projectsMapList.add(map);
        }

        return projectsMapList;
    }

    public Project getByIndex(int index){
        return allProjects.get(index);
    }

    public void importData(String filePath){

        String fileData = readFile(filePath);

        if(fileData == null)
            return;

        TupleJSON tuple = null;
        try {
            tuple = new TupleJSON(fileData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        List<Project> importedProjects = new ArrayList<Project>();
        List<TaskType> importedTaskTypes = new ArrayList<TaskType>();
        List<Task> importedTasks = new ArrayList<Task>();

        boolean hasErrors = false;

        for (int i = 0; i < tuple.getProjects().length(); i++) {
            JSONObject projectJSON = null;
            try {
                projectJSON = tuple.getProjects().getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                hasErrors = true;
                continue;
            }

            Project project = JSONHelper.projectFromJSON(projectJSON);
            if(project != null)
                importedProjects.add(project);
            else
                hasErrors = true;
        }

        for (int i = 0; i < tuple.getTaskTypes().length(); i++) {
            JSONObject json = null;
            try {
                json = tuple.getTaskTypes().getJSONObject(i);
            } catch (JSONException e) {
                hasErrors = true;
                continue;
            }

            TaskType taskType = JSONHelper.taskTypeFromJSON(json);
            if(taskType != null)
                importedTaskTypes.add(taskType);
            else
                hasErrors = true;
        }

        for (int i = 0; i < tuple.getTasks().length(); i++) {
            JSONObject taskJSON = null;
            try {
                taskJSON = tuple.getTasks().getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                hasErrors = true;
                continue;
            }

            Task task = JSONHelper.taskFromJSON(taskJSON);
            if(task != null)
                importedTasks.add(task);
            else
                hasErrors = true;
        }

    }


    private String readFile(String filePath){

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,filePath);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            return null;
        }
        return text.toString();
    }

    private class TupleJSON {
        public JSONArray getProjects() {
            return projects;
        }

        JSONArray projects;
        JSONArray tasks;

        public JSONArray getTaskTypes() {
            return taskTypes;
        }

        public JSONArray getTasks() {
            return tasks;
        }

        JSONArray taskTypes;

        public TupleJSON(String importText) throws JSONException {
            JSONObject data = new JSONObject(importText);

            projects = data.getJSONArray(ProjectConstants.TABLE_PROJECTS);
            taskTypes = data.getJSONArray(ProjectConstants.TABLE_TASK_TYPES);
            tasks = data.getJSONArray(ProjectConstants.TABLE_TASKS);
        }
    }


    public void exportData(){

    }
}

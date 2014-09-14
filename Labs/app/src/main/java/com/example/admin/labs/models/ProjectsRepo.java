package com.example.admin.labs.models;

import android.content.Context;

import com.example.admin.labs.models.sql.ProjectsDataSource;
import com.example.admin.labs.models.sql.data_models.Project;

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
}

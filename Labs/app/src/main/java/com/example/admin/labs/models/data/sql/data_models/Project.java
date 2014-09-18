package com.example.admin.labs.models.data.sql.data_models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 13.09.2014.
 */
public class Project extends BaseDataModel{

    protected Date start;
    protected Date deadline;
    protected Date finish;

    protected List<Task> tasks = new ArrayList<Task>();

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    protected String about;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public List<Task> getTasks(){
        return tasks;
    }
}

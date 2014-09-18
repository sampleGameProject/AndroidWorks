package com.example.admin.labs.models.helpers;

import com.example.admin.labs.models.data.sql.ProjectConstants;
import com.example.admin.labs.models.data.sql.data_models.BaseDataModel;
import com.example.admin.labs.models.data.sql.data_models.Project;
import com.example.admin.labs.models.data.sql.data_models.Task;
import com.example.admin.labs.models.data.sql.data_models.TaskType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 18.09.2014.
 */
public class JSONHelper {

    public static JSONObject toJSON(Project project){

        try {
            JSONObject json = baseToJSON(project);
            json.put(ProjectConstants.COLUMN_ABOUT,     project.getAbout());
            json.put(ProjectConstants.COLUMN_START,     DateHelper.dateToString(project.getStart()));
            json.put(ProjectConstants.COLUMN_DEADLINE,  DateHelper.dateToString(project.getDeadline()));
            json.put(ProjectConstants.COLUMN_FINISH,    DateHelper.dateToString(project.getFinish()));
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject toJSON(Task task){

        try {
            JSONObject json = baseToJSON(task);
            json.put(ProjectConstants.COLUMN_COMMENT,       task.getComment());
            json.put(ProjectConstants.COLUMN_PRIORITY,      task.getPriority());
            json.put(ProjectConstants.COLUMN_PROJECT_ID,    task.getProjectId());
            json.put(ProjectConstants.COLUMN_TASK_TYPE_ID,  task.getTaskTypeId());
            json.put(ProjectConstants.COLUMN_COMPLETED,     task.isCompleted());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject toJSON(TaskType taskType){

        try {
            JSONObject json = baseToJSON(taskType);
            json.put(ProjectConstants.COLUMN_COLOR_R,   taskType.getColorR());
            json.put(ProjectConstants.COLUMN_COLOR_G,   taskType.getColorG());
            json.put(ProjectConstants.COLUMN_COLOR_B,   taskType.getColorB());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject baseToJSON(BaseDataModel model) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("Id",model.getId());
        json.put(ProjectConstants.COLUMN_NAME,model.getName());
        return json;
    }

    public static Project projectFromJSON(JSONObject json){

        try {
            Project project = new Project();
            project.setId(json.getLong("Id"));
            project.setName(json.getString(ProjectConstants.COLUMN_NAME));
            project.setAbout(json.getString(ProjectConstants.COLUMN_ABOUT));
            project.setStart(DateHelper.stringToDate(json.getString(ProjectConstants.COLUMN_START)));
            project.setDeadline(DateHelper.stringToDate(json.getString(ProjectConstants.COLUMN_DEADLINE)));
            project.setFinish(DateHelper.stringToDate(json.getString(ProjectConstants.COLUMN_FINISH)));
            return project;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Task taskFromJSON(JSONObject json){

        try {
            Task task = new Task();
            task.setId(json.getLong("Id"));
            task.setName(json.getString(ProjectConstants.COLUMN_NAME));
            task.setComment(json.getString(ProjectConstants.COLUMN_ABOUT));
            task.setPriority(json.getInt(ProjectConstants.COLUMN_PRIORITY));
            task.setCompleted(json.getBoolean(ProjectConstants.COLUMN_COMPLETED));
            task.setProjectId(json.getInt(ProjectConstants.COLUMN_PROJECT_ID));
            task.setTaskTypeId(json.getInt(ProjectConstants.COLUMN_TASK_TYPE_ID));
            return task;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TaskType taskTypeFromJSON(JSONObject json){
        try {
            TaskType taskType = new TaskType();
            taskType.setId(json.getLong("Id"));
            taskType.setName(json.getString(ProjectConstants.COLUMN_NAME));
            taskType.setColorR(json.getInt(ProjectConstants.COLUMN_COLOR_R));
            taskType.setColorG(json.getInt(ProjectConstants.COLUMN_COLOR_G));
            taskType.setColorB(json.getInt(ProjectConstants.COLUMN_COLOR_B));
            return taskType;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

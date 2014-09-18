package com.example.admin.labs.models.data.sql;

/**
 * Created by admin on 17.09.2014.
 */
public class ProjectConstants {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    //projects:
    public static final String TABLE_PROJECTS = "projects";
    public static final String COLUMN_ABOUT = "about";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_FINISH = "finish";

    //tasks:
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_TASK_TYPE_ID = "task_type_id";

    //task types:
    public static final String TABLE_TASK_TYPES = "task_types";
    public static final String COLUMN_COLOR_R = "color_r";
    public static final String COLUMN_COLOR_G = "color_g";
    public static final String COLUMN_COLOR_B = "color_b";

    public static final String DATABASE_NAME = "projects.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table "
            + TABLE_PROJECTS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_ABOUT + " text, " +
            COLUMN_START + " date not null, " +
            COLUMN_DEADLINE + " date not null, " +
            COLUMN_FINISH + " date not null " +
            ");"
            + TABLE_TASK_TYPES + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_COLOR_R + " integer, " +
            COLUMN_COLOR_G + " integer, " +
            COLUMN_COLOR_B + " integer " +
            ");"
            + TABLE_TASKS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_PRIORITY + " integer, " +
            COLUMN_COMPLETED + " integer, " +
            COLUMN_COMMENT + " text not null, " +
            COLUMN_TASK_TYPE_ID + " integer references " + TABLE_TASK_TYPES + "(" + COLUMN_ID + "), " +
            COLUMN_PROJECT_ID + " integer references " + TABLE_PROJECTS + "(" + COLUMN_ID + ") " +
            ");";
}

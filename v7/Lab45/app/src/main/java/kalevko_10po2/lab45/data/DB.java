package kalevko_10po2.lab45.data;

/**
 * Created by admin on 25.10.2014.
 */
public class DB {
    public static final String COLUMN_ID = "_id";

    public static final String TABLE_TEACHERS = "teachers";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";

    public static final String TABLE_SUBJECTS = "subjects";
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_IS_LECTURE = "is_lecture";

    public static final String TABLE_PLAN_ITEMS = "plan_items";
    public static final String COLUMN_TEACHER_ID = "teacher_id";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DAY = "day";

    public static final String DATABASE_NAME = "plan.db";
    public static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    public static final String TABLE_SUBJECTS_CREATE = " create table "
        + TABLE_SUBJECTS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_SUBJECT_NAME + " text not null, " +
            COLUMN_IS_LECTURE + " integer not null " +
        ");" ;

    public static final String TABLE_TEACHERS_CREATE = "create table "
        + TABLE_TEACHERS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_PHONE + " text not null " +
        ");";

    public static final String TABLE_PLAN_CREATE = " create table "
        + TABLE_PLAN_ITEMS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_TIME + " datetime not null, " +
        COLUMN_DAY + " integer not null, " +
            COLUMN_TEACHER_ID + " integer references " + TABLE_TEACHERS + "(" + COLUMN_ID + "), " +
            COLUMN_SUBJECT_ID + " integer references " + TABLE_SUBJECTS + "(" + COLUMN_ID + ") " +
        ");";


}

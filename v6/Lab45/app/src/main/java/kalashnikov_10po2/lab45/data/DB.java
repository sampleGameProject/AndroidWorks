package kalashnikov_10po2.lab45.data;

/**
 * Created by admin on 25.10.2014.
 */
public class DB {
    public static final String COLUMN_ID = "_id";

    public static final String TABLE_EMPLOYEES = "employees";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POST = "post";

    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_CAR_NAME = "car_name";

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_CAR_ID = "car_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_IS_COMPLETE = "is_complete";

    public static final String DATABASE_NAME = "auto_service.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    public static final String TABLE_CARS_CREATE = " create table "
        + TABLE_CARS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CAR_NAME + " text not null, " +
            COLUMN_NUMBER + " text not null " +
        ");" ;

    public static final String TABLE_EMPLOYEES_CREATE  = "create table "
        + TABLE_EMPLOYEES + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_NAME + " text not null, " +
            COLUMN_POST + " text not null " +
        ");";

    public static final String TABLE_SEANCES_CREATE = " create table "
        + TABLE_TASKS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TASK + " text not null, " +
            COLUMN_IS_COMPLETE + " integer not null, " +
            COLUMN_EMPLOYEE_ID + " integer references " + TABLE_EMPLOYEES + "(" + COLUMN_ID + "), " +
            COLUMN_CAR_ID + " integer references " + TABLE_CARS + "(" + COLUMN_ID + ") " +
        ");";


}

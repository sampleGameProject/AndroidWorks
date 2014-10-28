package kalashnikov_po2.lab34.data;

/**
 * Created by admin on 25.10.2014.
 */
public class DB {
    public static final String COLUMN_ID = "_id";

    //clients:
    public static final String TABLE_CLIENTS = "clients";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";

    //cars:
    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_CLIENT_ID = "client_id";

    //orders:
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_CAR_ID = "car_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_PRICE = "price";

    public static final String DATABASE_NAME = "service.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table "
        + TABLE_CLIENTS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_FIRSTNAME + " text not null, " +
        COLUMN_LASTNAME + " text not null " +
        ");"
        + TABLE_CARS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_MODEL + " text not null, " +
        COLUMN_YEAR + " integer, " +
        COLUMN_CLIENT_ID + " integer references " + TABLE_CLIENTS + "(" + COLUMN_ID + "), " +
        ");"
        + TABLE_ORDERS + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_TASK + " text not null, " +
        COLUMN_PRICE + " integer, " +
        COLUMN_CAR_ID + " integer references " + TABLE_CARS + "(" + COLUMN_ID + "), " +
        ");";
}

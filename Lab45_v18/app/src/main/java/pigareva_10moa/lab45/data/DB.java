package pigareva_10moa.lab45.data;

/**
 * Created by admin on 25.10.2014.
 */
public class DB {
    public static final String COLUMN_ID = "_id";

    //theatres:
    public static final String TABLE_THEATRES = "theatres";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";

    //movies:
    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_LENGTH = "length";

    //seances:
    public static final String TABLE_SEANCES = "seances";
    public static final String COLUMN_THEATRE_ID = "theater_id";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_IS_3D = "is_3d";

    public static final String DATABASE_NAME = "poster.db";
    public static final int DATABASE_VERSION = 7;

    // Database creation sql statement
    public static final String TABLE_MOVIES_CREATE = " create table "
        + TABLE_MOVIES + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_NAME + " text not null, " +
        COLUMN_LENGTH + " integer not null " +
        ");" ;

    public static final String TABLE_THEATRES_CREATE = "create table "
        + TABLE_THEATRES + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_NAME + " text not null, " +
        COLUMN_ADDRESS + " text not null " +
        ");";

    public static final String TABLE_SEANCES_CREATE = " create table "
        + TABLE_SEANCES + "(" +
        COLUMN_ID + " integer primary key autoincrement, " +
        COLUMN_TIME + " datetime not null, " +
        COLUMN_IS_3D + " integer not null, " +
        COLUMN_THEATRE_ID + " integer references " + TABLE_THEATRES + "(" + COLUMN_ID + "), " +
        COLUMN_MOVIE_ID + " integer references " + TABLE_MOVIES + "(" + COLUMN_ID + ") " +
        ");";


}

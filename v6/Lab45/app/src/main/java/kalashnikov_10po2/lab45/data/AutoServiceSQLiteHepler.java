package kalashnikov_10po2.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 25.10.2014.
 */
public class AutoServiceSQLiteHepler extends SQLiteOpenHelper {

    public AutoServiceSQLiteHepler(Context context) {
        super(context,DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.TABLE_CARS_CREATE);
        db.execSQL(DB.TABLE_EMPLOYEES_CREATE);
        db.execSQL(DB.TABLE_SEANCES_CREATE);

        ContentValues cv;
        cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_NAME, "LADA Калина");
        cv.put(DB.COLUMN_NUMBER, "т120уц");
        db.insert(DB.TABLE_CARS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_NAME, "LADA Калина");
        cv.put(DB.COLUMN_NUMBER, "п342ру");
        db.insert(DB.TABLE_CARS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_NAME, "LADA Приора");
        cv.put(DB.COLUMN_NUMBER, "е333ен");
        db.insert(DB.TABLE_CARS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_NAME, "LADA х5");
        cv.put(DB.COLUMN_NUMBER, "аы140ы");
        db.insert(DB.TABLE_CARS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_NAME, "LADA х6");
        cv.put(DB.COLUMN_NUMBER, "и145ии");
        db.insert(DB.TABLE_CARS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Иванов А.А.");
        cv.put(DB.COLUMN_POST, "Слесарь");
        db.insert(DB.TABLE_EMPLOYEES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Панорама К.Г.");
        cv.put(DB.COLUMN_POST, "Автослеларь");
        db.insert(DB.TABLE_EMPLOYEES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Люксор И.И");
        cv.put(DB.COLUMN_POST, "Мотослесарь");
        db.insert(DB.TABLE_EMPLOYEES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Петров И.И");
        cv.put(DB.COLUMN_POST, "Хирург");
        db.insert(DB.TABLE_EMPLOYEES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Петров А.И.");
        cv.put(DB.COLUMN_POST, "Автослеварь");
        db.insert(DB.TABLE_EMPLOYEES, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AutoServiceSQLiteHepler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_EMPLOYEES);
        onCreate(db);
    }
}

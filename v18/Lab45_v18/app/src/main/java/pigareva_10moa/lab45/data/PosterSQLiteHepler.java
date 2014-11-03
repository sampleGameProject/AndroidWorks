package pigareva_10moa.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 25.10.2014.
 */
public class PosterSQLiteHepler extends SQLiteOpenHelper {

    public PosterSQLiteHepler(Context context) {
        super(context,DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.TABLE_THEATRES_CREATE);
        db.execSQL(DB.TABLE_MOVIES_CREATE);
        db.execSQL(DB.TABLE_SEANCES_CREATE);

        ContentValues cv;
        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сумерки");
        cv.put(DB.COLUMN_LENGTH, "120");
        db.insert(DB.TABLE_MOVIES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сумерки. Сага. Новолуние");
        cv.put(DB.COLUMN_LENGTH, "130");
        db.insert(DB.TABLE_MOVIES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сумерки. Сага. Затмение");
        cv.put(DB.COLUMN_LENGTH, "180");
        db.insert(DB.TABLE_MOVIES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сумерки. Сага. Рассвет: Часть 1");
        cv.put(DB.COLUMN_LENGTH, "140");
        db.insert(DB.TABLE_MOVIES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сумерки. Сага. Рассвет: Часть 2");
        cv.put(DB.COLUMN_LENGTH, "145");
        db.insert(DB.TABLE_MOVIES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Победа");
        cv.put(DB.COLUMN_ADDRESS, "ул. Майской Стачки, д. -1");
        db.insert(DB.TABLE_THEATRES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Панорама");
        cv.put(DB.COLUMN_ADDRESS, "ул. 3-го Интернационала, д. 3");
        db.insert(DB.TABLE_THEATRES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Люксор");
        cv.put(DB.COLUMN_ADDRESS, "ул. Объездная, д. 5");
        db.insert(DB.TABLE_THEATRES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Люксор 2");
        cv.put(DB.COLUMN_ADDRESS, "ул. Объездная, д. 6");
        db.insert(DB.TABLE_THEATRES, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Люксор 3");
        cv.put(DB.COLUMN_ADDRESS, "ул. Объездная, д. 7");
        db.insert(DB.TABLE_THEATRES, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PosterSQLiteHepler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_SEANCES);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_THEATRES);
        onCreate(db);
    }
}

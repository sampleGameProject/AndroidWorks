package kalevko_10po2.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 25.10.2014.
 */
public class PlanSQLiteHepler extends SQLiteOpenHelper {

    public PlanSQLiteHepler(Context context) {
        super(context,DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.TABLE_TEACHERS_CREATE);
        db.execSQL(DB.TABLE_SUBJECTS_CREATE);
        db.execSQL(DB.TABLE_PLAN_CREATE);

        ContentValues cv;
        cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, "ТРПО");
        cv.put(DB.COLUMN_IS_LECTURE, true);
        db.insert(DB.TABLE_SUBJECTS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, "СГИС");
        cv.put(DB.COLUMN_IS_LECTURE, true);
        db.insert(DB.TABLE_SUBJECTS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, "ТРПО");
        cv.put(DB.COLUMN_IS_LECTURE, false);
        db.insert(DB.TABLE_SUBJECTS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, "СГИС");
        cv.put(DB.COLUMN_IS_LECTURE, false);
        db.insert(DB.TABLE_SUBJECTS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Лагерев Д.Г.");
        cv.put(DB.COLUMN_PHONE, "-");
        db.insert(DB.TABLE_TEACHERS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Подвесовский А.Г.");
        cv.put(DB.COLUMN_PHONE, "-");
        db.insert(DB.TABLE_TEACHERS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Сериков А.Г.");
        cv.put(DB.COLUMN_PHONE, "-");
        db.insert(DB.TABLE_TEACHERS, null, cv);

        cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, "Бабурин А.Н.");
        cv.put(DB.COLUMN_PHONE, "-");
        db.insert(DB.TABLE_TEACHERS, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PlanSQLiteHepler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_PLAN_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_TEACHERS);
        onCreate(db);
    }
}

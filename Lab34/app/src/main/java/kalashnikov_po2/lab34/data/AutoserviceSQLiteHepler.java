package kalashnikov_po2.lab34.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 25.10.2014.
 */
public class AutoserviceSQLiteHepler extends SQLiteOpenHelper {

    public AutoserviceSQLiteHepler(Context context) {
        super(context,DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AutoserviceSQLiteHepler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_CLIENTS);
        onCreate(db);
    }
}

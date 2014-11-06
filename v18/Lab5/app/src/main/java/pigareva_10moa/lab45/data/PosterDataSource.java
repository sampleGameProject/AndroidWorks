package pigareva_10moa.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pigareva_10moa.lab45.data.models.Movie;
import pigareva_10moa.lab45.data.models.Seance;
import pigareva_10moa.lab45.data.models.Theater;

/**
 * Created by admin on 25.10.2014.
 */

// класс для работы с БД
public class PosterDataSource {

    private final Context context;

    private PosterSQLiteHepler hepler;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor actors;

    public PosterDataSource(Context ctx) {
        context = ctx;
    }

    // открыть подключение
    public void open() {
        hepler = new PosterSQLiteHepler(context);
        sqLiteDatabase = hepler.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (hepler !=null) hepler.close();
    }

    // получить все данные из таблицы
    public Cursor getMovies() {
        return sqLiteDatabase.query(DB.TABLE_MOVIES, null, null, null, null, null, null);
    }

    public void addMovie(Movie mov) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, mov.getName());
        cv.put(DB.COLUMN_LENGTH,mov.getLength());
        sqLiteDatabase.insert(DB.TABLE_MOVIES, null, cv);
    }

    public void deleteMovieById(long id) {
        sqLiteDatabase.delete(DB.TABLE_MOVIES, DB.COLUMN_ID + " = " + id, null);
    }

    public Cursor getTheatres() {
        return sqLiteDatabase.query(DB.TABLE_THEATRES, null, null, null, null, null, null);
    }

    public Cursor getSeancesForTheater(long theaterId) {

        final String seances = "select " +
                "* " +
                "from movies join seances " +
                "on movies._id = seances.movie_id " +
                "where " +
                "seances.theater_id = ?";

        return sqLiteDatabase.rawQuery( seances,new String[]{Long.toString(theaterId)} );
    }

    public void addTheater(Theater theater) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, theater.getName());
        cv.put(DB.COLUMN_ADDRESS,theater.getAddress());
        sqLiteDatabase.insert(DB.TABLE_THEATRES, null, cv);
    }

    public void deleteTheaterById(long id) {
        sqLiteDatabase.delete(DB.TABLE_THEATRES, DB.COLUMN_ID + " = " + id, null);
    }

    public void addSeance(Seance seance) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_MOVIE_ID, seance.getMovieId());
        cv.put(DB.COLUMN_THEATRE_ID,seance.getTheaterId());
        cv.put(DB.COLUMN_IS_3D,seance.isIs3D());
        cv.put(DB.COLUMN_TIME,seance.getTime());
        sqLiteDatabase.insert(DB.TABLE_SEANCES, null, cv);
    }

    public void updateTheater(Theater theater) {

        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, theater.getName());
        cv.put(DB.COLUMN_ADDRESS,theater.getAddress());

        sqLiteDatabase.update(DB.TABLE_THEATRES,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(theater.getId()) });
    }

    public void updateMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, movie.getName());
        cv.put(DB.COLUMN_LENGTH,movie.getLength());

        sqLiteDatabase.update(DB.TABLE_MOVIES,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(movie.getId()) });
    }

    public void updateSeance(Seance seance) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_THEATRE_ID, seance.getTheaterId());
        cv.put(DB.COLUMN_MOVIE_ID,seance.getMovieId());
        cv.put(DB.COLUMN_IS_3D,seance.isIs3D());
        cv.put(DB.COLUMN_TIME,seance.getTime());

        sqLiteDatabase.update(DB.TABLE_MOVIES,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(seance.getId()) });
    }

    public void deleteSeanceById(long id) {
        sqLiteDatabase.delete(DB.TABLE_SEANCES, DB.COLUMN_ID + " = " + id, null);
    }
}

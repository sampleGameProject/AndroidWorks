package pigareva_10moa.lab45.data;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import pigareva_10moa.lab45.PosterActivity;
import pigareva_10moa.lab45.R;
import pigareva_10moa.lab45.data.models.Seance;
import pigareva_10moa.lab45.data.models.Theater;

public class TheaterActivity  extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    // TheaterActivity работает аналогично MoviesActivity. Для кинотеатра можно редактировать список сеансов

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    ListView listView;
    PosterDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;
    long theaterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        theaterId = getIntent().getLongExtra(DB.COLUMN_ID, 0l);
        String theaterName = getIntent().getStringExtra(DB.COLUMN_NAME);

        getActionBar().setTitle(String.format("Сеансы в кинотеатре '%s'",theaterName));

        // открываем подключение к БД
        dataSource = new PosterDataSource(this);
        dataSource.open();


        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        String[] from = new String[] { DB.COLUMN_NAME, DB.COLUMN_TIME, DB.COLUMN_IS_3D  };
        int[] to = new int[] { R.id.textViewName, R.id.textViewTime, R.id.checkBoxIs3D };
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.seance_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        // viewBinder нужен ради адекватного отображения checkBox
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue (View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.textViewName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewTime:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.checkBoxIs3D:
                        ((CheckBox)view).setChecked(cursor.getInt(columnIndex) == 1);

                        return true;
                }

                return false;

            }
        });

        // добавляем контекстное меню к списку
        registerForContextMenu(listView);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSeanceDialog(null);
            }
        });

    }

    private void showSeanceDialog(final Seance seance) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_seance_layout);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);

        String[]  from = new String[] { DB.COLUMN_NAME, DB.COLUMN_LENGTH,  };
        int[] to = new int[] { R.id.textViewTime, R.id.textViewLength };
        final SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(this, R.layout.movie_list_item, null, from, to, 0);

        spinner.setAdapter(spinnerAdapter);

        getSupportLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new MoviesListActivity.MyCursorLoader(TheaterActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                spinnerAdapter.swapCursor(cursor);

                if(seance != null){
                    for (int i = 0; i < spinner.getCount(); i++) {
                        Cursor value = (Cursor) spinner.getItemAtPosition(i);
                        long id = value.getLong(value.getColumnIndex("_id"));
                        if (id == seance.getId()) {
                            spinner.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        if(seance == null) {
            dialog.setTitle(getString(R.string.titleEdit));

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Seance seance = new Seance();
                    seance.setTheaterId(theaterId);
                    seance.setMovieId(spinnerAdapter.getCursor().getLong(0));
                    seance.setIs3D(checkBox.isChecked());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    seance.setTime( formatter.format(calendar.getTime())); // 08:00

                    dialog.cancel();

                    ContentValues cv = new ContentValues();
                    cv.put(DB.COLUMN_MOVIE_ID, seance.getMovieId());
                    cv.put(DB.COLUMN_THEATRE_ID,theaterId);
                    cv.put(DB.COLUMN_IS_3D,seance.isIs3D());
                    cv.put(DB.COLUMN_TIME,seance.getTime());

                    getContentResolver().insert(PosterContentProvider.SEANCES_CONTENT_URI, cv);


                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.titleEdit));

            checkBox.setChecked(seance.isIs3D());

            {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                Date time = null;

                try {
                    time = formatter.parse(seance.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendar.setTime(time);

                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            }

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    seance.setMovieId(spinnerAdapter.getCursor().getLong(0));
                    seance.setIs3D(checkBox.isChecked());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    seance.setTime(formatter.format(calendar.getTime())); // 08:00

                    dialog.cancel();

                    ContentValues cv = new ContentValues();
                    cv.put(DB.COLUMN_MOVIE_ID, seance.getMovieId());
                    cv.put(DB.COLUMN_IS_3D, seance.isIs3D());
                    cv.put(DB.COLUMN_TIME, seance.getTime());

                    getContentResolver().update(PosterContentProvider.SEANCES_CONTENT_URI, cv,
                            DB.COLUMN_ID + "=" + seance.getId(), null);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }

        dialog.show();
    }



    public boolean onContextItemSelected(MenuItem item) {
        // получаем из пункта контекстного меню данные по пункту списка

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == CM_DELETE_ID) {

            // извлекаем id записи и удаляем соответствующую запись в БД

            Uri uri = Uri.parse(PosterContentProvider.SEANCES_CONTENT_URI + "/"  + acmi.id);
            getContentResolver().delete(uri, null, null);

            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            Seance seance = new Seance();

            Cursor cursor = (Cursor) simpleCursorAdapter.getItem(acmi.position);

//            for(int i = 0; i < 5; i++)
//            Log.i("TEST",String.format("%s -> %s",cursor.getColumnName(i),cursor.getString(i)));

            seance.setId(cursor.getLong(0));
            seance.setMovieId(cursor.getLong(cursor.getColumnIndex(DB.COLUMN_MOVIE_ID)));
            seance.setTime(cursor.getString(cursor.getColumnIndex(DB.COLUMN_TIME)));
            seance.setIs3D(cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IS_3D)) == 1);
            seance.setTheaterId(theaterId);

            showSeanceDialog(seance);
        }

        return super.onContextItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, getString(R.string.titleDelete));
        menu.add(0, CM_EDIT, 0, getString(R.string.titleEdit));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = { DB.COLUMN_NAME, DB.COLUMN_TIME, DB.COLUMN_IS_3D };
        CursorLoader cursorLoader = new CursorLoader(this,
                PosterContentProvider.SEANCES_CONTENT_URI, projection,
                DB.TABLE_SEANCES + "." + DB.COLUMN_THEATRE_ID + "=" + theaterId, null, null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

}

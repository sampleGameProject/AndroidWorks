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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.concurrent.TimeUnit;

import pigareva_10moa.lab45.PosterActivity;
import pigareva_10moa.lab45.R;
import pigareva_10moa.lab45.data.models.Movie;
import pigareva_10moa.lab45.data.models.Theater;

public class MoviesListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    ListView listView;
    PosterDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        // открываем подключение к БД
        dataSource = new PosterDataSource(this);
        dataSource.open();


        listView = (ListView) findViewById(R.id.listView);
        // биндинг для курсора
        String[] from  = new String[] { DB.COLUMN_NAME, DB.COLUMN_LENGTH,  };
        int[] to = new int[] { R.id.textViewTime, R.id.textViewLength};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.movie_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(listView);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMovieDialog(null);
            }
        });
    }

    private void showMovieDialog(final Movie movie){

        // диалог - создание или редактирование фильма
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_movie_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText length = (EditText) dialog.findViewById(R.id.editTextLength);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        if(movie != null){
            name.setText(movie.getName());
            length.setText(Integer.toString(movie.getLength()));
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    movie.setName(name.getText().toString());
                    movie.setLength(Integer.parseInt(length.getText().toString()));

                    dialog.cancel();
                    // обновляем запись
                    dataSource.updateMovie(movie);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.titleNewMovie));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Movie movie = new Movie();
                    movie.setName(name.getText().toString());
                    movie.setLength(Integer.parseInt(length.getText().toString()));

                    dialog.cancel();
                    // добавляем запись
                    dataSource.addMovie(movie);

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

            dataSource.deleteMovieById(acmi.id);


            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            Movie movie = new Movie();

            movie.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
            movie.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
            movie.setLength(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getInt(2));
            showMovieDialog(movie);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new MyCursorLoader(this,dataSource);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public static class MyCursorLoader extends CursorLoader {

        PosterDataSource db;

        public MyCursorLoader(Context context, PosterDataSource db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor =db.getMovies();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

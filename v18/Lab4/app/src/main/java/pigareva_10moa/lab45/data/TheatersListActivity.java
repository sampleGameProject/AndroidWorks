package pigareva_10moa.lab45.data;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import pigareva_10moa.lab45.data.models.Theater;

public class TheatersListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    // аналогично MoviesListActivity

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

            // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_NAME, DB.COLUMN_ADDRESS,  };
        int[] to = new int[] { R.id.textViewTime, R.id.textViewLength};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.theater_list_item, null, from, to, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Intent intent = new Intent(TheatersListActivity.this, TheaterActivity.class);
                intent.putExtra(DB.COLUMN_ID, ((Cursor) simpleCursorAdapter.getItem(position)).getLong(0));
                intent.putExtra(DB.COLUMN_NAME, ((Cursor) simpleCursorAdapter.getItem(position)).getString(1));
                startActivity(intent);
            }
        });



        listView.setAdapter(simpleCursorAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(listView);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTheaterDialog(null);

            }
        });
    }

    private void showTheaterDialog(final Theater theater) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_theater_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText address = (EditText) dialog.findViewById(R.id.editTextAddress);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        dialog.setTitle(getString(R.string.titleNewTheater));

        if(theater != null){
            name.setText(theater.getName());
            address.setText(theater.getAddress());
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    theater.setName(name.getText().toString());
                    theater.setAddress(address.getText().toString());

                    dialog.cancel();

                   dataSource.updateTheater(theater);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });

        }
        else
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Theater theater = new Theater();
                    theater.setName(name.getText().toString());
                    theater.setAddress(address.getText().toString());

                    dialog.cancel();

                    dataSource.addTheater(theater);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });

        dialog.show();
    }

    public boolean onContextItemSelected(MenuItem item) {
        // получаем из пункта контекстного меню данные по пункту списка

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

            if (item.getItemId() == CM_DELETE_ID) {


                // извлекаем id записи и удаляем соответствующую запись в БД

                dataSource.deleteTheaterById(acmi.id);

                // получаем новый курсор с данными
                getSupportLoaderManager().getLoader(0).forceLoad();
                return true;
            }
            if(item.getItemId() == CM_EDIT){
                Theater theater = new Theater();

                theater.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
                theater.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
                theater.setAddress(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(2));

                showTheaterDialog(theater);
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
            Cursor cursor = db.getTheatres();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

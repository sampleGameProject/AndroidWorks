package kalashnikov_10po2.lab45.data;

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

import kalashnikov_10po2.lab45.AutoServiceActivity;
import kalashnikov_10po2.lab45.R;
import kalashnikov_10po2.lab45.data.models.Car;

public class CarsListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    ListView listView;
    AutoServiceDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;

    boolean useContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        // определяем, используется ли Content Provider
        useContentProvider = getSharedPreferences(AutoServiceActivity.DATA_PREF,MODE_PRIVATE).getBoolean(AutoServiceActivity.USE_CONTENT_PROVIDER,false);

        if(!useContentProvider){//если нет, то начинаем работать прямо с базов
            // открываем подключение к БД
            dataSource = new AutoServiceDataSource(this);
            dataSource.open();
        }

        listView = (ListView) findViewById(R.id.listView);
        // биндинг для курсора
        String[] from  = new String[] { DB.COLUMN_CAR_NAME, DB.COLUMN_NUMBER,  };
        int[] to = new int[] { R.id.textViewCarName, R.id.textViewCarNumber};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.car_list_item, null, from, to, 0);

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

    private void showMovieDialog(final Car car){

        // диалог - создание или редактирование фильма
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_car_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText number = (EditText) dialog.findViewById(R.id.editTextLength);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        if(car != null){
            name.setText(car.getName());
            number.setText(car.getNumber());
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    car.setName(name.getText().toString());
                    car.setNumber(number.getText().toString());

                    dialog.cancel();
                    // обновляем запись
                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_CAR_NAME, car.getName());
                        cv.put(DB.COLUMN_NUMBER, car.getNumber());

                        getContentResolver().update(AutoServiceContentProvider.CARS_CONTENT_URI, cv,
                                DB.COLUMN_ID + "=" + car.getId() ,null);
                    }
                    else
                        dataSource.updateCar(car);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.titleNewEmployee));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Car car = new Car();
                    car.setName(name.getText().toString());
                    car.setNumber(number.getText().toString());

                    dialog.cancel();
                    // добавляем запись
                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_CAR_NAME, car.getName());
                        cv.put(DB.COLUMN_NUMBER, car.getNumber());

                        getContentResolver().insert(AutoServiceContentProvider.CARS_CONTENT_URI, cv);
                    }
                    else
                        dataSource.addCar(car);

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

            if (useContentProvider) {
                Uri uri = Uri.parse(AutoServiceContentProvider.CARS_CONTENT_URI + "/"
                        + acmi.id);
                getContentResolver().delete(uri, null, null);
            }
            else
                dataSource.deleteCarById(acmi.id);


            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            Car car = new Car();

            car.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
            car.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
            car.setNumber(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(2));
            showMovieDialog(car);
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

        if(useContentProvider){
            String[] projection = { DB.COLUMN_ID, DB.COLUMN_CAR_NAME,DB.COLUMN_NUMBER};
            CursorLoader cursorLoader = new CursorLoader(this,
                    AutoServiceContentProvider.CARS_CONTENT_URI, projection, null, null, null);
            return cursorLoader;
        }

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

        AutoServiceDataSource db;

        public MyCursorLoader(Context context, AutoServiceDataSource db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor =db.getCars();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

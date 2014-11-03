package kalashnikov_10po2.lab45.data;

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

import kalashnikov_10po2.lab45.AutoServiceActivity;
import kalashnikov_10po2.lab45.R;
import kalashnikov_10po2.lab45.data.models.Employee;

public class EmployeesListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;
    ListView listView;
    AutoServiceDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;
    private boolean useContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        useContentProvider = getSharedPreferences(AutoServiceActivity.DATA_PREF,MODE_PRIVATE).getBoolean(AutoServiceActivity.USE_CONTENT_PROVIDER,false);

        if(!useContentProvider) {
            // открываем подключение к БД
            dataSource = new AutoServiceDataSource(this);
            dataSource.open();
        }

        listView = (ListView) findViewById(R.id.listView);

            // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_NAME, DB.COLUMN_POST,  };
        int[] to = new int[] { R.id.textViewEmployeeName, R.id.textViewEmployeePost};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.employee_list_item, null, from, to, 0);

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

    private void showTheaterDialog(final Employee employee) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_employee_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText post = (EditText) dialog.findViewById(R.id.editTextAddress);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        dialog.setTitle(getString(R.string.titleNewEmployee));

        if(employee != null){
            name.setText(employee.getName());
            post.setText(employee.getPost());
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    employee.setName(name.getText().toString());
                    employee.setPost(post.getText().toString());

                    dialog.cancel();

                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_NAME, employee.getName());
                        cv.put(DB.COLUMN_POST, employee.getPost());

                        getContentResolver().update(AutoServiceContentProvider.EMPLOYEES_CONTENT_URI, cv,
                                DB.COLUMN_ID + "=" + employee.getId() ,null);
                    }
                    else
                        dataSource.updateEmployee(employee);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });

        }
        else
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Employee employee = new Employee();
                    employee.setName(name.getText().toString());
                    employee.setPost(post.getText().toString());

                    dialog.cancel();

                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_NAME, employee.getName());
                        cv.put(DB.COLUMN_POST, employee.getPost());

                        getContentResolver().insert(AutoServiceContentProvider.EMPLOYEES_CONTENT_URI, cv);
                    }
                    else
                        dataSource.addEmployee(employee);

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

                if (useContentProvider){
                    Uri uri = Uri.parse(AutoServiceContentProvider.EMPLOYEES_CONTENT_URI + "/"  + acmi.id);
                    getContentResolver().delete(uri, null, null);
                }
                else
                    dataSource.deleteEmployeeById(acmi.id);

                // получаем новый курсор с данными
                getSupportLoaderManager().getLoader(0).forceLoad();
                return true;
            }
            if(item.getItemId() == CM_EDIT){
                Employee employee = new Employee();

                employee.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
                employee.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
                employee.setPost(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(2));

                showTheaterDialog(employee);
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
            String[] projection = { DB.COLUMN_ID, DB.COLUMN_NAME,DB.COLUMN_POST};
            CursorLoader cursorLoader = new CursorLoader(this,
                    AutoServiceContentProvider.EMPLOYEES_CONTENT_URI, projection, null, null, null);
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
            Cursor cursor = db.getEmployee();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

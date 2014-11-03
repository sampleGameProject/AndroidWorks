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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import kalashnikov_10po2.lab45.AutoServiceActivity;
import kalashnikov_10po2.lab45.R;
import kalashnikov_10po2.lab45.data.models.Task;

public class TasksListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    ListView listView;
    AutoServiceDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;
    long theaterId;
    private boolean useContentProvider;

    final int DEFAULT = 0;
    final int EMPLOYEE_TO_SPINNER = 1;
    final int CAR_TO_SPINNER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        useContentProvider = getSharedPreferences(AutoServiceActivity.DATA_PREF,MODE_PRIVATE).
                getBoolean(AutoServiceActivity.USE_CONTENT_PROVIDER, false);

        getActionBar().setTitle(getString(R.string.buttonTitleTasks));


        // открываем подключение к БД
        dataSource = new AutoServiceDataSource(this);
        dataSource.open();


        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        String[] from = new String[] {
                DB.COLUMN_TASK,
                DB.COLUMN_NAME,
                DB.COLUMN_IS_COMPLETE,
                DB.COLUMN_CAR_NAME
        };

        int[] to = new int[] {
                R.id.textViewTask,
                R.id.textViewEmployeeName,
                R.id.checkBoxIsComplete,
                R.id.textViewCarName
        };
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.task_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        // viewBinder нужен ради адекватного отображения checkBox
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue (View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.textViewTask:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewEmployeeName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewCarName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.checkBoxIsComplete:
                        ((CheckBox)view).setChecked(cursor.getInt(columnIndex) == 1);
                        return true;
                }

                return false;

            }
        });

        // добавляем контекстное меню к списку
        registerForContextMenu(listView);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(DEFAULT, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialog(null);
            }
        });

    }

    private void showTaskDialog(final Task task) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_task_layout);

        final EditText editTextTask = (EditText) dialog.findViewById(R.id.editTextTask);

        final Spinner spinnerCar = (Spinner) dialog.findViewById(R.id.spinner);
        final Spinner spinnerEmployee = (Spinner) dialog.findViewById(R.id.spinner2);

        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);

        String[]  from = new String[] { DB.COLUMN_CAR_NAME, DB.COLUMN_NUMBER,  };
        int[] to = new int[] { R.id.textViewCarName, R.id.textViewCarNumber };
        final SimpleCursorAdapter carSpinnerAdapter = new SimpleCursorAdapter(this,
                R.layout.car_list_item, null, from, to, 0);

        spinnerCar.setAdapter(carSpinnerAdapter);


        final SimpleCursorAdapter employeeSpinnerAdapter = new SimpleCursorAdapter(
                this,
                R.layout.employee_list_item,
                null,
                new String[] { DB.COLUMN_NAME, DB.COLUMN_POST,  },
                new int[] { R.id.textViewEmployeeName, R.id.textViewEmployeePost },
                0);

        spinnerEmployee.setAdapter(employeeSpinnerAdapter);


        getSupportLoaderManager().initLoader(EMPLOYEE_TO_SPINNER, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new EmployeesListActivity.MyCursorLoader(TasksListActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                employeeSpinnerAdapter.swapCursor(cursor);

                if(task != null){
                    for (int i = 0; i < spinnerEmployee.getCount(); i++) {
                        Cursor value = (Cursor) spinnerEmployee.getItemAtPosition(i);
                        long id = value.getLong(value.getColumnIndex("_id"));
                        if (id == task.getId()) {
                            spinnerEmployee.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

        getSupportLoaderManager().initLoader(CAR_TO_SPINNER, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CarsListActivity.MyCursorLoader(TasksListActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                carSpinnerAdapter.swapCursor(cursor);

                if(task != null){
                    for (int i = 0; i < spinnerCar.getCount(); i++) {
                        Cursor value = (Cursor) spinnerCar.getItemAtPosition(i);
                        long id = value.getLong(value.getColumnIndex("_id"));
                        if (id == task.getId()) {
                            spinnerCar.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        if(task == null) {
            dialog.setTitle(getString(R.string.titleEdit));

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Task task = new Task();

                    task.setCarId(carSpinnerAdapter.getCursor().getLong(0));
                    task.setComplete(checkBox.isChecked());
                    task.setEmployeeId(employeeSpinnerAdapter.getCursor().getLong(0));
                    task.setTask(editTextTask.getText().toString());

                    dialog.cancel();

                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_CAR_ID, task.getCarId());
                        cv.put(DB.COLUMN_EMPLOYEE_ID,task.getEmployeeId());
                        cv.put(DB.COLUMN_IS_COMPLETE, task.isComplete());
                        cv.put(DB.COLUMN_TASK, task.getTask());

                        getContentResolver().insert(AutoServiceContentProvider.TASKS_CONTENT_URI, cv);
                    }
                    else
                        dataSource.addTask(task);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.titleEdit));

            checkBox.setChecked(task.isComplete());
            editTextTask.setText(task.getTask());

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    task.setCarId(carSpinnerAdapter.getCursor().getLong(0));
                    task.setComplete(checkBox.isChecked());
                    task.setEmployeeId(employeeSpinnerAdapter.getCursor().getLong(0));
                    task.setTask(editTextTask.getText().toString());

                    dialog.cancel();

                    if (useContentProvider) {
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_CAR_ID, task.getCarId());
                        cv.put(DB.COLUMN_EMPLOYEE_ID,task.getEmployeeId());
                        cv.put(DB.COLUMN_IS_COMPLETE, task.isComplete());
                        cv.put(DB.COLUMN_TASK, task.getTask());

                        getContentResolver().update(AutoServiceContentProvider.TASKS_CONTENT_URI, cv,
                                DB.COLUMN_ID + "=" + task.getId(), null);
                    } else
                        dataSource.updateTask(task);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.theater, menu);
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

    public boolean onContextItemSelected(MenuItem item) {
        // получаем из пункта контекстного меню данные по пункту списка

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == CM_DELETE_ID) {

            // извлекаем id записи и удаляем соответствующую запись в БД

            if (useContentProvider){
                Uri uri = Uri.parse(AutoServiceContentProvider.TASKS_CONTENT_URI + "/"  + acmi.id);
                getContentResolver().delete(uri, null, null);
            }
            else
                dataSource.deleteTaskById(acmi.id);

            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            Task task = new Task();

            Cursor cursor = (Cursor) simpleCursorAdapter.getItem(acmi.position);

            task.setId(cursor.getLong(0));
            task.setCarId(cursor.getLong(cursor.getColumnIndex(DB.COLUMN_CAR_ID)));
            task.setTask(cursor.getString(cursor.getColumnIndex(DB.COLUMN_TASK)));
            task.setComplete(cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IS_COMPLETE)) == 1);
            task.setEmployeeId(cursor.getLong(cursor.getColumnIndex(DB.COLUMN_EMPLOYEE_ID)));

            showTaskDialog(task);
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
        if(useContentProvider){
            String[] projection = { DB.COLUMN_NAME, DB.COLUMN_TASK, DB.COLUMN_IS_COMPLETE};
            CursorLoader cursorLoader = new CursorLoader(this,
                    AutoServiceContentProvider.TASKS_CONTENT_URI, projection,
                    null, null, null);
            return cursorLoader;
        }

        return new MyCursorLoader(this, dataSource, theaterId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    static class MyCursorLoader extends CursorLoader {

        AutoServiceDataSource db;
        long theaterId;
        public MyCursorLoader(Context context, AutoServiceDataSource db, long theaterId) {
            super(context);
            this.db = db;
            this.theaterId = theaterId;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getTasks();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

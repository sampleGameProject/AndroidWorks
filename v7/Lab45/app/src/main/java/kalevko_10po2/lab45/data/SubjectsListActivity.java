package kalevko_10po2.lab45.data;

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
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import kalevko_10po2.lab45.PlanActivity;
import kalevko_10po2.lab45.R;
import kalevko_10po2.lab45.data.models.Subject;

public class SubjectsListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    ListView listView;
    PlanDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;

    boolean useContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        // определяем, используется ли Content Provider
        useContentProvider = getSharedPreferences(PlanActivity.DATA_PREF,MODE_PRIVATE).getBoolean(PlanActivity.USE_CP,false);


        // открываем подключение к БД
        dataSource = new PlanDataSource(this);
        dataSource.open();


        listView = (ListView) findViewById(R.id.listView);
        // биндинг для курсора
        String[] from  = new String[] { DB.COLUMN_SUBJECT_NAME, DB.COLUMN_IS_LECTURE,  };
        int[] to = new int[] { R.id.textViewSubjectName, R.id.checkBoxIsLecture};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.subject_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue (View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.textViewSubjectName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.checkBoxIsLecture:
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
                showSubjectDialog(null);
            }
        });
    }

    private void showSubjectDialog(final Subject subject){

        // диалог - создание или редактирование фильма
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_subject_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextSubjectName);
        final CheckBox checkBoxIsLecture = (CheckBox) dialog.findViewById(R.id.checkBoxIsLecture);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        if(subject != null){
            dialog.setTitle(getString(R.string.title1));
            name.setText(subject.getName());
            checkBoxIsLecture.setChecked(subject.isLecture());
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    subject.setName(name.getText().toString());
                    subject.setLecture(checkBoxIsLecture.isChecked());

                    dialog.cancel();
                    // обновляем запись
                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_SUBJECT_NAME, subject.getName());
                        cv.put(DB.COLUMN_IS_LECTURE, subject.isLecture());

                        getContentResolver().update(PlanContentProvider.SUBJECTS_CONTENT_URI, cv,
                                DB.COLUMN_ID + "=" + subject.getId() ,null);
                    }
                    else
                        dataSource.updateSubject(subject);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.title2));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Subject subject = new Subject();
                    subject.setName(name.getText().toString());
                    subject.setLecture(checkBoxIsLecture.isChecked());

                    dialog.cancel();
                    // добавляем запись
                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_SUBJECT_NAME, subject.getName());
                        cv.put(DB.COLUMN_IS_LECTURE, subject.isLecture());

                        getContentResolver().insert(PlanContentProvider.SUBJECTS_CONTENT_URI, cv);
                    }
                    else
                        dataSource.addSubject(subject);

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
                Uri uri = Uri.parse(PlanContentProvider.SUBJECTS_CONTENT_URI + "/"
                        + acmi.id);
                getContentResolver().delete(uri, null, null);
            }
            else
                dataSource.deleteSubjectById(acmi.id);


            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            Subject subject = new Subject();

            subject.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
            subject.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
            subject.setLecture(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getInt(2) == 1);
            showSubjectDialog(subject);
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
            String[] projection = { DB.COLUMN_ID, DB.COLUMN_SUBJECT_NAME,DB.COLUMN_IS_LECTURE };
            CursorLoader cursorLoader = new CursorLoader(this,
                    PlanContentProvider.SUBJECTS_CONTENT_URI, projection, null, null, null);
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

        PlanDataSource db;

        public MyCursorLoader(Context context, PlanDataSource db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getSubjects();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

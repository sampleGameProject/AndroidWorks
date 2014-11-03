package kalevko_10po2.lab45.data;

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

import kalevko_10po2.lab45.PlanActivity;
import kalevko_10po2.lab45.R;
import kalevko_10po2.lab45.data.models.Teacher;

public class TeachersListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;
    ListView listView;
    PlanDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;
    private boolean useContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        useContentProvider = getSharedPreferences(PlanActivity.DATA_PREF,MODE_PRIVATE).getBoolean(PlanActivity.USE_CP,false);

        dataSource = new PlanDataSource(this);
        dataSource.open();


        listView = (ListView) findViewById(R.id.listView);


        String[] from = new String[] { DB.COLUMN_NAME, DB.COLUMN_PHONE,  };
        int[] to = new int[] { R.id.textViewName, R.id.textViewPhone};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.teacher_list_item, null, from, to, 0);


        listView.setAdapter(simpleCursorAdapter);


        registerForContextMenu(listView);


        getSupportLoaderManager().initLoader(0, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTeacherDialog(null);

            }
        });
    }

    private void showTeacherDialog(final Teacher teacher) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_teacher_layout);

        final EditText name = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText phone = (EditText) dialog.findViewById(R.id.editTextPhone);
        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);



        if(teacher != null){
            dialog.setTitle(getString(R.string.title1));
            name.setText(teacher.getName());
            phone.setText(teacher.getPhone());
            done.setText(getString(R.string.titleEdit));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    teacher.setName(name.getText().toString());
                    teacher.setPhone(phone.getText().toString());

                    dialog.cancel();

                    if(useContentProvider){
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_SUBJECT_NAME, teacher.getName());
                        cv.put(DB.COLUMN_PHONE, teacher.getPhone());

                        getContentResolver().update(PlanContentProvider.TEACHERS_CONTENT_URI, cv,
                                DB.COLUMN_ID + "=" + teacher.getId() ,null);
                    }
                    else
                        dataSource.updateTeacher(teacher);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });

        }
        else {
            dialog.setTitle(getString(R.string.title2));
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Teacher teacher = new Teacher();
                    teacher.setName(name.getText().toString());
                    teacher.setPhone(phone.getText().toString());

                    dialog.cancel();

                    if (useContentProvider) {
                        ContentValues cv = new ContentValues();
                        cv.put(DB.COLUMN_SUBJECT_NAME, teacher.getName());
                        cv.put(DB.COLUMN_PHONE, teacher.getPhone());

                        getContentResolver().insert(PlanContentProvider.TEACHERS_CONTENT_URI, cv);
                    } else
                        dataSource.addTeacher(teacher);

                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
        }

        dialog.show();

    }

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

            if (item.getItemId() == CM_DELETE_ID) {


                if (useContentProvider){
                    Uri uri = Uri.parse(PlanContentProvider.TEACHERS_CONTENT_URI + "/"  + acmi.id);
                    getContentResolver().delete(uri, null, null);
                }
                else
                    dataSource.deleteTeacherById(acmi.id);


                getSupportLoaderManager().getLoader(0).forceLoad();
                return true;
            }
            if(item.getItemId() == CM_EDIT){
                Teacher teacher = new Teacher();

                teacher.setId(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getLong(0));
                teacher.setName(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(1));
                teacher.setPhone(((Cursor) simpleCursorAdapter.getItem(acmi.position)).getString(2));

                showTeacherDialog(teacher);
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
            String[] projection = { DB.COLUMN_ID, DB.COLUMN_NAME,DB.COLUMN_PHONE};
            CursorLoader cursorLoader = new CursorLoader(this,
                    PlanContentProvider.TEACHERS_CONTENT_URI, projection, null, null, null);
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
            Cursor cursor = db.getTeachers();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

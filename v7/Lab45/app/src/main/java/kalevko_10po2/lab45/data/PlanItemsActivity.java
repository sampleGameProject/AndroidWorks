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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import kalevko_10po2.lab45.PlanActivity;
import kalevko_10po2.lab45.R;
import kalevko_10po2.lab45.data.models.PlanItem;

public class PlanItemsActivity extends FragmentActivity {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT = 2;

    private static final int LOADER_DEFAULT = 0;
    private static final int LOADER_SPINNER_SUBJECTS = 1;
    private static final int LOADER_SPINNER_TEACHERS = 2;

    ListView listView;
    PlanDataSource dataSource;
    SimpleCursorAdapter simpleCursorAdapter;
    long theaterId;
    private boolean useContentProvider;

    String[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_items);

        useContentProvider = getSharedPreferences(PlanActivity.DATA_PREF,MODE_PRIVATE).getBoolean(PlanActivity.USE_CP,false);

        dataSource = new PlanDataSource(this);
        dataSource.open();

        days =  getResources().getStringArray(R.array.days);

        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        String[] from = new String[] {  DB.COLUMN_SUBJECT_NAME,     DB.COLUMN_NAME,     DB.COLUMN_TIME,     DB.COLUMN_DAY,      DB.COLUMN_IS_LECTURE };
        int[] to = new int[] {          R.id.textViewSubjectName,   R.id.textViewName,  R.id.textViewTime , R.id.textViewDay,   R.id.textViewIsLection };
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.plan_item_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue (View view, Cursor cursor, int columnIndex) {
                switch (view.getId()){
                    case R.id.textViewName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewSubjectName:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewTime:
                        ((TextView)view).setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.textViewDay:
                        ((TextView)view).setText(days[cursor.getInt(columnIndex)]);
                        return true;
                    case R.id.textViewIsLection:
                        if(cursor.getInt(columnIndex) == 1)
                            ((TextView)view).setText("Лекция");
                        else
                            ((TextView)view).setText("Практика");
                        return true;
                }

                return false;

            }
        });

        registerForContextMenu(listView);

        getSupportLoaderManager().initLoader(LOADER_DEFAULT, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if(useContentProvider) {

                    String[] projection = {DB.COLUMN_SUBJECT_NAME, DB.COLUMN_TIME, DB.COLUMN_DAY,
                            DB.COLUMN_NAME,DB.COLUMN_ID,DB.COLUMN_IS_LECTURE};
                    CursorLoader cursorLoader = new CursorLoader(PlanItemsActivity.this,
                            PlanContentProvider.PLAN_ITEMS_CONTENT_URI, projection, null, null, null);
                    return cursorLoader;
                }
                return new MyCursorLoader(PlanItemsActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                simpleCursorAdapter.swapCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanItemDialog(null);
            }
        });

    }

    private void showPlanItemDialog(final PlanItem planItem) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_plan_item_layout);

        final Spinner subjectsSpinner = (Spinner) dialog.findViewById(R.id.spinnerSubjects);
        final Spinner daysSpinner = (Spinner) dialog.findViewById(R.id.spinnerDays);
        final Spinner teachersSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeachers);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);

        // init subjects spinner ==========================================================================

        final SimpleCursorAdapter subjectsSpinnerAdapter = new SimpleCursorAdapter(this,
                R.layout.subject_list_item, null,
                new String[] { DB.COLUMN_SUBJECT_NAME, DB.COLUMN_IS_LECTURE,  },
                new int[] { R.id.textViewSubjectName, R.id.checkBoxIsLecture },
                0);

        subjectsSpinner.setAdapter(subjectsSpinnerAdapter);

        getSupportLoaderManager().initLoader(LOADER_SPINNER_SUBJECTS, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new SubjectsListActivity.MyCursorLoader(PlanItemsActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                subjectsSpinnerAdapter.swapCursor(cursor);

                if(planItem != null){
                    for (int i = 0; i < subjectsSpinner.getCount(); i++) {
                        Cursor value = (Cursor) subjectsSpinner.getItemAtPosition(i);
                        long id = value.getLong(value.getColumnIndex("_id"));
                        if (id == planItem.getSubjectId()) {
                            subjectsSpinner.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });


        // init teachers spinner ==========================================================================

        final SimpleCursorAdapter teachersSpinnerAdapter = new SimpleCursorAdapter(this,
                R.layout.teacher_list_item, null,
                new String[] { DB.COLUMN_NAME, DB.COLUMN_PHONE,  },
                new int[] { R.id.textViewName, R.id.textViewPhone },
                0);

        teachersSpinner.setAdapter(teachersSpinnerAdapter);

        getSupportLoaderManager().initLoader(LOADER_SPINNER_TEACHERS, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new TeachersListActivity.MyCursorLoader(PlanItemsActivity.this, dataSource);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                teachersSpinnerAdapter.swapCursor(cursor);

                if(planItem != null){
                    for (int i = 0; i < teachersSpinner.getCount(); i++) {
                        Cursor value = (Cursor) teachersSpinner.getItemAtPosition(i);
                        long id = value.getLong(value.getColumnIndex("_id"));
                        if (id == planItem.getTeacherId()) {
                            teachersSpinner.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

        // init days spinner ==========================================================================

        final ArrayAdapter<?> daysAdapter =  ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(daysAdapter);

        final Button done = (Button) dialog.findViewById(R.id.buttonAdd);

        // let's go

        if(planItem == null) {
            dialog.setTitle("Добавить");

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlanItem planItem = new PlanItem();
                    planItem.setTeacherId(teachersSpinnerAdapter.getCursor().getLong(0));
                    planItem.setSubjectId(subjectsSpinnerAdapter.getCursor().getLong(0));
                    planItem.setDay(daysSpinner.getSelectedItemPosition());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    planItem.setTime( formatter.format(calendar.getTime())); // 08:00

                    dialog.cancel();

                    savePlanItem(planItem,false);

                    getSupportLoaderManager().getLoader(LOADER_DEFAULT).forceLoad();
                }
            });
        }
        else {
            dialog.setTitle(getString(R.string.titleEdit));
            daysSpinner.setSelection(planItem.getDay());

            {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                Date time = null;

                try {
                    time = formatter.parse(planItem.getTime());
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

                    planItem.setTeacherId(teachersSpinnerAdapter.getCursor().getLong(0));
                    planItem.setSubjectId(subjectsSpinnerAdapter.getCursor().getLong(0));
                    planItem.setDay(daysSpinner.getSelectedItemPosition());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    planItem.setTime(formatter.format(calendar.getTime())); // 08:00

                    dialog.cancel();

                    savePlanItem(planItem,true);

                    getSupportLoaderManager().getLoader(LOADER_DEFAULT).forceLoad();
                }
            });
        }

        dialog.show();
    }

    private void savePlanItem(PlanItem planItem, boolean update){
        if (useContentProvider)
            savePlanItemWithContentProvider(planItem,update);
        else
            if(update)
                dataSource.updatePlanItem(planItem);
            else
                dataSource.addPlanItem(planItem);
    }

    private void savePlanItemWithContentProvider(PlanItem planItem, boolean update){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_ID, planItem.getSubjectId());
        cv.put(DB.COLUMN_TEACHER_ID, planItem.getTeacherId());
        cv.put(DB.COLUMN_DAY, planItem.getDay());
        cv.put(DB.COLUMN_TIME, planItem.getTime());

        if(update)
            getContentResolver().update(PlanContentProvider.PLAN_ITEMS_CONTENT_URI, cv, DB.COLUMN_ID + "=" + planItem.getId(), null);
        else
            getContentResolver().insert(PlanContentProvider.PLAN_ITEMS_CONTENT_URI, cv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.theater, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == CM_DELETE_ID) {
            deleteItem(acmi.id);
            getSupportLoaderManager().getLoader(LOADER_DEFAULT).forceLoad();
            return true;
        }
        if(item.getItemId() == CM_EDIT){
            PlanItem planItem = new PlanItem();

            Cursor cursor = (Cursor) simpleCursorAdapter.getItem(acmi.position);

            planItem.setId(cursor.getLong(0));
            planItem.setSubjectId(cursor.getLong(cursor.getColumnIndex(DB.COLUMN_SUBJECT_ID)));
            planItem.setTeacherId(cursor.getLong(cursor.getColumnIndex(DB.COLUMN_TEACHER_ID)));
            planItem.setTime(cursor.getString(cursor.getColumnIndex(DB.COLUMN_TIME)));
            planItem.setDay(cursor.getInt(cursor.getColumnIndex(DB.COLUMN_DAY)));

            showPlanItemDialog(planItem);
        }

        return super.onContextItemSelected(item);
    }

    protected void deleteItem(long id){
        if (useContentProvider){
            Uri uri = Uri.parse(PlanContentProvider.PLAN_ITEMS_CONTENT_URI + "/"  + id);
            getContentResolver().delete(uri, null, null);
        }
        else
            dataSource.deletePlanItemById(id);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, getString(R.string.titleDelete));
        menu.add(0, CM_EDIT, 0, getString(R.string.titleEdit));
    }


    static class MyCursorLoader extends CursorLoader {

        PlanDataSource db;
        public MyCursorLoader(Context context, PlanDataSource db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getPlanItems();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}

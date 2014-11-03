package kalevko_10po2.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kalevko_10po2.lab45.data.models.PlanItem;
import kalevko_10po2.lab45.data.models.Subject;
import kalevko_10po2.lab45.data.models.Teacher;

/**
 * Created by admin on 25.10.2014.
 */

// класс для работы с БД
public class PlanDataSource {

    private final Context context;

    private PlanSQLiteHepler hepler;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor actors;

    public PlanDataSource(Context ctx) {
        context = ctx;
    }

    // открыть подключение
    public void open() {
        hepler = new PlanSQLiteHepler(context);
        sqLiteDatabase = hepler.getWritableDatabase();
    }


    public void close() {
        if (hepler !=null) hepler.close();
    }


    public Cursor getSubjects() {
        return sqLiteDatabase.query(DB.TABLE_SUBJECTS, null, null, null, null, null, null);
    }

    public void addSubject(Subject mov) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, mov.getName());
        cv.put(DB.COLUMN_IS_LECTURE,mov.isLecture());
        sqLiteDatabase.insert(DB.TABLE_SUBJECTS, null, cv);
    }

    public void deleteSubjectById(long id) {
        sqLiteDatabase.delete(DB.TABLE_SUBJECTS, DB.COLUMN_ID + " = " + id, null);
    }

    public Cursor getTeachers() {
        return sqLiteDatabase.query(DB.TABLE_TEACHERS, null, null, null, null, null, null);
    }

    public Cursor getPlanItems() {

        final String items = "select * " +
                "from plan_items " +
                " join subjects " +
                        "on subjects._id = plan_items.subject_id " +
                " join teachers " +
                    "on teachers._id = plan_items.teacher_id ";

        return sqLiteDatabase.rawQuery(items,null );
    }

    public void addTeacher(Teacher teacher) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, teacher.getName());
        cv.put(DB.COLUMN_PHONE, teacher.getPhone());
        sqLiteDatabase.insert(DB.TABLE_TEACHERS, null, cv);
    }

    public void deleteTeacherById(long id) {
        sqLiteDatabase.delete(DB.TABLE_TEACHERS, DB.COLUMN_ID + " = " + id, null);
    }

    public void addPlanItem(PlanItem planItem) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_ID, planItem.getSubjectId());
        cv.put(DB.COLUMN_TEACHER_ID, planItem.getTeacherId());
        cv.put(DB.COLUMN_DAY, planItem.getDay());
        cv.put(DB.COLUMN_TIME, planItem.getTime());
        sqLiteDatabase.insert(DB.TABLE_PLAN_ITEMS, null, cv);
    }

    public void updateTeacher(Teacher teacher) {

        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, teacher.getName());
        cv.put(DB.COLUMN_PHONE, teacher.getPhone());

        sqLiteDatabase.update(DB.TABLE_TEACHERS,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(teacher.getId()) });
    }

    public void updateSubject(Subject subject) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_SUBJECT_NAME, subject.getName());
        cv.put(DB.COLUMN_IS_LECTURE, subject.isLecture());

        sqLiteDatabase.update(DB.TABLE_SUBJECTS,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(subject.getId()) });
    }

    public void updatePlanItem(PlanItem planItem) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_TEACHER_ID, planItem.getTeacherId());
        cv.put(DB.COLUMN_SUBJECT_ID, planItem.getSubjectId());
        cv.put(DB.COLUMN_IS_LECTURE, planItem.getDay());
        cv.put(DB.COLUMN_TIME, planItem.getTime());

        sqLiteDatabase.update(DB.TABLE_SUBJECTS,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(planItem.getId()) });
    }

    public void deletePlanItemById(long id) {
        sqLiteDatabase.delete(DB.TABLE_PLAN_ITEMS, DB.COLUMN_ID + " = " + id, null);
    }
}

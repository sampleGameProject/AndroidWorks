package kalashnikov_10po2.lab45.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kalashnikov_10po2.lab45.data.models.Car;
import kalashnikov_10po2.lab45.data.models.Employee;
import kalashnikov_10po2.lab45.data.models.Task;

/**
 * Created by admin on 25.10.2014.
 */

// класс для работы с БД
public class AutoServiceDataSource {

    private final Context context;

    private AutoServiceSQLiteHepler hepler;
    private SQLiteDatabase sqLiteDatabase;

    public AutoServiceDataSource(Context ctx) {
        context = ctx;
    }

    // открыть подключение
    public void open() {
        hepler = new AutoServiceSQLiteHepler(context);
        sqLiteDatabase = hepler.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (hepler !=null) hepler.close();
    }

    // получить все данные из таблицы
    public Cursor getCars() {
        return sqLiteDatabase.query(DB.TABLE_CARS, null, null, null, null, null, null);
    }

    public void addCar(Car mov) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, mov.getName());
        cv.put(DB.COLUMN_NUMBER,mov.getNumber());
        sqLiteDatabase.insert(DB.TABLE_CARS, null, cv);
    }

    public void deleteCarById(long id) {
        sqLiteDatabase.delete(DB.TABLE_CARS, DB.COLUMN_ID + " = " + id, null);
    }

    public Cursor getEmployee() {
        return sqLiteDatabase.query(DB.TABLE_EMPLOYEES, null, null, null, null, null, null);
    }

    public Cursor getTasks() {
        final String tasks = "select " +
                "* " +
                "from tasks " +
                    "join cars " +
                        "on cars._id = tasks.car_id " +
                    "join employees " +
                        "on employees._id = tasks.employee_id ";

        return sqLiteDatabase.rawQuery( tasks,null );

    }

    public void addEmployee(Employee employee) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, employee.getName());
        cv.put(DB.COLUMN_POST, employee.getPost());
        sqLiteDatabase.insert(DB.TABLE_EMPLOYEES, null, cv);
    }

    public void deleteEmployeeById(long id) {
        sqLiteDatabase.delete(DB.TABLE_EMPLOYEES, DB.COLUMN_ID + " = " + id, null);
    }

    public void addTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_CAR_ID, task.getCarId());
        cv.put(DB.COLUMN_EMPLOYEE_ID, task.getEmployeeId());
        cv.put(DB.COLUMN_IS_COMPLETE, task.isComplete());
        cv.put(DB.COLUMN_TASK, task.getTask());
        sqLiteDatabase.insert(DB.TABLE_TASKS, null, cv);
    }

    public void updateEmployee(Employee employee) {

        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, employee.getName());
        cv.put(DB.COLUMN_POST, employee.getPost());

        sqLiteDatabase.update(DB.TABLE_EMPLOYEES,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(employee.getId()) });
    }

    public void updateCar(Car car) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_NAME, car.getName());
        cv.put(DB.COLUMN_NUMBER, car.getNumber());

        sqLiteDatabase.update(DB.TABLE_CARS,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(car.getId()) });
    }

    public void updateTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMN_EMPLOYEE_ID, task.getEmployeeId());
        cv.put(DB.COLUMN_CAR_ID, task.getCarId());
        cv.put(DB.COLUMN_IS_COMPLETE, task.isComplete());
        cv.put(DB.COLUMN_TASK, task.getTask());

        sqLiteDatabase.update(DB.TABLE_TASKS,cv, DB.COLUMN_ID
                + " = ?", new String[] {Long.toString(task.getId()) });
    }

    public void deleteTaskById(long id) {
        sqLiteDatabase.delete(DB.TABLE_TASKS, DB.COLUMN_ID + " = " + id, null);
    }
}

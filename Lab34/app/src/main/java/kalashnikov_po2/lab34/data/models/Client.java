package kalashnikov_po2.lab34.data.models;

import android.content.ContentValues;

import kalashnikov_po2.lab34.data.DB;

/**
 * Created by admin on 25.10.2014.
 */
public class Client {
    private long id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;
    private String lastName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public ContentValues getConventValues() {
        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_FIRSTNAME, firstName);
        values.put(DB.COLUMN_LASTNAME, lastName);

        return values;
    }
}

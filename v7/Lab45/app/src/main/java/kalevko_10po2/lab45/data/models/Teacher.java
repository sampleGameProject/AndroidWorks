package kalevko_10po2.lab45.data.models;

/**
 * Created by admin on 27.10.2014.
 */
public class Teacher {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    long id;
    String name;
    String phone;
}

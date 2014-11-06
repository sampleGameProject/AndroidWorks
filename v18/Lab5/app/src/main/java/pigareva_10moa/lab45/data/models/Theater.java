package pigareva_10moa.lab45.data.models;

/**
 * Created by admin on 27.10.2014.
 */
public class Theater {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    long id;
    String name;
    String address;
}

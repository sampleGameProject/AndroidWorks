package pigareva_10moa.lab45.data.models;

/**
 * Created by admin on 25.10.2014.
 */
public class Movie {
    private long id;
    private String name;
    private int length;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

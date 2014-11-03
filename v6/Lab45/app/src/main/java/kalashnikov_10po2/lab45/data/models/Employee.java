package kalashnikov_10po2.lab45.data.models;

/**
 * Created by admin on 27.10.2014.
 */
public class Employee {
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    long id;
    String name;
    String post;
}

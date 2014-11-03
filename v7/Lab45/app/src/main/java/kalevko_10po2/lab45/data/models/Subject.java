package kalevko_10po2.lab45.data.models;

/**
 * Created by admin on 25.10.2014.
 */
public class Subject {
    private long id;
    private String name;

    public boolean isLecture() {
        return lecture;
    }

    public void setLecture(boolean lecture) {
        this.lecture = lecture;
    }

    private boolean lecture;

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


}

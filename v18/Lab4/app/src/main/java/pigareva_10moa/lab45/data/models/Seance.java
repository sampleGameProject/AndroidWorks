package pigareva_10moa.lab45.data.models;

import java.sql.Time;

/**
 * Created by admin on 27.10.2014.
 */
public class Seance {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(long theaterId) {
        this.theaterId = theaterId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIs3D() {
        return is3D;
    }

    public void setIs3D(boolean is3D) {
        this.is3D = is3D;
    }

    private long id, theaterId, movieId;
    private String time;
    private boolean is3D;

}

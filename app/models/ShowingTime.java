package models;

import com.avaje.ebean.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by evan_ on 22/03/2017.
 */
@Entity
@SequenceGenerator(name = "time_gen", allocationSize = 1, initialValue = 1)
public class ShowingTime extends Model {
    @Id
    @JoinColumn(name = "timeId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_gen")
    private String id;

    private String time;

    @ManyToOne()
    @JoinColumn(name = "showingId")
    private Showing showing;


    public static Finder<String, ShowingTime> find = new Finder<String, ShowingTime>(ShowingTime.class);

    public static List<ShowingTime> findMovieShowTimes(String dateIn) {
        return ShowingTime.find.where().like("id", dateIn).findList();
    }

    public static ShowingTime findByTime(String timeIn) {
        return (ShowingTime) ShowingTime.find.where().like("time", timeIn).findList();
    }


    public ShowingTime() {
    }

    public ShowingTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }
}

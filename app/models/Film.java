package models;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
/**
 * Created by evan.
 */
@Entity
public class Film extends Model{
    @OneToMany(mappedBy = "titleId")
    @Id //Primary Key
    private String title;
    private String director;
    private String trailerURL;
    private int duration;
    private String summery;

    public static Finder<String, Film> find = new Finder<String, Film>(Film.class);

    public static List<Film> findAll(){
        return Film.find.all();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSummery() {
        return summery;
    }

    public void setSummery(String summery) {
        this.summery = summery;
    }
}

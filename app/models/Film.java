package models;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
@SequenceGenerator(name = "film_gen", allocationSize=1, initialValue=1)
public class Film extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "film_gen")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "titleId")
    private String filmId;

    @Constraints.Required
    private String title;
    @Constraints.Required
    private String director;
    private String trailerURL;
    @Constraints.Required
    private int duration;
    @Constraints.Required
    private String summery;

    public static Finder<String, Film> find = new Finder<String, Film>(Film.class);

    public static List<Film> findAll(){
        return Film.find.all();
    }

    public static List<Film> search(String title) {
        return Film.find.where()
                .ilike("title", "%" + title + "%")
                .orderBy("title asc")
                .findList();
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

    public String getFilmId() {
        return filmId;
    }
}

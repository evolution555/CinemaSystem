package models;

import java.util.*;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import com.avaje.ebeaninternal.server.lib.util.Str;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Created by evan_ on 20/03/2017.
 */
@Entity
public class Showing extends Model{
    @Id
    private String showingId;
    @ManyToOne()
    private String title;
    private int screen;
    @Formats.DateTime(pattern="dd/mm/yyyy'T'HH:mm:ss")
    private String date;

    @OneToMany (mappedBy = "showing", cascade = CascadeType.ALL)
    private List<ShowingTime> times = new ArrayList<>();

    public Showing(){
    }
    public static Finder<String, Showing> find = new Finder<String, Showing>(Showing.class);


    public static List<Showing> findAll(){
        return Showing.find.all();
    }

    public static List<Showing> findMovieShowings(String movieName){
        return Showing.find.where().like("title", movieName).findList();
    }

    public Showing(int screen, String date, String title) {
        this.showingId = genId(title);
        this.title = title;
        this.screen = screen;
        this.date = date;
    }

    public void addShowing(String time){
        ShowingTime s = new ShowingTime(time);
        times.add(s);
        s.setShowing(this);
        this.save();
        s.save();
    }

    public String getShowingId() {
        return showingId;
    }

    public void setShowingId(String id) {
        this.showingId = id;
    }

    public static String genId(String title) {
        String id = title;
        Random rand = new Random();
        int randNum = 0;
        boolean check = true;
            randNum = rand.nextInt((9999 - 1001) + 1) + 1001;
        String numberAsString = Integer.toString(randNum);
        return id + numberAsString;
    }

    public String getTitle(){ return title;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ShowingTime> getTimes() {
        return times;
    }

    public void setTimes(List<ShowingTime> times) {
        this.times = times;
    }
}
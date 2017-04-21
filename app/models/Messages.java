package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Created by Dan on 06/04/2017.
 */
@Entity
public class Messages extends Model {

    @Id
    private String id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String email;
    @Constraints.Required
    private String message;

    public Messages() {
        this.id = genId();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String genId() {
        Random rand = new Random();
        int randNum = rand.nextInt((9999 - 1001) + 1) + 1001;
        String numberAsString = Integer.toString(randNum);
        return numberAsString;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Finder<String, Messages> find = new Finder<String, Messages>(Messages.class);

    public static List<Messages> findAll(){
        return Messages.find.all();
    }



}

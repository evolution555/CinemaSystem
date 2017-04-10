package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Entity
public class Staff extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String jobRole;

    @Constraints.Required
    private String description;

    @Constraints.Required
    private String email;

    @Constraints.Required
    private String number;


    public Staff() {
    }

    public Staff(Long id, String name, String jobRole, String description, String email, String number) {
        this.id = id;
        this.name = name;
        this.jobRole = jobRole;
        this.description = description;
        this.email = email;
        this.number = number;
    }

    public static Finder<Long, Staff> find = new Finder<Long, Staff>(Staff.class);

    public static List<Staff> findAll() {
        return Staff.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
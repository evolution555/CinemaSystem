package models.users;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by evan.
 */
@Entity
public class User extends Model{
    @Id
    private String email;
    private String name;
    private String role;
    private String password;

    public static User create(String email, String name, String role, String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.save();
        return user;
    }

    //Find a specific user
    public static Finder<String, User> find = new Finder<String, User>(User.class);

    //User by id(email)
    public static User getUserById(String id){
        if(id == null)
            return null;
        else
            return find.byId(id);
    }

    //Find all users and return an arraylist
    public static List<User> findAll(){
        return User.find.all();
    }

    public static User authenticate(String email, String password){
        User user = User.find.where().eq("email", email).findUnique();
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }else{
            return null;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

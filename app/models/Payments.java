package models;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import org.mindrot.jbcrypt.BCrypt;
import com.avaje.ebean.*;

/**
 * Created by evan_ on 01/04/2017.
 */
@Entity
public class Payments extends Model{
    @Constraints.Required
    private String name;
    @Id
    @Constraints.Required
    private String cardNumber;
    @Constraints.Required
    private String expMonth;
    @Constraints.Required
    private int expYear;
    @Constraints.Required
    private int cvv2;

    public Payments(String name, String cardNumber, String expMonth, int expYear, int cvv2) {
        this.name = name;
        this.cardNumber =BCrypt.hashpw(cardNumber, BCrypt.gensalt());
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv2 = cvv2;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public int getCvv2() {
        return cvv2;
    }

    public void setCvv2(int cvv2) {
        this.cvv2 = cvv2;
    }
}

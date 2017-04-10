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
public class Booking extends Model {
    @Id //Primary Key
    private int bookingId;
    @ManyToOne()
    private String title;
    private String time;
    private String date;
    private int qty;
    public double total;

    private final double COST = 10.00;

    public Booking() {
    }

    public Booking(int qty, String time, String date, String title) {
        this.bookingId = genId();
        this.qty = qty;
        this.title = title;
        this.time = time;
        this.date = date;
        this.total = calcTotal(qty);
    }

    public int genId() {
        Random rand = new Random();
        int randNum = rand.nextInt((9999 - 1001) + 1) + 1001;
        return randNum;
    }

    public double calcTotal(int qty) {
        double newTotal;
        newTotal = qty * COST;
        return newTotal;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCOST() {
        return COST;
    }
}

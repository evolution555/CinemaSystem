package models;
import java.util.Comparator;

/**
 * Created by evan_ on 24/04/2017.
 */
public class timeComparitor implements Comparator<ShowingTime> {
    public int compare(ShowingTime a, ShowingTime b){
        return a.getTime().compareTo(b.getTime());
    }
}

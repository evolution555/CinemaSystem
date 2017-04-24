package models;
import java.util.Comparator;

/**
 * Created by evan_ on 24/04/2017.
 */
public class dateComparitor implements Comparator<Showing> {
    public int compare(Showing a, Showing b){
        return a.getDate().compareTo(b.getDate());
    }
}

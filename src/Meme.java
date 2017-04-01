import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class Meme {
    private String name;
    private ArrayList<Integer> ratings;
    private String picture;

    public Meme(String name, ArrayList<Integer> relativeRatings, String link) {
        this.name = name;
        ratings = relativeRatings;
        picture = link;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public void adjust(int newRating) {
        ratings.add(newRating);
    }

    public void adjust(Iterable<Integer> newData) {
        for (int i : newData) {
            ratings.add(i);
        }
    }

    public void visualize() {

    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class Meme {
    private String name;
    private String abbrev = "";
    private ArrayList<Integer> ratings;
    private String picture;

    public Meme(String name, ArrayList<Integer> relativeRatings, String link) {
        this.name = name;
        int counter = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != ' ') {
                abbrev += Character.toUpperCase(name.charAt(i));
                counter += 1;
            }

            if (counter == 3) {
                i = name.length();
            }
        }
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

    public String getAbbrev() {
        return abbrev;
    }

    public void visualize() {

    }
}

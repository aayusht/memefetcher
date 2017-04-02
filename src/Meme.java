import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class Meme {
    private String name;
    private String abbrev = "";
    private ArrayList<DatePair> ratings;
    private String picture;

    public Meme(String name, ArrayList<DatePair> relativeRatings, String link) {
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

    public ArrayList<DatePair> getRatings() {
        return ratings;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public void adjust(int newRating, Date date) {
        ratings.add(new DatePair(newRating, date));
    }

    public void adjust(Iterable<DatePair> newData) {
        for (DatePair d : newData) {
            ratings.add(d);
        }
    }

    public String getAbbrev() {
        return abbrev;
    }
}

import java.util.Date;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class DatePair {
    private double score;
    private Date date;

    public DatePair(double score, Date date) {
        this.score = score;
        this.date = date;
    }

    public double getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }
}

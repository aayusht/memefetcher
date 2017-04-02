import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by hp on 4/1/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
;

/**
 * Created by hp on 4/1/2017.
 */
public class Fetcher {
    private static final String MEMES = "http://knowyourmeme.com/categories/meme";
    private static final String SUBMISSIONS_MODIFIER = "?status=submissions";
    private static final int PAGE_COUNT = 10;
    private static final int FIVEYEARS = 60;
    private static final int ONEYEAR = 12;
    private static final int THREEMONTHS = 3;
    private static final int ONEMONTH = 1;
    public static final String REF = "swords";
    public static DatabaseReference ref;

    private static String getStringFromUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null) { a.append(inputLine); }
        in.close();
        //System.out.println(a.toString().substring(100, 200));
        return a.toString();
    }

    private static List<Meme> getMemes(String urlString) throws IOException {
        ArrayList<Meme> result = new ArrayList<>();
        Document document = Jsoup.connect(MEMES).get();
        Element entriesList = document.getElementById("entries_list").child(0).child(0);
        Elements rows = entriesList.children();
        for (Element row : rows) {
            for (Element data : row.children()) {
                //int redditOccurrences
                result.add(new Meme(data.select("h2").select("a").text()));
            }
        }
        return result;
    }

    private static void sendData(List<Meme> memes) throws Exception {
        ref = FirebaseDatabase.getInstance().getReference();
        List<String> names = new LinkedList<>();
        for (Meme meme : memes) { names.add(meme.toString()); }
        while (names.size() > 0) {
            int size = Math.min(4, names.size());
            List<String> subNames = names.subList(0, Math.min(4, names.size()));
            JSONObject data = processGraph(getStringFromUrl(getDataUrl(names.subList(0, size), FIVEYEARS)));
            JSONArray array = data.getJSONArray("rows");
            for (Object date : array) {
                JSONArray subArray = (JSONArray) date;
                if (!subArray.get(3).getClass().getSimpleName().equals("Integer")) { continue; }
                for (int i = 0; i < size; i++) {
                    ref.child(subNames.get(i).replaceAll("[\\.\\#\\$\\[\\]\\/]", "")).child("" + subArray.getJSONObject(0).getBigInteger("v"))
                            .setValue(subArray.getInt(4 * i + 7) * 100 / subArray.getInt(3));
                }
            }
            if (names.size() > 4) names = names.subList(4, names.size());
        }
    }


    public static JSONObject processGraph(String rawSource) {
        int start = rawSource.indexOf("var chartData = ") + 16;
        String text = rawSource.substring(start, rawSource.indexOf(";", start));
        while (text.contains("new Date")) {
            try {
                int first = text.indexOf("new Date(");
                int last = text.indexOf(")");
                String[] date = text.substring(first + 9, last).split(", ");
                date[1] = "" + (Integer.parseInt(date[1]) + 1);
                String inputDate = date[0];
                inputDate += (date[1].length() == 1) ? "0" + date[1] : date[1];
                inputDate += (date[2].length() == 1) ? "0" + date[2] : date[2];
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                text = text.substring(0, first) + inputDate + text.substring(last + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new JSONObject(text);
    }

    private static String getDataUrl(List<String> names, int duration) {
        //return "https://www.google.com/search?" + name.replaceAll("[-+.^:,]'\"","").replace(" ", "+")+ "&as_sitesearch=" + website + "&as_qdr=" + frequency;
        String rest = "";
        for (String s: names) {
            String temp = s.replace(" ","%20");
            temp = temp.replaceAll("\"*\'*","");
            rest += "," + temp;
        }
        return "https://trends.google.com/trends/fetchComponent?date=today%20"+duration+"-m&hl=en-US&q="+REF+rest+"&geo=US&cid=TIMESERIES_GRAPH_0&export=5";
    }

    private static String getUrl(int page) {
        return MEMES + "/page/" + page;
    }

    public static void main(String[] args) {
        try {
            FileInputStream serviceAccount = new FileInputStream("memedatabase-3d38e-firebase-adminsdk-n87oy-0d41bdc059.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://memedatabase-3d38e.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Meme> memes = new ArrayList<>();
            for (int i = 0; i < PAGE_COUNT; i++) {
                memes.addAll(getMemes(getUrl(i)));
                memes.addAll(getMemes(getUrl(i) + SUBMISSIONS_MODIFIER));
            }
<<<<<<< HEAD
            sendData(memes);
=======
            sendData(memes.get(0).name);
>>>>>>> 7c8801e39ddd03b4890a6bdabd28607db6e7fd70
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Meme {
        String name;
        Meme(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
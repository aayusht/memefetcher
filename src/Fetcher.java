import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hp on 4/1/2017.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
;

/**
 * Created by hp on 4/1/2017.
 */
public class Fetcher {
    private static final String MEMES = "http://knowyourmeme.com/categories/meme";
    private static final String SUBMISSIONS_MODIFIER = "?status=submissions";
    private static final int PAGE_COUNT = 5;
    private static final int FIVEYEARS = 60;
    private static final int ONEYEAR = 12;
    private static final int THREEMONTHS = 3;
    private static final int ONEMONTH = 1;
    public static final String REF = "swords";

//    private static String getStringFromUrl(String urlString) throws Exception{
//        URL url = new URL(urlString);
//        URLConnection yc = url.openConnection();
//        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
//        String inputLine;
//        StringBuilder a = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) { a.append(inputLine); }
//        in.close();
//        //System.out.println(a.toString().substring(100, 200));
//        return a.toString();
//    }

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

    private static void sendData(List<String> names, DatabaseReference root) throws IOException {
        if (names.size() > 4) {
            sendData(names.subList(0,4), root);
            names = names.subList(4,names.size());
        }
        Document document = Jsoup.connect(getDataUrl(names,FIVEYEARS)).get();
        Elements tableElements = document.select("table");

        Elements headerElems = tableElements.select("thead tr th");
        Elements rowElems = tableElements.select("tbody tr");
        for (int i = 1; i < headerElems.size(); i++) {
            String head = headerElems.get(i).text();
            for (Element r: rowElems) {
                root.child(head).child(r.select("td").get(0).text()).setValue(r.select("td").get(i));
            }
        }
    }
    public static void sendData(List<String> names) throws IOException {
        sendData(names, FirebaseDatabase.getInstance().getReference());
    }
    public static void sendData(String name) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(name);
        sendData(temp);
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
            sendData(memes.get(0).name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Meme {
        String name;
        int redditOccurrences;
        Meme(String name) {
            this.name = name;
//            try {
//                Document document = Jsoup.parse(getStringFromUrl("https://trends.google.com/trends/fetchComponent?date=today%2060-m&hl=en-US&q=html5,jquery&cid=TIMESERIES_GRAPH_0&export=5"));
//                //String text = document.getElementById("resultStats").ownText();
//                //this.redditOccurrences = Integer.parseInt(text.replaceAll("[^\\d]", ""));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public String toString() {
            return name + " " + redditOccurrences;
        }
    }
}
package keSt93.springmoviedb.utils;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

public class ImdbApi {

    private String APIKEY = "92857ebb";
    private String INITIAL_DATAURL = "http://www.omdbapi.com/?apikey=" + APIKEY + "&";
    private String INITIAL_POSTERURL = "http://img.omdbapi.com/?apikey=" + APIKEY + "&";

    private JSONObject jsonObject;
    private String title;
    private String coverUrl;
    private String imdbRating;
    private String imdbID;
    private String released;
    private String plot;
    private int length;


    public ImdbApi(String movieName) {
        try {
            URL apiUrl = new URL(INITIAL_DATAURL + "t=" + URLEncoder.encode(movieName, "UTF-8"));
            Scanner scan = new Scanner(apiUrl.openStream());

            String str = "";
            while (scan.hasNext()) {
                str += scan.nextLine();
            }
            scan.close();
            jsonObject = new JSONObject(str);

            // JSONObject title = json.getJSONArray("title").getJSONObject(0);
            title = jsonObject.getString("Title");
            coverUrl = jsonObject.getString("Poster");
            imdbRating = jsonObject.getString("imdbRating");
            imdbID = jsonObject.getString("imdbID");
            released = jsonObject.getString("Released");
            plot = jsonObject.getString("Plot");
            length = Integer.parseInt(jsonObject.getString("Runtime").split(" ")[0]);


        } catch (Exception x) {
            System.out.print(x.toString());
        }
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getReleased() {
        return released;
    }

    public String getPlot() {
        return plot;
    }

    public String getImdbID() {
        return imdbID;
    }

    public int getLength() {
        return length;
    }
}

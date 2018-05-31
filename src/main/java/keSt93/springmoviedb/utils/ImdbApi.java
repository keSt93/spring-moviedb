package keSt93.springmoviedb.utils;


import keSt93.springmoviedb.entities.Movie;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public final class ImdbApi {

    private static String APIKEY = "92857ebb";
    private static String INITIAL_DATAURL = "http://www.omdbapi.com/?apikey=" + APIKEY + "&";


    public static Movie getMovieByImdbId(String id) {
        Movie movie = new Movie();
        JSONObject json;

        try {
            URL apiSearchUrl = new URL(INITIAL_DATAURL + "i=" + URLEncoder.encode(id, "UTF-8"));
            Scanner scan = new Scanner(apiSearchUrl.openStream());

            String str = "";
            while (scan.hasNext()) {
                str += scan.nextLine();
            }
            scan.close();
            json = new JSONObject(str);

            movie.setTitle(json.getString("Title"));
            movie.setPlot(json.getString("Plot"));
            movie.setLength(Integer.parseInt(json.getString("Runtime").split(" ")[0]));
            movie.setCoverImage(DataUriHelper.getDataURIForURL(new URL(json.getString("Poster"))).toString());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date releasedate = formatter.parse(json.getString("Released").replace(" ", "-"));
            movie.setReleaseDate(releasedate);

        } catch (Exception x) {
            x.printStackTrace();
        }
        return movie;
    }

    // use this only as fallback, its not really accurate since i'm not asking for a releasedate
    public static Movie getMovieByName(String movieName) {
        Movie movie = new Movie();

        try {
            URL apiUrl = new URL(INITIAL_DATAURL + "t=" + URLEncoder.encode(movieName, "UTF-8"));
            Scanner scan = new Scanner(apiUrl.openStream());

            String str = "";
            while (scan.hasNext()) {
                str += scan.nextLine();
            }
            scan.close();
            JSONObject json = new JSONObject(str);

            movie.setTitle(json.getString("Title"));
            movie.setPlot(json.getString("Plot"));
            movie.setLength(Integer.parseInt(json.getString("Runtime").split(" ")[0]));
            movie.setCoverImage(DataUriHelper.getDataURIForURL(new URL(json.getString("Poster"))).toString());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date releasedate = formatter.parse(json.getString("Released").replace(" ", "-"));
            movie.setReleaseDate(releasedate);


        } catch (Exception x) {
            System.out.print(x.toString());
        }
        return movie;
    }

}

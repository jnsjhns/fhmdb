package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MovieApi {
    private static final OkHttpClient client = new OkHttpClient();
    private final static String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";


    public static String constructUrl (String query, Genre genre, String releaseYear, String ratingFrom) {
        StringBuilder url = new StringBuilder(baseUrl);

        if ( (query != null && !query.isEmpty()) ||
                genre != null || releaseYear != null || ratingFrom != null) {

            url.append("?");

            // check all parameters and add them to the url
            if (genre != null) {
                url.append("genre=").append(genre).append("&");
            }
            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append("&");
            }
            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append("&");
            }
            if (ratingFrom != null) {
                url.append("rating=").append(ratingFrom).append("&");
            }
        }

        return url.toString();
    }


    public static List<Movie> getAllMovies() {
        return getAllMovies(null, null, null, null);
    }

    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom) {
        String url = constructUrl(query, genre, releaseYear, ratingFrom);

        // Erstelle eine GET-Anfrage
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Java HttpClient") // User-Agent setzen
                .build();

        // Sende die Anfrage und erhalte die Antwort
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Request failed with status code: " + response.code());
                return new ArrayList<>(); // Leere Liste zurückgeben bei Fehler
            }

            // JSON-Antwort als String abrufen
            String jsonResponse = response.body().string();

            // JSON in eine Liste von Movie-Objekten umwandeln
            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            Gson gson = new Gson();
            return gson.fromJson(jsonResponse, movieListType);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return new ArrayList<>(); // Leere Liste zurückgeben bei Fehler
        }
    }


}

package at.ac.fhcampuswien.fhmdb.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MovieApi {
    private static final OkHttpClient client = new OkHttpClient();;

    // Methode zum Abrufen aller Filme
    public String getAllMovies() throws Exception {
        String url = "https://prog2.fh-campuswien.ac.at/movies";

        // Erstelle eine GET-Anfrage
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Java HttpClient") // User-Agent setzen
                .build();

        // Sende die Anfrage und erhalte die Antwort
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Request failed: " + response.code());
            }
            return response.body().string(); // JSON-Antwort als String zurückgeben
        }
    }

    // Weitere Methoden für gefilterte Anfragen können hier hinzugefügt werden
}

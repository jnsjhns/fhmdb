package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Movie {
    private String id;
    private String title;
    private String description;
    private List<Genre> genres;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;


    public Movie(String title, String description, List<Genre> genres) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = 0;
        this.imgUrl = "";
        this.lengthInMinutes = 0;
        this.rating = 0;
    }

    public Movie(String id, String title, String description, List<Genre> genres, int releaseYear,
                 String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers,
                 List<String> mainCast, double rating) {
        if(id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", releaseYear=" + releaseYear +
                ", imgUrl='" + imgUrl + '\'' +
                ", lengthInMinutes=" + lengthInMinutes +
                ", directors=" + directors +
                ", writers=" + writers +
                ", mainCast=" + mainCast +
                ", rating=" + String.format("%.1f", rating) +
                '}';
    }

    // getter

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }


    // Dummy-List from ex1
    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Oppenheimer",
                "A biographical film about J. Robert Oppenheimer, the theoretical physicist who led the Manhattan Project to develop the atomic bomb during World War II.",
                Arrays.asList (Genre.BIOGRAPHY, Genre.DRAMA, Genre.HISTORY)));

        movies.add(new Movie("Everything Everywhere All at Once",
                "An aging Chinese immigrant is swept up in an insane adventure, where she alone can save the world by exploring other universes connecting with the lives she could have led.",
                Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY)));

        movies.add(new Movie("CODA",
                "As a CODA (Child of Deaf Adults), Ruby is the only hearing person in her deaf family. When the family's fishing business is threatened, Ruby finds herself torn between pursuing her love of music and her fear of abandoning her parents.",
                Arrays.asList(Genre.DRAMA, Genre.MUSICAL)));

        movies.add(new Movie("Nomadland",
                "A woman in her sixties, after losing everything in the Great Recession, embarks on a journey through the American West, living as a van-dwelling modern-day nomad.",
                List.of(Genre.DRAMA)));

        movies.add(new Movie("Parasite",
                "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
                Arrays.asList(Genre.DRAMA, Genre.THRILLER)));

        movies.add(new Movie("The Whale",
                "A reclusive English teacher living with severe obesity attempts to reconnect with his estranged teenage daughter for one last chance at redemption.",
                List.of(Genre.DRAMA)));

        movies.add(new Movie("The Father",
                "A man refuses all assistance from his daughter as he ages. As he tries to make sense of his changing circumstances, he begins to doubt his loved ones, his own mind and even the fabric of his reality.",
                List.of(Genre.DRAMA)));

        movies.add(new Movie("Joker",
                "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.",
                Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));

        movies.add(new Movie("Bohemian Rhapsody",
                "The story of the legendary British rock band Queen and lead singer Freddie Mercury, leading up to their famous performance at Live Aid (1985).",
                Arrays.asList(Genre.BIOGRAPHY, Genre.DRAMA, Genre.MUSICAL)));

        return movies;
    }




}

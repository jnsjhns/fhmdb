package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

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

    public List<Genre> getGenres() {
        return genres;
    }
}

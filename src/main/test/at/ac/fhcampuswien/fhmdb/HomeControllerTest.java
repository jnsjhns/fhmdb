package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieApi;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }



    @Test
    void construct_url_constructs_correctly() {
        // Given
        String query = "test";
        Genre genre = Genre.ACTION;
        String releaseYear = "2016";
        String ratingFrom = "8";

        // When
        String returnString = MovieApi.constructUrl(query, genre, releaseYear, ratingFrom);

        // Then
        assertEquals("https://prog2.fh-campuswien.ac.at/movies?genre=ACTION&query=test&releaseYear=2016&rating=8&", returnString);
    }


    // Ex1
    @Test
    void ensure_allMovies_and_observableMovies_are_identical_initially() {
        // When
        homeController.initializeStateWithDummyMovies();

        // Then
        assertEquals(homeController.allMovies, homeController.observableMovies,
                "allMovies and observableMovies should be identical after initialization");
    }



    @Test
    void filter_by_search_query_title_match() {
        // Given
        String query = "Oppenheimer";

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("Oppenheimer")));
        assertFalse(result.isEmpty());
    }



    @Test
    void filter_by_empty_genre_string_should_result_in_full_movie_list() {
        // Given
        String selectedGenreString = "";
        String query = "";
        homeController.initializeStateWithDummyMovies();

        // When
        Genre selectedGenre = homeController.parseGenre(selectedGenreString);
        List<Movie> result = homeController.applyFilters(selectedGenre, query, null, null);

        // Then
        assertEquals(homeController.allMovies, result,
                "Filtering with empty genre and query should return all movies");
    }



    @Test
    void filter_by_search_query_title_match_case_insensitive() {
        // Given
        String query = "oppenHeiMer";

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("Oppenheimer")));
        assertFalse(result.isEmpty());
    }



    @Test
    void filter_by_search_query_search_for_and() {
        // Given
        String query = "and";

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertEquals(6, result.size());
    }



    @Test
    void empty_search_query_returns_all_movies() {
        // Given
        String query = "";
        homeController.initializeStateWithDummyMovies();

        // When
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertEquals(homeController.allMovies.size(), result.size());
        assertTrue(result.containsAll(homeController.allMovies));
    }



    @Test
    void filter_by_search_query_and_genre_should_return_correct_results() {
        // Given
        String query = "man";
        Genre genre = Genre.DRAMA;
        homeController.initializeStateWithDummyMovies();

        // When
        List<Movie> result = homeController.applyFilters(genre, query, null, null);

        // Then
        assertEquals(4, result.size());
    }



    @Test
    void toggle_sort_state_should_change_state_correctly() {
        // Given
        homeController.sortState = SortState.NONE;

        // When & Then
        assertEquals(SortState.ASCENDING, homeController.toggleSortState(), "First toggle should result in ASCENDING");

        // When & Then
        assertEquals(SortState.DESCENDING, homeController.toggleSortState(), "Second toggle should result in DESCENDING");

        // When & Then
        assertEquals(SortState.ASCENDING, homeController.toggleSortState(), "Third toggle should result in ASCENDING");
    }



    @Test
    void movie_list_is_sorted_ascending_if_sort_state_is_descending () {
        // Given
        Movie movieA = new Movie("MovieA", "descriptionA", List.of(Genre.ADVENTURE));
        Movie movieB = new Movie("MovieB", "descriptionB", List.of(Genre.HORROR));
        Movie movieC = new Movie("MovieC", "descriptionC", List.of(Genre.ROMANCE));
        homeController.observableMovies.addAll(movieC, movieA, movieB);
        homeController.sortState = SortState.DESCENDING;

        // When
        homeController.handleSortButtonClick();

        // Then
        List<String> expectedTitles = Arrays.asList("MovieA", "MovieB", "MovieC");
        List<String> actualTitles = homeController.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());
        assertEquals(expectedTitles, actualTitles);
    }



    @Test
    void movie_list_is_sorted_descending_if_sort_state_is_ascending () {
        // Given
        Movie movieA = new Movie("MovieA", "descriptionA", List.of(Genre.ADVENTURE));
        Movie movieB = new Movie("MovieB", "descriptionB", List.of(Genre.HORROR));
        Movie movieC = new Movie("MovieC", "descriptionC", List.of(Genre.ROMANCE));
        homeController.observableMovies.addAll(movieC, movieA, movieB);
        homeController.sortState = SortState.ASCENDING;

        // When
        homeController.handleSortButtonClick();

        // Then
        List<String> expectedTitles = Arrays.asList("MovieC", "MovieB", "MovieA");
        List<String> actualTitles = homeController.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());
        assertEquals(expectedTitles, actualTitles);
    }



    @Test
    void sortState_changes_to_ascending_when_initial_state_is_none() {
        // Given
        homeController.sortState = SortState.NONE;
        homeController.observableMovies.addAll(
                new Movie("B Movie", "Description", List.of()),
                new Movie("A Movie", "Description", List.of()),
                new Movie("C Movie", "Description", List.of())
        );

        // When
        homeController.handleSortButtonClick();

        // Then
        assertEquals(SortState.ASCENDING, homeController.sortState, "Sort state should change to ASCENDING");

        List<String> expectedTitles = Arrays.asList("A Movie", "B Movie", "C Movie");
        List<String> actualTitles = homeController.observableMovies.stream().map(Movie::getTitle).toList();
        assertEquals(expectedTitles, actualTitles, "Movies should be sorted in ascending order");
    }



    @Test
    void filter_by_genre_THRILLER_should_lead_to_2_results() {
        // Given
        Genre genre = Genre.THRILLER;

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // Then
        assertEquals(filteredList.size(), 2, "allMovies contains 2 movies with genre THRILLER.");
    }



    @Test
    void filter_by_genre_DRAMA_should_lead_to_2_results() {
        // Given
        Genre genre = Genre.DRAMA;

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // Then
        assertEquals(filteredList.size(), 8, "allMovies contains 8 movies with genre DRAMA.");
    }



    @Test
    void filter_by_genre_WAR_should_lead_to_0_results() {
        // Given
        Genre genre = Genre.WAR;

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // Then
        assertEquals(filteredList.size(), 0, "allMovies contains no movies with genre WAR.");
    }



    @Test
    void filter_by_genre_null_should_return_all_movies() {
        // Given
        Genre genre = null;

        // When
        homeController.initializeStateWithDummyMovies();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // Then
        assertEquals(filteredList.size(), 9);
        assertTrue(filteredList.containsAll(homeController.allMovies), "Filter by null (genre) should return all movies in allMovies");
    }

}
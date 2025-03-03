package at.ac.fhcampuswien.fhmdb;

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
    void ensure_allMovies_and_observableMovies_are_identical_initially() {
        // WHEN
        homeController.initializeState();

        // THEN
        assertEquals(homeController.allMovies, homeController.observableMovies,
                "allMovies and observableMovies should be identical after initialization");
    }

    @Test
    void filter_by_search_query_title_match() {
        // Given
        String query = "Oppenheimer";

        // When
        homeController.initializeState();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("Oppenheimer")));
        assertTrue(!result.isEmpty());
    }


    @Test
    void filter_by_empty_genre_string_should_result_in_full_film_list() {
        // Given
        String selectedGenreString = "";
        String query = "";
        homeController.initializeState();

        // When
        Genre selectedGenre = homeController.parseGenre(selectedGenreString);
        List<Movie> result = homeController.applyFilters(selectedGenre, query);

        // Then
        assertEquals(homeController.allMovies, result,
                "Filtering with empty genre and query should return all movies");
    }



    @Test
    void filter_by_search_query_title_match_case_insensitive() {
        // Given
        String query = "oppenHeiMer";

        // When
        homeController.initializeState();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("Oppenheimer")));
        assertTrue(!result.isEmpty());
    }



    @Test
    void filter_by_search_query_search_for_and() {
        // Given
        String query = "and";

        // When
        homeController.initializeState();
        List<Movie> result = homeController.filterBySearchQuery(query);

        // Then
        assertEquals(6, result.size());
    }



    @Test
    void empty_search_query_returns_all_movies() {
        // Given
        String query = "";
        homeController.initializeState();

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
        homeController.initializeState();

        // When
        List<Movie> result = homeController.applyFilters(genre, query);

        // Then
        assertEquals(4, result.size());
    }


    @Test
    void movie_list_is_sorted_ascending_if_button_displays_desc() {
        // GIVEN
        HomeController controller = new HomeController();
        Movie movieA = new Movie("MovieA", "descriptionA", List.of(Genre.ADVENTURE));
        Movie movieB = new Movie("MovieB", "descriptionB", List.of(Genre.HORROR));
        Movie movieC = new Movie("MovieC", "descriptionC", List.of(Genre.ROMANCE));
        controller.observableMovies.addAll(movieC, movieA, movieB);

        // WHEN
        controller.sortMovies(SortState.ASCENDING);

        // THEN
        List<String> expectedTitles = Arrays.asList("MovieA", "MovieB", "MovieC");
        List<String> actualTitles = controller.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());
        assertEquals(expectedTitles, actualTitles);
    }


    @Test
    void movie_list_is_sorted_descending_if_button_displays_asc() {
        // GIVEN
        HomeController controller = new HomeController();
        Movie movieA = new Movie("MovieA", "descriptionA", List.of(Genre.ADVENTURE));
        Movie movieB = new Movie("MovieB", "descriptionB", List.of(Genre.HORROR));
        Movie movieC = new Movie("MovieC", "descriptionC", List.of(Genre.ROMANCE));
        controller.observableMovies.addAll(movieC, movieA, movieB);

        // WHEN
        controller.sortMovies(SortState.DESCENDING);

        // THEN
        List<String> expectedTitles = Arrays.asList("MovieC", "MovieB", "MovieA");
        List<String> actualTitles = controller.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());
        assertEquals(expectedTitles, actualTitles);
    }

    @Test
    void sortState_changes_to_ascending_when_initial_state_is_none() {
        // GIVEN
        homeController.sortState = SortState.NONE;
        homeController.observableMovies.addAll(
                new Movie("B Movie", "Description", List.of()),
                new Movie("A Movie", "Description", List.of()),
                new Movie("C Movie", "Description", List.of())
        );

        // WHEN
        homeController.handleSortButtonClick();

        // THEN
        assertEquals(SortState.ASCENDING, homeController.sortState, "Sort state should change to ASCENDING");

        List<String> expectedTitles = Arrays.asList("A Movie", "B Movie", "C Movie");
        List<String> actualTitles = homeController.observableMovies.stream().map(Movie::getTitle).toList();
        assertEquals(expectedTitles, actualTitles, "Movies should be sorted in ascending order");
    }


    @Test
    void filter_by_genre_THRILLER_should_lead_to_2_results() {
        // given
        Genre genre = Genre.THRILLER;

        // when
        homeController.initializeState();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // then
        assertEquals(filteredList.size(), 2);
    }

    @Test
    void filter_by_genre_WAR_should_lead_to_0_results() {
        // given
        Genre genre = Genre.WAR;

        // when
        homeController.initializeState();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // then
        assertEquals(filteredList.size(), 0);
    }

    @Test
    void filter_by_genre_null_should_lead_to_9_results() {
        // given
        Genre genre = null;

        // when
        homeController.initializeState();
        List<Movie> filteredList = homeController.filterByGenre(homeController.allMovies, genre);

        // then
        assertEquals(filteredList.size(), 9);
    }




}


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
        // GIVEN

        // WHEN
        homeController.initializeState();

        // THEN
        assertEquals(homeController.allMovies.size(), homeController.observableMovies.size(),
                "Die Größen von allMovies und observableMovies sollten gleich sein");

        for (int i = 0; i < homeController.allMovies.size(); i++) {
            assertEquals(homeController.allMovies.get(i), homeController.observableMovies.get(i),
                    "Film an Index " + i + " sollte in beiden Listen gleich sein");
        }

        assertTrue(homeController.allMovies.containsAll(homeController.observableMovies) &&
                        homeController.observableMovies.containsAll(homeController.allMovies),
                "Beide Listen sollten die gleichen Elemente enthalten");
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
        assertTrue(result.size() >= 1);
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
        assertTrue(result.size() >= 1);
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
        homeController.sortState = SortState.NONE; // Setze den initialen Zustand auf NONE
        Movie movieA = new Movie("B Movie", "Description", List.of());
        Movie movieB = new Movie("A Movie", "Description", List.of());
        Movie movieC = new Movie("C Movie", "Description", List.of());
        homeController.observableMovies.addAll(movieB, movieA, movieC);

        // WHEN
        homeController.handleSortButtonClick(); // Rufe die Sortierlogik auf

        // THEN
        assertEquals(SortState.ASCENDING, homeController.sortState,
                "Der Sortierzustand sollte auf ASCENDING wechseln.");

        List<String> actualTitles = homeController.observableMovies.stream()
                .map(Movie::getTitle)
                .toList();
        List<String> expectedTitles = Arrays.asList("A Movie", "B Movie", "C Movie");

        assertEquals(expectedTitles, actualTitles,
                "Die Filme sollten alphabetisch aufsteigend sortiert sein.");
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


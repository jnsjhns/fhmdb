package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    void filter_by_search_query_title_match() {
        // Given
        String query = "Oppenheimer";

        // When
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


}
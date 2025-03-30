package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieApi;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton filterButton;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortButton;

    public List<Movie> allMovies;

    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    protected SortState sortState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeState();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data
        sortButton.setPrefWidth(75);

        // initialize ComboBox with all Genres + "" for selection without filter
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("");
        for (Genre genre : Genre.values()) {
            genreComboBox.getItems().add(genre.toString());
        }

        // releaseYear comboBox
        releaseYearComboBox.setPromptText("Filter by Release Year");
        releaseYearComboBox.getItems().clear();
        releaseYearComboBox.getItems().add("");

        // get all ReleaseYears of allMovies
        Set<Integer> allReleaseYears = getAllReleaseYears(allMovies);

        // sort years
        List<Integer> sortedYears = allReleaseYears.stream()
                .sorted()
                .toList();

        // add sortedYears to ComboBox after empty String
        releaseYearComboBox.getItems().addAll(FXCollections.observableArrayList(sortedYears));


        // rating comboBox
        ratingFromComboBox.setPromptText("Filter by Rating");
        ratingFromComboBox.getItems().clear();
        ratingFromComboBox.getItems().add("");
        Integer[] rating = new Integer[10];
        for (int i = 0; i < rating.length; i++) {
            rating[i] = i;
        }
        ratingFromComboBox.getItems().addAll(rating);
    }


    public void initializeState () {
        // initialize movies and SortState
        allMovies = MovieApi.getAllMovies();
        sortState = SortState.NONE;
        observableMovies.setAll(allMovies);
    }

    public void initializeStateWithDummyMovies () {
        // initialize movies and SortState
        allMovies = Movie.initializeMovies();
        sortState = SortState.NONE;
        observableMovies.setAll(allMovies);
    }

    public Set<Integer> getAllReleaseYears(List<Movie> allMovies) {
        return allMovies.stream()
                .map(Movie::getReleaseYear)
                .filter(year -> year > 0)
                .collect(Collectors.toSet());
    }

    @FXML
    public void onSortButtonClick (ActionEvent event) {
        handleSortButtonClick();
    }

    public SortState toggleSortState() {
        if (sortState == SortState.NONE || sortState == SortState.DESCENDING) {
            sortState = SortState.ASCENDING;
        } else {
            sortState = SortState.DESCENDING;
        }
        return sortState;
    }

    public void updateSortButtonText() {
        if (sortButton != null) {
            sortButton.setText(sortState == SortState.ASCENDING ? "Sort (desc)" : "Sort  (asc)");
        }
    }

    public void handleSortButtonClick() {
        SortState newState = toggleSortState();
        updateSortButtonText();
        sortMovies(newState);
    }


    @FXML
    public void onFilterButtonClick(ActionEvent event) {
        handleFilterButtonClick();
    }

    public void handleFilterButtonClick() {
        String selectedGenreString = getSelectedGenre();
        Genre selectedGenre = parseGenre(selectedGenreString);
        String query = getSearchQuery();
        String releaseYear = getSelectedReleaseYear();
        String rating = getSelectedRating();
        List<Movie> filteredMovies = MovieApi.getAllMovies(query, selectedGenre, releaseYear, rating);
        updateObservableMovies(filteredMovies);
    }

    private String getSelectedGenre() {
        return genreComboBox != null ? (String) genreComboBox.getValue() : null;
    }

    private String getSearchQuery() {
        return searchField != null ? searchField.getText().trim() : "";
    }

    private String getSelectedReleaseYear() {
        if (releaseYearComboBox != null && releaseYearComboBox.getValue() != null) {
            Object value = releaseYearComboBox.getValue();
            if (value instanceof Integer) {
                return String.valueOf(value);
            }
        }
        return null;
    }

    // converts int to String
    private String getSelectedRating() {
        if (ratingFromComboBox != null && ratingFromComboBox.getValue() != null) {
            Object value = ratingFromComboBox.getValue();
            if (value instanceof Integer) {
                return String.valueOf(value);
            }
        }
        return null;
    }

    protected Genre parseGenre(String genreString) {
        return (genreString != null && !genreString.isEmpty()) ? Genre.valueOf(genreString) : null;
    }


    public List<Movie> applyFilters(Genre genre, String query, String releaseYear, String rating) {
        List<Movie> moviesFiltered = filterBySearchQuery(query);
        filterByGenre(moviesFiltered, genre);
        filterByReleaseYear(moviesFiltered, releaseYear);
        filterByRatingFrom(moviesFiltered, rating);
        return moviesFiltered;
    }

    private void updateObservableMovies(List<Movie> movies) {
        observableMovies.setAll(movies);
    }


    public void sortMovies (SortState sortState) {
        if (sortState == SortState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
        } else if (sortState == SortState.DESCENDING){
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
        }
    }

    // function is called after filterByReleaseYear and has the return value as argument
    public List<Movie> filterByRatingFrom(List<Movie> moviesFilteredBySearchQueryAndGenreAndReleaseYear, String rating) {
        List<Movie> moviesFilteredByRatingFrom = new ArrayList<>();
        if (rating != null && !rating.isEmpty()) {
            for (Movie movie : moviesFilteredBySearchQueryAndGenreAndReleaseYear) {
                if (rating.equals(movie.getRating())) {
                    moviesFilteredByRatingFrom.add(movie);
                }
            } return moviesFilteredByRatingFrom;
        } else {
            return moviesFilteredBySearchQueryAndGenreAndReleaseYear;
        }
    }

    // function is called after filterByGenre and has the return value as argument
    public List<Movie> filterByReleaseYear(List<Movie> moviesFilteredBySearchQueryAndGenre, String releaseYear) {
        List<Movie> moviesFilteredByReleaseYear = new ArrayList<>();
        if (releaseYear != null && !releaseYear.isEmpty()) {
            for (Movie movie : moviesFilteredBySearchQueryAndGenre) {
                if (releaseYear.equals(movie.getReleaseYear())) {
                    moviesFilteredByReleaseYear.add(movie);
                }
            } return moviesFilteredByReleaseYear;
        } else {
            return moviesFilteredBySearchQueryAndGenre;
        }
    }

    // function is called after filterBySearchQuery and has the return value as argument
    public List<Movie> filterByGenre(List<Movie> moviesFilteredBySearchQuery, Genre genre) {
        List<Movie> moviesfilteredByGenre = new ArrayList<>();
        if (genre != null) {
            for (Movie movie : moviesFilteredBySearchQuery) {
                if (movie.getGenres().contains(genre)) {
                    moviesfilteredByGenre.add(movie);
                }
            }
            return moviesfilteredByGenre;
        } else {
            return moviesFilteredBySearchQuery;
        }
    }


    public List<Movie> filterBySearchQuery (String query) {
        Set<Movie> filteredMovies = new HashSet<>(); // no duplicates in Sets
        if (query != null && !(query.isEmpty())) {
            for (Movie movie : allMovies) {
                if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredMovies.add(movie);
                }
                if (movie.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            }
            return new ArrayList<>(filteredMovies);
        } else {
            return allMovies;
        }
    }

}
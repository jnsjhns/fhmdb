package at.ac.fhcampuswien.fhmdb;

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

        // initialize ComboBox with all Genres + "" for selection without filter
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("");
        for (Genre genre : Genre.values()) {
            genreComboBox.getItems().add(genre.toString());
        }
    }

    public void initializeState (){
        // initialize movies and SortState
        allMovies = Movie.initializeMovies();
        sortState = SortState.NONE;
        observableMovies.setAll(allMovies);
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
            sortButton.setText(sortState == SortState.ASCENDING ? "Sort (desc)" : "Sort (asc)");
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
        String query = getSearchQuery();
        Genre selectedGenre = parseGenre(selectedGenreString);
        List<Movie> filteredMovies = applyFilters(selectedGenre, query);
        updateObservableMovies(filteredMovies);
    }

    private String getSelectedGenre() {
        return genreComboBox != null ? (String) genreComboBox.getValue() : null;
    }

    private String getSearchQuery() {
        return searchField != null ? searchField.getText().trim() : "";
    }

    protected Genre parseGenre(String genreString) {
        return (genreString != null && !genreString.isEmpty()) ? Genre.valueOf(genreString) : null;
    }

    public List<Movie> applyFilters(Genre genre, String query) {
        List<Movie> moviesFilteredBySearchQuery = filterBySearchQuery(query);
        return filterByGenre(moviesFilteredBySearchQuery, genre);
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
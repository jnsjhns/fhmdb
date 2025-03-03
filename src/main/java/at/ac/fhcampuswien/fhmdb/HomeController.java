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

    public List<Movie> allMovies = Movie.initializeMovies();

    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    protected SortState sortState = SortState.NONE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().add(null);
        genreComboBox.getItems().addAll(Genre.values());






    }

    @FXML
    public void onSortButtonClick (ActionEvent event) {
        if (sortState == SortState.NONE || sortState == SortState.DESCENDING) {
            sortState = SortState.ASCENDING;
            sortButton.setText("Sort (desc)");
        } else {
            sortState = SortState.DESCENDING;
            sortButton.setText("Sort (asc)");
        }
        sortMovies(sortState);
    }


    @FXML
    public void onFilterButtonClick(ActionEvent event) {
        Genre selectedGenre = (Genre) genreComboBox.getValue();
        String query = searchField.getText().trim();

        List<Movie> filteredMovies = filterBySearchQuery(query);
        filteredMovies = filterByGenre(filteredMovies, selectedGenre);

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }


    public void sortMovies (SortState sortState) {
        if (sortState == SortState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
        } else if (sortState == SortState.DESCENDING){
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
        }
    }

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
            List<Movie> moviesFilteredBySearchQuery = new ArrayList<>(filteredMovies);
            return moviesFilteredBySearchQuery;
        } else {
            return allMovies;
        }
    }

}
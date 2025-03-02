package at.ac.fhcampuswien.fhmdb;

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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private SortState sortState = SortState.NONE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here




    }

    @FXML
    public void onSortButtonClick (ActionEvent event) {
        if (sortState == SortState.NONE || sortState == SortState.DESCENDING) {
            sortState = SortState.ASCENDING;
            sortBtn.setText("Sort (desc)"); // Button-Text auf "Sort (desc)" setzen
        } else {
            sortState = SortState.DESCENDING;
            sortBtn.setText("Sort (asc)"); // Button-Text auf "Sort (asc)" setzen
        }
        sortFilms(sortState);
    }

    public void sortFilms (SortState sortState) {
        if (sortState == SortState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle)); // Sortiere aufsteigend nach Titel
        } else if (sortState == SortState.DESCENDING){
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed()); // Sortiere absteigend nach Titel
        }
    }

    /*
    // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
        if(sortBtn.getText().equals("Sort (asc)")) {
            // TODO sort observableMovies ascending
            sortBtn.setText("Sort (desc)");
        } else {
            // TODO sort observableMovies descending
            sortBtn.setText("Sort (asc)");
        }
    });
    */

}
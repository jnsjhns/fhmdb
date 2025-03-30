package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label(); // Label for the movie title
    private final Label detail = new Label(); // Label for movie description
    private final Label runtimeAndRating = new Label(); // Label for runtime and rating
    private final Label genres = new Label(); // Label for movie genres
    private final JFXButton detailButton = new JFXButton("Details"); // Button to toggle details
    private final HBox runtimeRatingGenres = new HBox(runtimeAndRating, genres); // HBox for runtime, rating, and genres
    private final VBox layout = new VBox(title, detail, runtimeRatingGenres, detailButton); // Main layout container
    private boolean collapsedDetails = true; // Tracks whether details are collapsed or expanded

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null); // Clear text if no movie is present
            setGraphic(null); // Clear graphic if no movie is present
        } else {
            this.getStyleClass().add("movie-cell"); // Add style class for the cell

            // Set title and release year
            title.setText(movie.getTitle() + " (" + movie.getReleaseYear() + ")");

            // Set description or fallback text if unavailable
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");

            // Set runtime and rating with thin spaces for formatting
            runtimeAndRating.setText(movie.getLengthInMinutes() + "\u2009min     ★\u2009" + movie.getRating() + "/10");

            // Set genres
            genres.setText("  " + String.join(", ", movie.getGenres().stream().map(Genre::name).toList()));


            // Farbschema for labels
            title.getStyleClass().add("text-yellow");
            runtimeAndRating.getStyleClass().add("text-yellow");
            genres.getStyleClass().add("text-lightgray-italic");
            detail.getStyleClass().add("text-white");

            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), CornerRadii.EMPTY, Insets.EMPTY)));

            // Layout settings for runtime, rating, and genres in a single line (HBox)
            runtimeRatingGenres.setSpacing(10);
            runtimeRatingGenres.setAlignment(Pos.CENTER_LEFT);

            // Layout settings for the main VBox
            title.fontProperty().set(title.getFont().font(20)); // Set font size for the title
            detail.setMaxWidth(this.getScene().getWidth() - 30); // Wrap text in description label
            detail.setWrapText(true);
            layout.setPadding(new Insets(10)); // Add padding around the layout
            layout.spacingProperty().set(10); // Add spacing between elements in the layout
            layout.alignmentProperty().set(Pos.CENTER_LEFT); // Align elements to the left

            // Button styling and fixed width
            detailButton.setStyle("-fx-background-color: #AAAAAA; -fx-text-fill: black;");
            detailButton.setPrefWidth(60); // Fixed width for the button

            // Button click logic to toggle details visibility (including image)
            detailButton.setOnMouseClicked(mouseEvent -> {
                if (collapsedDetails) {
                    layout.getChildren().add(getDetails(movie)); // Add details to the layout
                    collapsedDetails = false; // Update state to expanded
                    detailButton.setText("Hide"); // Change button text to "Hide"
                } else {
                    layout.getChildren().remove(layout.getChildren().size() - 1); // Remove details from the layout (last element)
                    collapsedDetails = true; // Update state to collapsed
                    detailButton.setText("Details"); // Change button text back to "Details"
                }
                setGraphic(layout); // Update the graphic of the cell
            });

            setGraphic(layout); // Set the layout as the graphic for the cell
        }
    }

    private VBox getDetails(Movie movie) {
        VBox details = new VBox(); // Container for additional details

        ImageView imageView = new ImageView(); // Image view for displaying the movie's image
        try {
            Image image = new Image(movie.getImgUrl(), true); // Load image from URL (lazy loading)
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        imageView.setFitHeight(150); // Set fixed height for the image
        imageView.setPreserveRatio(true); // Preserve aspect ratio of the image

        Label directors = new Label("Directors: " + String.join(", ", movie.getDirectors())); // Directors label
        Label writers = new Label("Writers: " + String.join(", ", movie.getWriters())); // Writers label
        Label mainCast = new Label("Main Cast: " + String.join(", ", movie.getMainCast())); // Main cast label

        directors.getStyleClass().add("text-white"); // Style for directors label
        writers.getStyleClass().add("text-white"); // Style for writers label
        mainCast.getStyleClass().add("text-white"); // Style for main cast label

        details.getChildren().add(imageView); // Add image view to container first (above text)
        details.getChildren().add(directors); // Add directors label to container
        details.getChildren().add(writers); // Add writers label to container
        details.getChildren().add(mainCast); // Add main cast label to container

        return details; // Return container with additional details and image
    }
}

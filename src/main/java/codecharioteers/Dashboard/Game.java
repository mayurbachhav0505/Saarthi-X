package codecharioteers.Dashboard;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends VBox {

    // --- Game Configuration ---
    private int grid_size = 4; // Initial grid size (4x4)
    private int level = 1;
    private int sequenceLength = 3; // How many tiles to remember at level 1

    // --- Game State ---
    private List<Tile> sequence = new ArrayList<>();
    private int currentSequenceIndex = 0;
    private boolean playerTurn = false;
    private int score = 0;

    // --- UI Elements ---
    private GridPane gridPane;
    private Label levelLabel;
    private Label scoreLabel;
    private Label messageLabel;
    private Button startButton;

    Stage gamestage;
    Scene gamScene;

    /**
     * The main entry point for the JavaFX application.
     * @param primaryStage The primary stage for this application.
     */
     public StackPane createScene() {
        // --- IMPORTANT ---
        // For the custom font to work, create a 'fonts' folder inside 'src/main/resources'
        // and place the 'Poppins-Regular.ttf' and 'Poppins-Bold.ttf' files there.
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.out.println("Could not load fonts. Using system default.");
        }

        // This BorderPane holds the main game layout
        BorderPane gameLayout = new BorderPane();
        gameLayout.setPadding(new Insets(20));
        gameLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #6DD5FA, #5984a0ff);");

        // --- Top Section: Title and Info ---
        VBox topContainer = new VBox(10);
        topContainer.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Focus Matrix");
        titleLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 40));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.3)));


        HBox infoBox = new HBox(50);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 15; -fx-padding: 10 20;");
        levelLabel = new Label("Level: 1");
        scoreLabel = new Label("Score: 0");
        levelLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        scoreLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        levelLabel.setTextFill(Color.WHITE);
        scoreLabel.setTextFill(Color.WHITE);
        infoBox.getChildren().addAll(levelLabel, scoreLabel);

        topContainer.getChildren().addAll(titleLabel, infoBox);
        gameLayout.setTop(topContainer);
        BorderPane.setAlignment(topContainer, Pos.CENTER);
        BorderPane.setMargin(topContainer, new Insets(10, 0, 20, 0));


        // --- Center Section: Game Grid ---
        StackPane gridContainer = new StackPane();
        gridContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); -fx-background-radius: 20;");
        gridContainer.setPadding(new Insets(20));
        gridPane = createGrid();
        gridContainer.getChildren().add(gridPane);
        gameLayout.setCenter(gridContainer);
        BorderPane.setAlignment(gridContainer, Pos.CENTER);


        // --- Bottom Section: Messages and Controls ---
        VBox bottomContainer = new VBox(15);
        bottomContainer.setAlignment(Pos.CENTER);
        messageLabel = new Label("Click 'Start Level' to begin!");
        messageLabel.setFont(Font.font("Poppins", FontWeight.NORMAL, 18));
        messageLabel.setTextFill(Color.WHITE);

        startButton = new Button("Start Level");
        startButton.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        startButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f857a6, #ff5858);" +
            "-fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12 25;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.5, 0, 2);"
        );
        addHoverEffect(startButton);
        startButton.setOnAction(e -> startGame());

        bottomContainer.getChildren().addAll(messageLabel, startButton);
        gameLayout.setBottom(bottomContainer);
        BorderPane.setMargin(bottomContainer, new Insets(20, 0, 10, 0));

        // --- Create Instructions Overlay ---
        VBox instructionsOverlay = createInstructionsOverlay();

        // --- Create Root StackPane to hold game and overlay ---
        StackPane root = new StackPane();
        root.getChildren().addAll(gameLayout, instructionsOverlay); // Overlay on top
         
        return root;
        // --- Scene and Stage Setup ---
        // Scene scene = new Scene(root, 650, 750);
        // // primaryStage.setTitle("Focus Matrix Game");
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }

    /**
     * Creates the instructions overlay VBox.
     * @return A VBox containing the instructions UI.
     */
    private VBox createInstructionsOverlay() {
        VBox overlay = new VBox(25);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(40));
        overlay.setStyle("-fx-background-color: rgba(0, 40, 60, 0.9);");

        Label instructionsTitle = new Label("How to Play");
        instructionsTitle.setFont(Font.font("Poppins", FontWeight.BOLD, 36));
        instructionsTitle.setTextFill(Color.WHITE);

        VBox rulesBox = new VBox(15);
        rulesBox.setAlignment(Pos.CENTER);
        String[] rules = {
            "1. Watch carefully as the tiles light up in a sequence.",
            "2. Wait for the 'Your turn!' message.",
            "3. Click the tiles in the exact same order.",
            "Complete the sequence to advance!"
        };

        for (String rule : rules) {
            Label ruleLabel = new Label(rule);
            ruleLabel.setFont(Font.font("Poppins", FontWeight.NORMAL, 18));
            ruleLabel.setTextFill(Color.web("#CFD8DC"));
            ruleLabel.setWrapText(true);
            ruleLabel.setTextAlignment(TextAlignment.CENTER);
            rulesBox.getChildren().add(ruleLabel);
        }

        Button gotItButton = new Button("Let's Go!");
        gotItButton.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        gotItButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2ECC71, #28B463);" +
            "-fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12 25;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.5, 0, 2);"
        );
        addHoverEffect(gotItButton);

        // Action to fade out the overlay
        gotItButton.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(400), overlay);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> overlay.setVisible(false));
            ft.play();
        });

        overlay.getChildren().addAll(instructionsTitle, rulesBox, gotItButton);
        return overlay;
    }

    /**
     * Adds hover and press effects to a button for a more tactile feel.
     * @param button The button to apply effects to.
     */
    private void addHoverEffect(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(1.05);
        st.setToY(1.05);
        button.setOnMouseEntered(e -> st.playFromStart());
        
        ScaleTransition stExit = new ScaleTransition(Duration.millis(100), button);
        stExit.setToX(1.0);
        stExit.setToY(1.0);
        button.setOnMouseExited(e -> stExit.playFromStart());

        button.setOnMousePressed(e -> button.setScaleX(0.95));
        button.setOnMouseReleased(e -> button.setScaleX(1.05));
    }


    /**
     * Creates and configures the grid of tiles.
     * @return A GridPane populated with Tile objects.
     */
    private GridPane createGrid() {
        GridPane gPane = new GridPane();
        gPane.setAlignment(Pos.CENTER);
        gPane.setHgap(12);
        gPane.setVgap(12);

        for (int row = 0; row < grid_size; row++) {
            for (int col = 0; col < grid_size; col++) {
                Tile tile = new Tile();
                tile.setOnMouseClicked(e -> handleTileClick(tile));
                gPane.add(tile, col, row);
            }
        }
        return gPane;
    }

    /**
     * Starts a new level or the first game.
     */
    private void startGame() {
        playerTurn = false;
        currentSequenceIndex = 0;
        sequence.clear();
        startButton.setDisable(true);
        updateMessage("Watch carefully...");

        // Reset all tiles to default state
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Tile) {
                ((Tile) node).setState(TileState.DEFAULT, true);
            }
        }

        // Generate a random sequence of tiles to highlight
        List<Tile> allTiles = new ArrayList<>();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Tile) {
                allTiles.add((Tile) node);
            }
        }
        Collections.shuffle(allTiles);
        for (int i = 0; i < Math.min(sequenceLength, allTiles.size()); i++) {
            sequence.add(allTiles.get(i));
        }

        // Play the highlight animation sequence
        playSequenceAnimation();
    }

    /**
     * Animates the sequence of tiles for the player to memorize.
     */
    private void playSequenceAnimation() {
        SequentialTransition sequenceAnimation = new SequentialTransition();
        
        double flashSpeed = Math.max(0.1, 0.5 - (level * 0.04));

        for (Tile tile : sequence) {
            PauseTransition preFlashDelay = new PauseTransition(Duration.seconds(0.3));
            preFlashDelay.setOnFinished(e -> {
                tile.pulse(); // Add a pulse animation
                tile.setState(TileState.HIGHLIGHT, false);
            });
            
            PauseTransition flashDuration = new PauseTransition(Duration.seconds(flashSpeed));
            flashDuration.setOnFinished(e -> tile.setState(TileState.DEFAULT, false));
            
            sequenceAnimation.getChildren().addAll(preFlashDelay, flashDuration);
        }

        sequenceAnimation.setOnFinished(e -> {
            playerTurn = true;
            updateMessage("Your turn! Repeat the pattern.");
        });

        sequenceAnimation.play();
    }
    
    /**
     * Updates the message label with a fade-in transition.
     * @param text The new message to display.
     */
    private void updateMessage(String text) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), messageLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        messageLabel.setText(text);
        ft.play();
    }


    /**
     * Handles the logic when a player clicks on a tile.
     * @param tile The Tile object that was clicked.
     */
    private void handleTileClick(Tile tile) {
        if (!playerTurn || tile.isLocked()) return;
        
        tile.lock(); // Prevent double-clicking

        // Check if the clicked tile is the correct one in the sequence
        if (currentSequenceIndex < sequence.size() && tile == sequence.get(currentSequenceIndex)) {
            tile.setState(TileState.CORRECT, false);
            score += 10 * level;
            updateLabels();
            currentSequenceIndex++;

            // Check if the player has completed the sequence
            if (currentSequenceIndex == sequence.size()) {
                playerTurn = false;
                updateMessage("Correct! Well done!");
                
                PauseTransition nextLevelDelay = new PauseTransition(Duration.seconds(1.5));
                nextLevelDelay.setOnFinished(e -> nextLevel());
                nextLevelDelay.play();
            }
        } else {
            // Player made a mistake
            tile.setState(TileState.INCORRECT, false);
            playerTurn = false;
            updateMessage("Incorrect! Click 'Start Over' to try again.");
            startButton.setText("Start Over");
            startButton.setDisable(false);
            level = 1; // Reset progress
            score = 0;
            sequenceLength = 3;
            updateLabels();
        }
    }

    /**
     * Sets up the game for the next level.
     */
    private void nextLevel() {
        level++;
        // Increase difficulty
        if (level % 2 != 0 && sequenceLength < grid_size * grid_size) {
            sequenceLength++;
        }
        updateLabels();
        startButton.setText("Start Level " + level);
        startButton.setDisable(false);
        updateMessage("Ready for the next challenge?");
    }

    /**
     * Updates the score and level labels on the UI.
     */
    private void updateLabels() {
        levelLabel.setText("Level: " + level);
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Enum to manage the visual state of a tile.
     */
    private enum TileState {
        DEFAULT, HIGHLIGHT, CORRECT, INCORRECT
    }

    /**
     * A custom StackPane representing a single tile in the grid with enhanced visuals.
     */
    private static class Tile extends StackPane {
        private Rectangle rect;
        private boolean locked = false;

        private static final Color DEFAULT_COLOR = Color.web("#FFFFFF", 0.5);
        private static final Color HIGHLIGHT_COLOR = Color.web("#F1C40F");
        private static final Color CORRECT_COLOR = Color.web("#2ECC71");
        private static final Color INCORRECT_COLOR = Color.web("#E74C3C");

        public Tile() {
            rect = new Rectangle(90, 90);
            rect.setArcWidth(20);
            rect.setArcHeight(20);
            
            DropShadow shadow = new DropShadow();
            shadow.setRadius(5.0);
            shadow.setOffsetX(3.0);
            shadow.setOffsetY(3.0);
            shadow.setColor(Color.rgb(0, 0, 0, 0.2));
            this.setEffect(shadow);

            setState(TileState.DEFAULT, true);
            this.getChildren().add(rect);

            // Add hover effect
            ScaleTransition st = new ScaleTransition(Duration.millis(100), this);
            st.setToX(1.08);
            st.setToY(1.08);
            this.setOnMouseEntered(e -> {
                if (!locked) st.playFromStart();
            });

            ScaleTransition stExit = new ScaleTransition(Duration.millis(100), this);
            stExit.setToX(1.0);
            stExit.setToY(1.0);
            this.setOnMouseExited(e -> stExit.playFromStart());
        }
        
        public void pulse() {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), this);
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
        }

        public void setState(TileState state, boolean instant) {
            Color targetColor;
            switch (state) {
                case HIGHLIGHT: targetColor = HIGHLIGHT_COLOR; break;
                case CORRECT: targetColor = CORRECT_COLOR; break;
                case INCORRECT: targetColor = INCORRECT_COLOR; break;
                case DEFAULT:
                default:
                    targetColor = DEFAULT_COLOR;
                    this.locked = false; // Unlock on reset
                    break;
            }
            
            if (instant) {
                rect.setFill(targetColor);
            } else {
                FillTransition ft = new FillTransition(Duration.millis(200), rect, (Color) rect.getFill(), targetColor);
                ft.play();
            }
        }
        
        public void lock() { this.locked = true; }
        public boolean isLocked() { return this.locked; }
    }

    public void setStage(Stage stage) {
        this.gamestage=stage;
   
    }
    public void setScene(Scene scene){
        this.gamScene= scene;
    }

    
}

package codecharioteers.Dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Journal extends BorderPane {

    private enum Mood {
        HAPPY("😊"), NEUTRAL("😐"), SAD("😢");

        private final String icon;

        Mood(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }
    }

    private static final Path JOURNAL_DIR = Paths.get(System.getProperty("user.home"), "GratitudeJournalEntries");
    private static final DateTimeFormatter FILENAME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DatePicker datePicker;
    private TextArea journalArea;
    private Label statusLabel;
    private ToggleGroup moodGroup;
    private TextField tagsField;
    private TextField searchField;
    private ListView<String> searchResultsList;
    private FlowPane moodVisualizer;

    public BorderPane createScene() {
        createJournalDirectory();

        // Load custom fonts (optional, but can greatly enhance UI)
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load custom fonts. Using default system fonts. Error: " + e.getMessage());
        }

        BorderPane root = new BorderPane();
        
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda); " + // Darker blue-grey
                                                                                               // gradient
                "-fx-padding: 25px; " +
                "-fx-font-family: 'Roboto', 'Segoe UI', sans-serif;");

        Label headerLabel = new Label(" Gratitude Journal ");
        headerLabel.setStyle("-fx-font-size: 38px; -fx-font-weight: bold; " +
                "-fx-text-fill: linear-gradient(to right, #4a148c, #880e4f); " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.0, 0, 2);");
        HBox headerBox = new HBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15, 0, 30, 0));
        root.setTop(headerBox);

        VBox leftPanel = createLeftPanel();
        root.setCenter(leftPanel);
        BorderPane.setMargin(leftPanel, new Insets(0, 25, 0, 0));

        VBox rightPanel = createRightPanel();
        root.setRight(rightPanel);

       
        loadJournalEntry();
        updateMoodVisualizer();

        return root;
    }

    private VBox createLeftPanel() {
        GridPane controlsGrid = new GridPane();
        controlsGrid.setHgap(20);
        controlsGrid.setVgap(20);
        controlsGrid.setPadding(new Insets(10, 0, 10, 0));

        Label dateLabel = new Label("Select Date:");
        dateLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setOnAction(event -> loadJournalEntry());

        Label moodLabel = new Label("How was your mood?");
        moodLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        moodGroup = new ToggleGroup();
        ToggleButton happyButton = createMoodButton(Mood.HAPPY, "#e6f4ea", "#4CAF50");
        ToggleButton neutralButton = createMoodButton(Mood.NEUTRAL, "#fff3e0", "#FFC107");
        ToggleButton sadButton = createMoodButton(Mood.SAD, "#ffebee", "#F44336");
        HBox moodControls = new HBox(15, happyButton, neutralButton, sadButton);

        Label tagsLabel = new Label("Tags (e.g., work, family):");
        tagsLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        tagsField = new TextField();
        tagsField.setPromptText("Add comma-separated tags here...");

        controlsGrid.add(dateLabel, 0, 0);
        controlsGrid.add(datePicker, 1, 0);
        controlsGrid.add(moodLabel, 0, 1);
        controlsGrid.add(moodControls, 1, 1);
        controlsGrid.add(tagsLabel, 0, 2);
        controlsGrid.add(tagsField, 1, 2);
        GridPane.setHgrow(tagsField, Priority.ALWAYS);

        journalArea = new TextArea();
        journalArea.setPromptText("What are you grateful for today? Write your thoughts here...");
        journalArea.setWrapText(true);
        journalArea.setPrefRowCount(15);
        VBox.setVgrow(journalArea, Priority.ALWAYS);

        Button saveButton = new Button("✍ Save Entry");
        String saveButtonStyle = "-fx-background-color: #673ab7; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 10px; " +
                "-fx-padding: 12px 25px; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.0, 0, 2);";
        String saveButtonHoverStyle = "-fx-background-color: #5e35b1; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 10px; " +
                "-fx-padding: 12px 25px; -fx-cursor: hand;";
        saveButton.setStyle(saveButtonStyle);
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(saveButtonHoverStyle));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(saveButtonStyle));
        saveButton.setOnAction(event -> saveJournalEntry());

        HBox saveBox = new HBox(saveButton);
        saveBox.setAlignment(Pos.CENTER_RIGHT);
        saveBox.setPadding(new Insets(10, 0, 0, 0));

        statusLabel = new Label("Welcome to your Gratitude Journal!");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px 0 0 0; -fx-text-fill: #555555;");

        String textInputStyle = "-fx-font-size: 14px; -fx-background-color: #F8F9FA; " +
                "-fx-border-color: #CED4DA; -fx-border-width: 1px; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-padding: 8px 10px;";
        String textInputFocusedStyle = "-fx-font-size: 14px; -fx-background-color: #FFFFFF; " +
                "-fx-border-color: #673ab7; -fx-border-width: 2px; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-padding: 8px 10px;";

        datePicker.setStyle(textInputStyle);
        tagsField.setStyle(textInputStyle);
        journalArea.setStyle(textInputStyle);

        datePicker.focusedProperty().addListener(
                (obs, oldVal, newVal) -> datePicker.setStyle(newVal ? textInputFocusedStyle : textInputStyle));
        tagsField.focusedProperty().addListener(
                (obs, oldVal, newVal) -> tagsField.setStyle(newVal ? textInputFocusedStyle : textInputStyle));
        journalArea.focusedProperty().addListener(
                (obs, oldVal, newVal) -> journalArea.setStyle(newVal ? textInputFocusedStyle : textInputStyle));

        VBox leftPanel = new VBox(25, controlsGrid, journalArea, saveBox, statusLabel);
        leftPanel.setStyle("-fx-background-color: white; -fx-padding: 30px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.3, 0, 8);");

        return leftPanel;
    }

    private ToggleButton createMoodButton(Mood mood, String selectedBgColor, String selectedBorderColor) {
        ToggleButton button = new ToggleButton(mood.getIcon());
        button.setUserData(mood);
        button.setToggleGroup(moodGroup);
        Tooltip.install(button, new Tooltip(mood.name()));

        String baseStyle = "-fx-font-size: 22px; -fx-padding: 8px 15px; " +
                "-fx-background-radius: 10px; -fx-border-radius: 10px; " +
                "-fx-border-width: 2px; -fx-cursor: hand; -fx-border-color: #CFD8DC; " +
                "-fx-background-color: #F5F7FA;";
        String hoverStyle = "-fx-font-size: 22px; -fx-padding: 8px 15px; " +
                "-fx-background-radius: 10px; -fx-border-radius: 10px; " +
                "-fx-border-width: 2px; -fx-cursor: hand; -fx-border-color: #B0BEC5; " +
                "-fx-background-color: #F0F2F5;";
        String selectedStyle = String.format("-fx-font-size: 22px; -fx-padding: 8px 15px; " +
                "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 3px; " +
                "-fx-cursor: hand; -fx-border-color: %s; -fx-background-color: %s;",
                selectedBorderColor, selectedBgColor);

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> {
            if (!button.isSelected())
                button.setStyle(hoverStyle);
        });
        button.setOnMouseExited(e -> {
            if (!button.isSelected())
                button.setStyle(baseStyle);
        });

        button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected)
                button.setStyle(selectedStyle);
            else
                button.setStyle(baseStyle);
        });

        return button;
    }

    private VBox createRightPanel() {
        Label searchLabel = new Label("🔍 Find Entries by Tag");
        searchLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; " +
                "-fx-text-fill: #3F51B5;");

        searchField = new TextField();
        searchField.setPromptText("Enter tag to search...");
        String searchInputStyle = "-fx-font-size: 14px; -fx-background-color: #F8F9FA; " +
                "-fx-border-color: #CED4DA; -fx-border-width: 1px; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-padding: 8px 10px;";
        String searchInputFocusedStyle = "-fx-font-size: 14px; -fx-background-color: #FFFFFF; " +
                "-fx-border-color: #3F51B5; -fx-border-width: 2px; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-padding: 8px 10px;";
        searchField.setStyle(searchInputStyle);
        searchField.focusedProperty().addListener(
                (obs, oldVal, newVal) -> searchField.setStyle(newVal ? searchInputFocusedStyle : searchInputStyle));

        Button searchButton = new Button("Search");
        String searchButtonStyle = "-fx-font-size: 15px; -fx-background-color: #3F51B5; " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10px; " +
                "-fx-padding: 10px 20px; -fx-cursor: hand;";
        String searchButtonHoverStyle = "-fx-font-size: 15px; -fx-background-color: #303F9F; " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10px; " +
                "-fx-padding: 10px 20px; -fx-cursor: hand;";
        searchButton.setStyle(searchButtonStyle);
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(searchButtonHoverStyle));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(searchButtonStyle));
        searchButton.setOnAction(event -> searchEntriesByTag());

        HBox searchBox = new HBox(10, searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchResultsList = new ListView<>();
        searchResultsList.setPlaceholder(new Label("No results to display."));
        searchResultsList.setStyle("-fx-border-color: #CED4DA; -fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-font-size: 14px;");
        searchResultsList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-padding: 10px; -fx-background-color: white; -fx-text-fill: #333333; " +
                            "-fx-border-color: #F0F2F5; -fx-border-width: 0 0 1px 0;");
                    if (isSelected()) {
                        setStyle("-fx-padding: 10px; -fx-background-color: #E8EAF6; -fx-text-fill: #3F51B5; " +
                                "-fx-font-weight: bold; -fx-border-color: #3F51B5; -fx-border-width: 0 0 1px 0;");
                    }
                }
            }
        });

        searchResultsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                LocalDate date = LocalDate.parse(newVal, FILENAME_FORMATTER);
                datePicker.setValue(date);
            }
        });

        VBox.setVgrow(searchResultsList, Priority.ALWAYS);

        Label vizLabel = new Label("📊 Mood Calendar");
        vizLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #3F51B5;");
        moodVisualizer = new FlowPane(6, 6);
        moodVisualizer.setAlignment(Pos.CENTER_LEFT);
        moodVisualizer.setPadding(new Insets(5, 0, 0, 0));

        VBox rightPanel = new VBox(25, searchLabel, searchBox, searchResultsList, vizLabel, moodVisualizer);
        rightPanel.setStyle("-fx-background-color: white; -fx-padding: 30px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.3, 0, 8);");
        rightPanel.setPrefWidth(380);

        return rightPanel;
    }

    private void saveJournalEntry() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            updateStatus("Please select a date before saving.", true);
            return;
        }

        Toggle selectedMoodToggle = moodGroup.getSelectedToggle();
        if (selectedMoodToggle == null) {
            updateStatus("Please select a mood before saving.", true);
            return;
        }

        Mood mood = (Mood) selectedMoodToggle.getUserData();
        String tags = tagsField.getText().trim();
        String content = journalArea.getText().trim();

        if (content.isEmpty()) {
            updateStatus("Journal entry cannot be empty. Please write something!", true);
            return;
        }

        List<String> lines = Arrays.asList(mood.name(), tags, content);

        try {
            Files.write(getFilePathForDate(date), lines);
            updateStatus("Entry for " + date.format(FILENAME_FORMATTER) + " saved successfully! 😊", false);
            updateMoodVisualizer();
        } catch (IOException e) {
            updateStatus("Error: Failed to save entry. " + e.getMessage(), true);
        }
    }

    private void loadJournalEntry() {
        LocalDate date = datePicker.getValue();
        if (date == null)
            return;

        Path filePath = getFilePathForDate(date);
        clearInputFields();

        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath);
                if (lines.size() >= 3) {
                    Mood mood = Mood.valueOf(lines.get(0));
                    for (Toggle toggle : moodGroup.getToggles()) {
                        if (toggle.getUserData() == mood) {
                            toggle.setSelected(true);
                            break;
                        }
                    }
                    tagsField.setText(lines.get(1));
                    String content = lines.stream().skip(2).collect(Collectors.joining("\n"));
                    journalArea.setText(content);
                    updateStatus("Loaded entry for " + date.format(FILENAME_FORMATTER) + ".", false);
                } else {
                    updateStatus(
                            "Could not parse entry for " + date.format(FILENAME_FORMATTER) + ". File might be corrupt.",
                            true);
                }
            } catch (Exception e) {
                updateStatus(
                        "Error: Failed to read entry for " + date.format(FILENAME_FORMATTER) + ". " + e.getMessage(),
                        true);
            }
        } else {
            updateStatus("No entry found for " + date.format(FILENAME_FORMATTER) + ". Time to write a new one!", false);
        }
    }

    private void searchEntriesByTag() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            updateStatus("Please enter a tag to search for, e.g., 'work' or 'family'.", true);
            return;
        }
        searchResultsList.getItems().clear();

        try (Stream<Path> paths = Files.list(JOURNAL_DIR)) {
            List<String> results = paths.filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            if (lines.size() > 1) {
                                String[] tags = lines.get(1).toLowerCase().split(",");
                                return Arrays.stream(tags).anyMatch(tag -> tag.trim().equals(searchTerm));
                            }
                        } catch (IOException e) {
                            System.err.println("Could not read file during search: " + path);
                        }
                        return false;
                    })
                    .map(path -> path.getFileName().toString().replace(".txt", ""))
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            searchResultsList.getItems().addAll(results);
            if (results.isEmpty()) {
                updateStatus("No entries found for tag '" + searchTerm + "'. Try a different tag!", false);
            } else {
                updateStatus("Search complete. Found " + results.size() + " entries for tag '" + searchTerm + "'.",
                        false);
            }
        } catch (IOException e) {
            updateStatus("Error: Could not access journal directory for search.", true);
        }
    }

    private void updateMoodVisualizer() {
        moodVisualizer.getChildren().clear();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 42; i++) {
            LocalDate date = today.minusDays(i);
            Path filePath = getFilePathForDate(date);

            Rectangle daySquare = new Rectangle(28, 28);
            daySquare.setArcWidth(10);
            daySquare.setArcHeight(10);
            daySquare.setStyle("-fx-stroke: #CFD8DC; -fx-stroke-width: 1.5px; -fx-cursor: hand;");

            String tooltipText = date.format(FILENAME_FORMATTER) + " - No Entry";
            Color fillColor = Color.web("#ECEFF1");

            if (Files.exists(filePath)) {
                try {
                    Optional<String> moodLine = Files.lines(filePath).findFirst();
                    if (moodLine.isPresent()) {
                        Mood mood = Mood.valueOf(moodLine.get());
                        tooltipText = date.format(FILENAME_FORMATTER) + " - " + mood.name();
                        switch (mood) {
                            case HAPPY:
                                fillColor = Color.web("#66BB6A");
                                break;
                            case NEUTRAL:
                                fillColor = Color.web("#FFCA28");
                                break;
                            case SAD:
                                fillColor = Color.web("#EF5350");
                                break;
                        }
                    }
                } catch (IOException ignored) {
                    System.err.println("Error reading mood for date " + date.format(FILENAME_FORMATTER) + ": "
                            + ignored.getMessage());
                }
            }

            daySquare.setFill(fillColor);
            Tooltip.install(daySquare, new Tooltip(tooltipText));
            daySquare.setOnMouseClicked(e -> datePicker.setValue(date));

            moodVisualizer.getChildren().add(daySquare);
        }
    }

    private void clearInputFields() {
        moodGroup.selectToggle(null);
        tagsField.clear();
        journalArea.clear();
    }

    private Path getFilePathForDate(LocalDate date) {
        return JOURNAL_DIR.resolve(date.format(FILENAME_FORMATTER) + ".txt");
    }

    private void createJournalDirectory() {
        try {
            if (!Files.exists(JOURNAL_DIR)) {
                Files.createDirectories(JOURNAL_DIR);
                System.out.println("Journal directory created at: " + JOURNAL_DIR.toAbsolutePath());
            }
        } catch (IOException e) {
            updateStatus("Error: Could not create journal directory. " + e.getMessage(), true);
            System.err.println("Failed to create journal directory: " + e.getMessage());
        }
    }

    private void updateStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isError ? Color.valueOf("#D32F2F") : Color.valueOf("#388E3C"));
        statusLabel.setFont(Font.font("Roboto", 14));
    }

}
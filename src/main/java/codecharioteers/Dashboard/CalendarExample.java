package codecharioteers.Dashboard;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class CalendarExample  extends VBox{

    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthYearLabel;
    private Map<LocalDate, ObservableList<String>> events = new HashMap<>();
    private ListView<String> eventList;
    private LocalDate selectedDate = LocalDate.now();

    
    public VBox createScene() {
       // stage.setTitle("Vibrant Calendar with Notes");
        currentYearMonth = YearMonth.now();

        // Calendar title
        // Label calendarTitle = new Label("Calendar");
        // calendarTitle.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 34));
        // calendarTitle.setTextFill(Color.web("#2C3E50"));
        // calendarTitle.setStyle("-fx-letter-spacing: 1px; -fx-padding: 10 0 0 0;");

        Label calendarTitle = new Label(" Calendar ");
        calendarTitle.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 34));
        calendarTitle.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; " +
                        "-fx-text-fill: linear-gradient(to right, rgba(74,20,140,0.85), rgba(136,14,79,0.85)); " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0.0, 0, 2);");
        calendarTitle.setPadding(new Insets(25,25, 25, 25));


        // Header with navigation
        Button prevMonth = new Button("←");
        Button nextMonth = new Button("→");
        monthYearLabel = new Label();
        monthYearLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        monthYearLabel.setTextFill(Color.web("#283593"));

        HBox header = new HBox(15, prevMonth, monthYearLabel, nextMonth);
        header.setAlignment(Pos.CENTER);

        prevMonth.setOnAction(e -> changeMonth(-1));
        nextMonth.setOnAction(e -> changeMonth(1));

        // Calendar grid
        calendarGrid = new GridPane();
        calendarGrid.setHgap(12);
        calendarGrid.setVgap(12);
        calendarGrid.setAlignment(Pos.CENTER);

        // Event list view inside wrapper
        eventList = new ListView<>();
        eventList.setPrefHeight(100);    // height of white box
        eventList.setMaxWidth(400);      // width matches calendar (7 x 55 + gaps)
        eventList.setPrefWidth(400);

        // Custom cell factory with ✔ and 🗑 buttons
        eventList.setCellFactory(param -> new ListCell<String>() {
            HBox hbox = new HBox();
            Label label = new Label();
            Button doneBtn = new Button("✔");
            Button deleteBtn = new Button("🗑");

            {
                doneBtn.setStyle("-fx-background-color: #81C784; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                deleteBtn.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                doneBtn.setOnAction(e -> {
                    label.setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
                });
                deleteBtn.setOnAction(e -> {
                    getListView().getItems().remove(getItem());
                });

                hbox.setSpacing(10);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(label, doneBtn, deleteBtn);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    label.setStyle("-fx-text-fill: black; -fx-font-weight: normal; -fx-strikethrough: false;");
                    setGraphic(hbox);
                }
            }
        });

        HBox eventListBox = new HBox(eventList);
        eventListBox.setAlignment(Pos.CENTER);

        // Notes label (bold + large)
        Label notesLabel = new Label("Notes for selected day:");
        notesLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        notesLabel.setTextFill(Color.web("#1A237E"));

        // Input area for new notes
        TextField eventInput = new TextField();
        eventInput.setPromptText("Add note for selected date...");
        Button addEventBtn = new Button("Add");
        addEventBtn.setStyle("-fx-background-color: #FF7043; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        addEventBtn.setOnAction(e -> {
            String task = eventInput.getText().trim();
            if (!task.isEmpty()) {
                events.putIfAbsent(selectedDate, FXCollections.observableArrayList());
                events.get(selectedDate).add(task);
                eventList.setItems(events.get(selectedDate));
                eventInput.clear();
                populateCalendar(); // refresh calendar to show event indicators
            }
        });

        HBox addEventBox = new HBox(10, eventInput, addEventBtn);
        addEventBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, calendarTitle, header, calendarGrid, notesLabel, eventListBox, addEventBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Background gradient
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#BBDEFB")), new Stop(1, Color.web("#E1BEE7")) };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        populateCalendar();

        Scene scene = new Scene(root, 540, 650);
       // stage.setScene(scene);
        //stage.show();
        return root;
    }

    private void changeMonth(int increment) {
        currentYearMonth = currentYearMonth.plusMonths(increment);
        populateCalendar();

        FadeTransition fade = new FadeTransition(Duration.millis(400), calendarGrid);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void populateCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());

        // Day headers
        String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            dayLabel.setTextFill(Color.web("#00695C"));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefSize(55, 30);
            calendarGrid.add(dayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int col = firstDayOfWeek;
        int row = 1;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Button dayBtn = new Button(String.valueOf(day));
            dayBtn.setPrefSize(55, 55);

            styleDayButton(dayBtn, date);

            dayBtn.setOnAction(e -> {
                selectedDate = date;
                eventList.setItems(events.getOrDefault(date, FXCollections.observableArrayList()));
            });

            calendarGrid.add(dayBtn, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void styleDayButton(Button btn, LocalDate date) {
        String baseColor = "#D1C4E9";
        String textColor = "#1A237E";

        if (date.equals(LocalDate.now())) {
            baseColor = "#00BCD4"; // teal
            textColor = "white";
        }

        if (events.containsKey(date) && !events.get(date).isEmpty()) {
            baseColor = "#FF7043"; // coral for event day
            textColor = "white";
        }

        String normalStyle = "-fx-background-color: " + baseColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 12;";
        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #7E57C2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12;"));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
    }

   
}
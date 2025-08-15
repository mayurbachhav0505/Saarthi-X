package codecharioteers.Dashboard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class todolist extends VBox {

    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Category { WORK, STUDY, PERSONAL, HEALTH, FINANCE, SOCIAL, OTHER }

    private final ObservableList<Task> tasks = FXCollections.observableArrayList(task -> new Property[]{
            task.titleProperty(), task.completedProperty(), task.priorityProperty(),
            task.categoryProperty(), task.dueDateProperty()
    });

    private final FilteredList<Task> filteredTasks = new FilteredList<>(tasks, t -> true);
    private final SortedList<Task> sortedTasks = new SortedList<>(filteredTasks);

    private final Path dataFile = Paths.get("tasks.ser");

    public VBox createScene() {
        //stage.setTitle("✨ SaarathiX – To-Do List ✨");
        loadTasks();

        // Title label
        Label titleLabel = new Label(" To-Do List ");
        titleLabel.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; " +
                        "-fx-text-fill: linear-gradient(to right, rgba(74,20,140,0.85), rgba(136,14,79,0.85)); " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0.0, 0, 2);"
        );

        // Search and sort controls
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search your tasks...");
        searchField.setStyle("-fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 5 10;");

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Default", "By Due Date", "By Priority");
        sortComboBox.setValue("Default");
        sortComboBox.setOnAction(e -> applySort(sortComboBox.getValue()));

        HBox topControls = new HBox(10, searchField, new Label("Sort by:"), sortComboBox);
        topControls.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, javafx.scene.layout.Priority.ALWAYS);
        topControls.getChildren().stream()
                .filter(node -> node instanceof Label)
                .forEach(node -> node.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;"));

        Label noResultsLabel = new Label("No matching tasks found!");
        noResultsLabel.setTextFill(Color.web("#777"));
        noResultsLabel.setVisible(false);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredTasks.setPredicate(task -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                return task.getTitle().toLowerCase().contains(filter) ||
                        task.getDescription().toLowerCase().contains(filter) ||
                        task.getCategory().toString().toLowerCase().contains(filter) ||
                        task.getPriority().toString().toLowerCase().contains(filter);
            });
            noResultsLabel.setVisible(filteredTasks.isEmpty() && !newVal.isEmpty());
        });

        // Task list
        ListView<Task> taskListView = new ListView<>(sortedTasks);
        taskListView.setCellFactory(lv -> new TaskCell());
        taskListView.setStyle("-fx-background-color: transparent;");

        // Progress bar
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: #42a5f5;");
        updateProgressBar(progressBar);
        tasks.addListener((javafx.collections.ListChangeListener<Task>) c -> {
            updateProgressBar(progressBar);
            applySort(sortComboBox.getValue());
        });

        // Inputs
        TextField taskInput = new TextField();
        taskInput.setPromptText("New task title...");

        TextArea descriptionInput = new TextArea();
        descriptionInput.setPromptText("Optional description...");
        descriptionInput.setPrefRowCount(2);
        descriptionInput.setWrapText(true);

        ComboBox<Category> categoryBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        categoryBox.setValue(Category.PERSONAL);

        ComboBox<Priority> priorityBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
        priorityBox.setValue(Priority.MEDIUM);

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due date");

        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, LocalTime.now().getHour());
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, LocalTime.now().getMinute());
        hourSpinner.setPrefWidth(70);
        minuteSpinner.setPrefWidth(70);

        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #4cafef; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 10 15;");
        addButton.setOnAction(e -> addTask(taskInput, descriptionInput, categoryBox, priorityBox, dueDatePicker, hourSpinner, minuteSpinner));

        GridPane addBox = new GridPane();
        addBox.setHgap(10);
        addBox.setVgap(5);
        addBox.setAlignment(Pos.CENTER);
        addBox.add(taskInput, 0, 0, 2, 1);
        GridPane.setHgrow(taskInput, javafx.scene.layout.Priority.ALWAYS);
        addBox.add(descriptionInput, 0, 1, 2, 1);
        addBox.add(new Label("Category:"), 2, 0);
        addBox.add(categoryBox, 3, 0);
        addBox.add(new Label("Priority:"), 2, 1);
        addBox.add(priorityBox, 3, 1);
        addBox.add(new Label("Due:"), 4, 0);
        addBox.add(dueDatePicker, 5, 0);
        addBox.add(new Label("Time:"), 4, 1);
        addBox.add(new HBox(5, hourSpinner, minuteSpinner), 5, 1);
        addBox.add(addButton, 6, 0, 1, 2);

        addBox.getChildren().filtered(node -> node instanceof Label)
                .forEach(node -> node.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;"));

        // White card container
        VBox card = new VBox(15, topControls, noResultsLabel, taskListView, progressBar, addBox);
        card.setPadding(new Insets(30));
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.3, 0, 8);"
        );
        VBox.setVgrow(taskListView, javafx.scene.layout.Priority.ALWAYS);

        // Root layout
        VBox root = new VBox(20, titleLabel, card);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        // Reminder timeline
        Timeline reminderTimeline = new Timeline(new KeyFrame(Duration.seconds(60), e -> checkReminders()));
        reminderTimeline.setCycleCount(Timeline.INDEFINITE);
        reminderTimeline.play();

        // stage.setOnCloseRequest(event -> {
        //     saveTasks();
        //     reminderTimeline.stop();
        // });

        Scene scene = new Scene(root, 1000, 700);
        //  stage.setScene(scene);
        //  stage.show();

         return root;
    }

    // === Helper methods (sorting, adding, saving, etc.) ===
    private void applySort(String sortType) {
        switch (sortType) {
            case "By Due Date":
                sortedTasks.setComparator(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case "By Priority":
                sortedTasks.setComparator(Comparator.comparing(Task::getPriority));
                break;
            default:
                sortedTasks.setComparator(null);
                break;
        }
    }

    private void updateProgressBar(ProgressBar progressBar) {
        long total = tasks.size();
        if (total == 0) {
            progressBar.setProgress(0);
            return;
        }
        long completed = tasks.stream().filter(Task::isCompleted).count();
        progressBar.setProgress((double) completed / total);
    }

    private void addTask(TextField titleField, TextArea descField, ComboBox<Category> catBox, ComboBox<Priority> prioBox, DatePicker datePicker, Spinner<Integer> hourSpinner, Spinner<Integer> minSpinner) {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Task title cannot be empty!");
            return;
        }
        LocalTime reminderTime = (datePicker.getValue() != null) ? LocalTime.of(hourSpinner.getValue(), minSpinner.getValue()) : null;
        tasks.add(new Task(title, descField.getText().trim(), catBox.getValue(), prioBox.getValue(), datePicker.getValue(), reminderTime));
        titleField.clear();
        descField.clear();
    }

    private void checkReminders() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        tasks.stream()
                .filter(task -> !task.isCompleted() && task.getDueDate() != null && task.getDueDate().equals(LocalDate.now()) && now.equals(task.getReminderTime()))
                .forEach(this::showReminderPopup);
    }

    private void showReminderPopup(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder!");
        alert.setHeaderText("📢 Upcoming Task: " + task.getTitle());
        alert.setContentText(String.format("Category: %s\nPriority: %s", task.getCategory(), task.getPriority()));

        ButtonType snoozeButton = new ButtonType("Snooze (5 min)");
        alert.getButtonTypes().setAll(snoozeButton, ButtonType.OK);

        alert.showAndWait().ifPresent(response -> {
            if (response == snoozeButton) {
                task.setReminderTime(LocalTime.now().plusMinutes(5).withSecond(0).withNano(0));
            } else {
                task.setReminderTime(null);
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataFile))) {
            List<SerializableTask> serializableTasks = tasks.stream().map(SerializableTask::new).collect(Collectors.toList());
            oos.writeObject(serializableTasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private void loadTasks() {
        if (!Files.exists(dataFile)) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(dataFile))) {
            List<SerializableTask> loadedTasks = (List<SerializableTask>) ois.readObject();
            tasks.setAll(loadedTasks.stream().map(Task::new).collect(Collectors.toList()));
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    // === Data classes ===
    static class SerializableTask implements Serializable {
        private static final long serialVersionUID = 1L;
        String title, description;
        Category category;
        Priority priority;
        LocalDate dueDate;
        LocalTime reminderTime;
        boolean completed;

        SerializableTask(Task task) {
            this.title = task.getTitle();
            this.description = task.getDescription();
            this.category = task.getCategory();
            this.priority = task.getPriority();
            this.dueDate = task.getDueDate();
            this.reminderTime = task.getReminderTime();
            this.completed = task.isCompleted();
        }
    }

    static class Task {
        private final StringProperty title;
        private final StringProperty description;
        private final ObjectProperty<Category> category;
        private final ObjectProperty<Priority> priority;
        private final ObjectProperty<LocalDate> dueDate;
        private final ObjectProperty<LocalTime> reminderTime;
        private final BooleanProperty completed;

        Task(String title, String description, Category category, Priority priority, LocalDate dueDate, LocalTime reminderTime) {
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
            this.category = new SimpleObjectProperty<>(category);
            this.priority = new SimpleObjectProperty<>(priority);
            this.dueDate = new SimpleObjectProperty<>(dueDate);
            this.reminderTime = new SimpleObjectProperty<>(reminderTime);
            this.completed = new SimpleBooleanProperty(false);
        }

        Task(SerializableTask sTask) {
            this(sTask.title, sTask.description, sTask.category, sTask.priority, sTask.dueDate, sTask.reminderTime);
            this.completed.set(sTask.completed);
        }

        public String getTitle() { return title.get(); }
        public String getDescription() { return description.get(); }
        public Category getCategory() { return category.get(); }
        public Priority getPriority() { return priority.get(); }
        public LocalDate getDueDate() { return dueDate.get(); }
        public LocalTime getReminderTime() { return reminderTime.get(); }
        public boolean isCompleted() { return completed.get(); }
        public void setReminderTime(LocalTime time) { this.reminderTime.set(time); }

        public StringProperty titleProperty() { return title; }
        public StringProperty descriptionProperty() { return description; }
        public ObjectProperty<Category> categoryProperty() { return category; }
        public ObjectProperty<Priority> priorityProperty() { return priority; }
        public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
        public ObjectProperty<LocalTime> reminderTimeProperty() { return reminderTime; }
        public BooleanProperty completedProperty() { return completed; }
    }

    class TaskCell extends ListCell<Task> {
        private final HBox graphic = new HBox(10);
        private final CheckBox checkBox = new CheckBox();
        private final Label titleLabel = new Label();
        private final Label detailsLabel = new Label();

        public TaskCell() {
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            detailsLabel.setFont(Font.font("Arial", 12));
            detailsLabel.setTextFill(Color.web("#555"));

            Button editButton = new Button("✏");
            editButton.setStyle("-fx-background-color: #4db6ac; -fx-text-fill: white; -fx-background-radius: 5;");
            editButton.setOnAction(e -> {
                if (getItem() != null) {
                    new TaskEditDialog(getItem()).showAndWait();
                }
            });

            Button deleteButton = new Button("🗑");
            deleteButton.setStyle("-fx-background-color: #ef5350; -fx-text-fill: white; -fx-background-radius: 5;");
            deleteButton.setOnAction(e -> {
                if (getItem() != null) {
                    getListView().getItems().remove(getItem());
                }
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            graphic.getChildren().addAll(checkBox, new VBox(3, titleLabel, detailsLabel), spacer, editButton, deleteButton);
            graphic.setAlignment(Pos.CENTER_LEFT);
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setGraphic(null);
                styleProperty().unbind();
                setStyle("");
            } else {
                checkBox.selectedProperty().bindBidirectional(task.completedProperty());
                titleLabel.textProperty().bind(task.titleProperty());
                detailsLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("[%s]", task.getCategory()));
                    if (task.getDueDate() != null) sb.append("  📅 ").append(task.getDueDate().format(dtf));
                    return sb.toString();
                }, task.categoryProperty(), task.dueDateProperty()));

                styleProperty().bind(Bindings.when(task.completedProperty())
                        .then("-fx-background-color: #e0f2f1; -fx-background-radius: 8;")
                        .otherwise(Bindings.createStringBinding(() -> {
                            String color = "#FFFFFF";
                            switch (task.getPriority()) {
                                case HIGH: color = "#ffcdd2"; break;
                                case MEDIUM: color = "#fff9c4"; break;
                                case LOW: color = "#dcedc8"; break;
                            }
                            return "-fx-background-color: " + color + "; -fx-background-radius: 8;";
                        }, task.priorityProperty()))
                );

                titleLabel.styleProperty().bind(Bindings.when(task.completedProperty())
                        .then("-fx-strikethrough: true; -fx-text-fill: #388e3c;")
                        .otherwise("-fx-strikethrough: false; -fx-text-fill: #2c3e50;")
                );

                setGraphic(graphic);
            }
        }
    }

    class TaskEditDialog extends Dialog<Task> {
        public TaskEditDialog(Task task) {
            setTitle("Edit Task");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titleField = new TextField(task.getTitle());
            TextArea descArea = new TextArea(task.getDescription());
            ComboBox<Category> categoryBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
            categoryBox.setValue(task.getCategory());
            ComboBox<Priority> priorityBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
            priorityBox.setValue(task.getPriority());
            DatePicker datePicker = new DatePicker(task.getDueDate());

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descArea, 1, 1);
            grid.add(new Label("Category:"), 0, 2);
            grid.add(categoryBox, 1, 2);
            grid.add(new Label("Priority:"), 0, 3);
            grid.add(priorityBox, 1, 3);
            grid.add(new Label("Due Date:"), 0, 4);
            grid.add(datePicker, 1, 4);

            getDialogPane().setContent(grid);
            getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    task.titleProperty().set(titleField.getText());
                    task.descriptionProperty().set(descArea.getText());
                    task.categoryProperty().set(categoryBox.getValue());
                    task.priorityProperty().set(priorityBox.getValue());
                    task.dueDateProperty().set(datePicker.getValue());
                }
                return null;
            });
        }
    }

   
}
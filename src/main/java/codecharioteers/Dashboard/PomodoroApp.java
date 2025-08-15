package codecharioteers.Dashboard;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PomodoroApp {

    private enum TimerState {
        POMODORO("Pomodoro", 25),
        SHORT_BREAK("Short Break", 5),
        LONG_BREAK("Long Break", 15);

        private final String displayName;
        private final int durationMinutes;

        TimerState(String displayName, int durationMinutes) {
            this.displayName = displayName;
            this.durationMinutes = durationMinutes;
        }

        public int getDurationSeconds() {
            return durationMinutes * 60;
        }
    }

    private Timeline timeline;
    private int timeLeft;
    private TimerState currentState;
    private int pomodoroCycleCount = 0;
    private static final int LONG_BREAK_INTERVAL = 4;

    private final BooleanProperty running = new SimpleBooleanProperty(false);

    private final Label timerLabel = new Label();
    private final Label stateLabel = new Label();
    private final TextField customTimeField = new TextField();
    
    private Button pomodoroBtn, shortBreakBtn, longBreakBtn;
    private Button startBtn, pauseBtn, resetBtn, setCustomBtn;

    Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Modified method to return VBox instead of creating a full scene
    public VBox createPomodoroContent() {
        VBox pomodoroContainer = setupUI();
        setupTimerLogic();
        setupButtonActions();
        bindButtonStates();
        setTimerState(TimerState.POMODORO);
        return pomodoroContainer;
    }

    // Original method kept for backward compatibility if needed
    public BorderPane createScene() {
        VBox content = createPomodoroContent();
        BorderPane root = new BorderPane();
        root.setCenter(content);
        
        if (stage != null) {
            Scene scene = new Scene(root, 550, 620);
            stage.setTitle("Pomodoro Timer");
            stage.setScene(scene);
            stage.show();
        }
        return root;
    }

    private VBox setupUI() {
        // Main container for the Pomodoro content
        VBox mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda); -fx-background-radius: 15;");
        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(20);

        // --- Heading ---
        Label headingLabel = new Label("Pomodoro Timer");
        headingLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 32));
        headingLabel.setTextFill(Color.web("#333D4A"));
        HBox headingContainer = new HBox(headingLabel);
        headingContainer.setAlignment(Pos.CENTER);
        headingContainer.setPadding(new Insets(15, 20, 15, 20));
        headingContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-background-radius: 15;");

        // --- Labels ---
        stateLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 22));
        stateLabel.setTextFill(Color.web("#333D4A"));

        timerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
        timerLabel.setTextFill(Color.web("#212121"));

        // --- Color Palette ---
        String pomodoroColor = "#3F51B5"; // Indigo
        String breakColor = "#00796B";    // Teal
        String startColor = "#43A047";    // Green
        String pauseColor = "#546E7A";    // Blue Grey
        String resetColor = "#D32F2F";    // Red
        String setColor = "#673AB7";      // Deep Purple

        // --- Button Creation ---
        pomodoroBtn = createStyledButton("Pomodoro", pomodoroColor, 140);
        shortBreakBtn = createStyledButton("Short Break", breakColor, 140);
        longBreakBtn = createStyledButton("Long Break", breakColor, 140);
        HBox modeButtons = new HBox(20, pomodoroBtn, shortBreakBtn, longBreakBtn);
        modeButtons.setAlignment(Pos.CENTER);
        
        startBtn = createStyledButton("START", startColor, 100);
        pauseBtn = createStyledButton("PAUSE", pauseColor, 100);
        resetBtn = createStyledButton("RESET", resetColor, 100);
        HBox controlButtons = new HBox(20, startBtn, pauseBtn, resetBtn);
        controlButtons.setAlignment(Pos.CENTER);
        
        customTimeField.setPromptText("Enter minutes");
        customTimeField.setStyle("-fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10; -fx-border-color: #B0BEC5; -fx-border-radius: 20;");
        customTimeField.setMaxWidth(150);
        setCustomBtn = createStyledButton("Set", setColor, 70);
        HBox customInputBox = new HBox(10, customTimeField, setCustomBtn);
        customInputBox.setAlignment(Pos.CENTER);

        // Timer display container
        VBox timerContainer = new VBox(10, stateLabel, timerLabel);
        timerContainer.setAlignment(Pos.CENTER);
        timerContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 20; -fx-padding: 30;");
        
        // Add all components to main container
        mainContainer.getChildren().addAll(
            headingContainer,
            modeButtons,
            timerContainer,
            customInputBox,
            controlButtons
        );
        
        mainContainer.setAlignment(Pos.CENTER);
        return mainContainer;
    }

    private Button createStyledButton(String text, String colorStyle, double width) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + colorStyle + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 30; -fx-border-color: transparent;");
        btn.setPrefWidth(width);
        btn.setEffect(new DropShadow(3, Color.rgb(0,0,0,0.3)));
        
        ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
        btn.setOnMouseEntered(e -> {
            st.setToX(1.1);
            st.setToY(1.1);
            st.playFromStart();
        });
        btn.setOnMouseExited(e -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
        });

        return btn;
    }
    
    private void updateUIForState() {
        String defaultBorderStyle = " -fx-border-width: 0;";
        pomodoroBtn.setStyle(pomodoroBtn.getStyle() + defaultBorderStyle);
        shortBreakBtn.setStyle(shortBreakBtn.getStyle() + defaultBorderStyle);
        longBreakBtn.setStyle(longBreakBtn.getStyle() + defaultBorderStyle);
        
        stateLabel.setText(currentState != null ? currentState.displayName : "Custom Timer");
        
        if (currentState != null) {
            String activeBorderStyle = "-fx-border-color: white; -fx-border-width: 2.5; -fx-border-radius: 30;";
            switch (currentState) {
                case POMODORO: pomodoroBtn.setStyle(pomodoroBtn.getStyle() + activeBorderStyle); break;
                case SHORT_BREAK: shortBreakBtn.setStyle(shortBreakBtn.getStyle() + activeBorderStyle); break;
                case LONG_BREAK: longBreakBtn.setStyle(longBreakBtn.getStyle() + activeBorderStyle); break;
            }
        }
    }

    private void setupTimerLogic() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            updateTimerLabel();
            if (timeLeft <= 0) handleSessionEnd();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupButtonActions() {
        startBtn.setOnAction(e -> startTimer());
        pauseBtn.setOnAction(e -> pauseTimer());
        resetBtn.setOnAction(e -> resetTimer());
        setCustomBtn.setOnAction(e -> setCustomTimer());

        pomodoroBtn.setOnAction(e -> setTimerState(TimerState.POMODORO));
        shortBreakBtn.setOnAction(e -> setTimerState(TimerState.SHORT_BREAK));
        longBreakBtn.setOnAction(e -> setTimerState(TimerState.LONG_BREAK));
    }
    
    private void bindButtonStates() {
        startBtn.disableProperty().bind(running);
        pauseBtn.disableProperty().bind(running.not());
    }
    
    private void setCustomTimer() {
        try {
            int minutes = Integer.parseInt(customTimeField.getText());
            if (minutes <= 0) {
                showInfoAlert("Error", "Please enter a positive number."); return;
            }
            running.set(false);
            timeline.stop();
            currentState = null;
            timeLeft = minutes * 60;
            updateTimerLabel();
            updateUIForState();
            customTimeField.clear();
        } catch (NumberFormatException ex) {
            showInfoAlert("Error", "Invalid input. Please enter numbers only.");
        }
    }

    private void setTimerState(TimerState newState) {
        currentState = newState;
        timeLeft = newState.getDurationSeconds();
        running.set(false);
        timeline.stop();
        updateTimerLabel();
        updateUIForState();
    }
    
    private void startTimer() {
        if (timeLeft > 0) {
            running.set(true);
            timeline.play();
        }
    }

    private void pauseTimer() {
        running.set(false);
        timeline.pause();
    }

    private void resetTimer() {
        setTimerState(TimerState.POMODORO);
    }
    
    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void handleSessionEnd() {
        running.set(false);
        timeline.stop();
        showInfoAlert("Time's Up!", "Your session has finished. Great work!");
        setTimerState(TimerState.POMODORO);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pomodoro");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }
}
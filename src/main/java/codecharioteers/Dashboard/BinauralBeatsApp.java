package codecharioteers.Dashboard;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinauralBeatsApp extends BorderPane {

    // Audio generation parameters
    private static final int SAMPLE_RATE = 44100;
    private static final int SAMPLES_PER_BUFFER = 2048; // Smaller buffer for lower latency feel
    private static final double MAX_AMPLITUDE = 32767.0;

    // Volatile variables for thread-safe access
    private volatile double carrierFrequency = 220.0;
    private volatile double beatFrequency = 10.0;
    private volatile double volume = 0.5;
    private volatile boolean isPlaying = false;

    private Thread audioThread;
    private SourceDataLine sourceDataLine;

    // UI elements
    private Button playPauseButton;
    private Canvas waveformCanvas;
    private double phaseLeft = 0.0;
    private double phaseRight = 0.0;

    
    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    Scene scene;


    public BorderPane createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("linear-gradient(to bottom, #d2e3f0, #bcceda)");
        BorderPane r = setupUI(root);

        // --- IMPORTANT: Removed the redundant Scene creation here ---
        // Scene scene = new Scene(root, 450, 650); // This line is removed

        // The stopPlayback() and Platform.exit() logic usually belongs
        // to the primary stage's close request, not a sub-component.
        // If this component were a standalone app, this would be fine.
        // primaryStage.setOnCloseRequest(event -> {
            stopPlayback();
            // Platform.exit(); // Calling exit here might close the whole Dash app
            // System.exit(0);
        // });
        return r;
    }

    private BorderPane setupUI(BorderPane root) {
        // --- Styling ---
        String accentColor = "#49DEFF"; // Electric Blue/Cyan
        String trackColor = "#334E68"; // Not directly used in the provided CSS, but good for consistency
        String textColor = "#F0F4F8";
        String darkTextColor = "#BCCCDC"; // Not directly used, but good for consistency

        // --- Background Gradient ---
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#102A43")), new Stop(1, Color.web("#0D1B2A"))};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        root.setBackground(new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY)));

        // --- Header ---
        Label headerLabel = new Label("Binaural Beats Generator");
        headerLabel.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; " +
                        "-fx-text-fill: linear-gradient(to right, rgba(74,20,140,0.85), rgba(136,14,79,0.85)); " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0.0, 0, 2);");
    
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.web(textColor));
        headerLabel.setEffect(new DropShadow(10, Color.BLACK));
        HBox headerBox = new HBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(30, 0, 10, 0));
        root.setTop(headerBox);

        // --- Controls GridPane ---
        GridPane controlsGrid = new GridPane();
        controlsGrid.setAlignment(Pos.CENTER);
        controlsGrid.setHgap(15);
        controlsGrid.setVgap(20);

        // Carrier Frequency Slider
        addSliderToGrid(controlsGrid, 0, "Carrier Frequency", "Hz", 100, 1000, carrierFrequency, newVal -> carrierFrequency = newVal, textColor);
        // Beat Frequency Slider
        addSliderToGrid(controlsGrid, 1, "Binaural Beat", "Hz", 1, 30, beatFrequency, newVal -> beatFrequency = newVal, textColor);
        // Volume Slider
        addSliderToGrid(controlsGrid, 2, "Volume", "", 0, 1, volume, newVal -> volume = newVal, textColor);

        // --- Waveform Canvas ---
        waveformCanvas = new Canvas(380, 120);
        startWaveformAnimation();

        VBox centerBox = new VBox(30, controlsGrid, waveformCanvas);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        root.setCenter(centerBox);

        // --- Play/Pause Button ---
        playPauseButton = new Button("▶");
        playPauseButton.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        String buttonStyle = String.format("-fx-background-radius: 50em; -fx-min-width: 80px; -fx-min-height: 80px; -fx-max-width: 80px; -fx-max-height: 80px; -fx-background-color: %s; -fx-text-fill: #102A43;", accentColor);
        playPauseButton.setStyle(buttonStyle);
        playPauseButton.setOnAction(e -> togglePlayback());
        playPauseButton.setEffect(new DropShadow(10, Color.web(accentColor, 0.5)));

        HBox buttonBox = new HBox(playPauseButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 20, 0));
        root.setBottom(buttonBox);
        return root;
    }

    // Helper to create styled sliders and reduce code repetition
    private void addSliderToGrid(GridPane grid, int row, String name, String unit, double min, double max, double initialValue, DoubleParameterSetter setter, String textColor) {
        Label titleLabel = new Label(name);
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(textColor));

        Slider slider = new Slider(min, max, initialValue);

        Label valueLabel = new Label(String.format("%.1f %s", initialValue, unit));
        valueLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        valueLabel.setTextFill(Color.web(textColor));
        valueLabel.setMinWidth(80);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            setter.setValue(newVal.doubleValue());
            valueLabel.setText(String.format("%.1f %s", newVal.doubleValue(), unit));
        });

        grid.add(titleLabel, 0, row);
        grid.add(slider, 1, row);
        grid.add(valueLabel, 2, row);
    }

    // Functional interface for the lambda in addSliderToGrid
    @FunctionalInterface
    interface DoubleParameterSetter {
        void setValue(double value);
    }

    private void startWaveformAnimation() {
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawWaveform(gc);
            }
        };
        timer.start();
    }

    private void drawWaveform(GraphicsContext gc) {
        double width = waveformCanvas.getWidth();
        double height = waveformCanvas.getHeight();

        // Fading background effect
        gc.setFill(Color.rgb(13, 27, 42, 0.1));
        gc.fillRect(0, 0, width, height);

        if (!isPlaying) return;

        double centerY = height / 2.0;

        // Left Channel Wave
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.beginPath();
        for (int x = 0; x < width; x++) {
            double angle = 2 * Math.PI * (carrierFrequency - beatFrequency / 2.0) * ((phaseLeft + x) / SAMPLE_RATE);
            double y = centerY + Math.sin(angle) * (centerY - 10) * volume;
            if (x == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();

        // Right Channel Wave
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(2);
        gc.beginPath();
        for (int x = 0; x < width; x++) {
            double angle = 2 * Math.PI * (carrierFrequency + beatFrequency / 2.0) * ((phaseRight + x) / SAMPLE_RATE);
            double y = centerY + Math.sin(angle) * (centerY - 10) * volume;
            if (x == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();

        // Animate the phase for a scrolling effect
        phaseLeft += 100;
        phaseRight += 100;
    }

    private void togglePlayback() {
        if (isPlaying) {
            stopPlayback();
        } else {
            startPlayback();
        }
    }

    private void startPlayback() {
        if (isPlaying) return;
        isPlaying = true;
        playPauseButton.setText("❚❚"); // Pause Symbol

        audioThread = new Thread(() -> {
            try {
                AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 16, 2, true, false);
                sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
                sourceDataLine.open(audioFormat, SAMPLES_PER_BUFFER * 4);
                sourceDataLine.start();

                byte[] buffer = new byte[SAMPLES_PER_BUFFER * 4];
                double currentPhaseLeft = 0;
                double currentPhaseRight = 0;

                while (isPlaying) {
                    double freqLeft = carrierFrequency - (beatFrequency / 2.0);
                    double freqRight = carrierFrequency + (beatFrequency / 2.0);

                    for (int i = 0; i < SAMPLES_PER_BUFFER; i++) {
                        double angleLeft = currentPhaseLeft * 2 * Math.PI / SAMPLE_RATE;
                        double angleRight = currentPhaseRight * 2 * Math.PI / SAMPLE_RATE;

                        short leftSample = (short) (MAX_AMPLITUDE * volume * Math.sin(angleLeft));
                        short rightSample = (short) (MAX_AMPLITUDE * volume * Math.sin(angleRight));

                        ByteBuffer.wrap(buffer, i * 4, 4).order(ByteOrder.LITTLE_ENDIAN).putShort(leftSample).putShort(rightSample);

                        currentPhaseLeft += freqLeft;
                        currentPhaseRight += freqRight;
                    }
                    sourceDataLine.write(buffer, 0, buffer.length);
                }
            } catch (LineUnavailableException e) {
                System.err.println("Audio line unavailable: " + e.getMessage());
            } finally {
                if (sourceDataLine != null) {
                    sourceDataLine.drain();
                    sourceDataLine.close();
                }
            }
        });
        audioThread.setDaemon(true);
        audioThread.start();
    }

    private void stopPlayback() {
        isPlaying = false;
        playPauseButton.setText("▶"); // Play Symbol
        if (audioThread != null) {
            audioThread.interrupt();
           
            try {
                audioThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
package codecharioteers.Dashboard;



import GeminiService.GeminiService;

import javafx.application.Platform;

import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.stage.Stage;



public class SaarthiX extends VBox {



    private final GeminiService geminiService = new GeminiService();

    private Stage aiStage;

    private Scene aiScene;

    private BorderPane rootPane;



    public BorderPane createScene() {

        // Create a new BorderPane each time

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(10));

        // Apply the specified linear gradient background
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");



        // Create a new TextArea each time

        TextArea chatArea = new TextArea();

        chatArea.setEditable(false);

        chatArea.setWrapText(true);

        chatArea.setPrefRowCount(20);

        chatArea.setStyle(

                "-fx-font-size: 14px; " +

                "-fx-padding: 10px; " +

                "-fx-background-color: white; " +

                "-fx-border-color: #cccccc; " +

                "-fx-border-radius: 5px; " +

                "-fx-background-radius: 5px;"

        );



        // Initial welcome message

        chatArea.setText("SaarthiX: Hello! I'm your AI assistant. How can I help you today?\n\n");



        // Create input area

        HBox inputBox = new HBox(10);

        inputBox.setPadding(new Insets(10));
        
        // Setting the background of the input box to transparent to see the gradient
        inputBox.setStyle("-fx-background-color: transparent;");



        TextField messageField = new TextField();

        messageField.setPromptText("Type your message here...");

        messageField.setPrefWidth(400);

        messageField.setStyle(

                "-fx-font-size: 14px; " +

                "-fx-padding: 8px; " +

                "-fx-border-color: #cccccc; " +

                "-fx-border-radius: 5px; " +

                "-fx-background-radius: 5px;"

        );



        Button sendButton = new Button("Send");

        sendButton.setPrefWidth(80);

        sendButton.setStyle(

                "-fx-background-color: #4CAF50; " +

                "-fx-text-fill: white; " +

                "-fx-font-weight: bold; " +

                "-fx-font-size: 14px; " +

                "-fx-padding: 8px 16px; " +

                "-fx-background-radius: 5px; " +

                "-fx-border-radius: 5px;"

        );



        // Hover effects for send button

        sendButton.setOnMouseEntered(e ->

            sendButton.setStyle(

                    "-fx-background-color: #45a049; " +

                    "-fx-text-fill: white; " +

                    "-fx-font-weight: bold; " +

                    "-fx-font-size: 14px; " +

                    "-fx-padding: 8px 16px; " +

                    "-fx-background-radius: 5px; " +

                    "-fx-border-radius: 5px;"

            )

        );



        sendButton.setOnMouseExited(e ->

            sendButton.setStyle(

                    "-fx-background-color: #4CAF50; " +

                    "-fx-text-fill: white; " +

                    "-fx-font-weight: bold; " +

                    "-fx-font-size: 14px; " +

                    "-fx-padding: 8px 16px; " +

                    "-fx-background-radius: 5px; " +

                    "-fx-border-radius: 5px;"

            )

        );



        inputBox.getChildren().addAll(messageField, sendButton);



        // Add components to BorderPane

        root.setCenter(chatArea);

        root.setBottom(inputBox);



        // Send button action

        sendButton.setOnAction(e -> {

            String message = messageField.getText().trim();

            if (!message.isEmpty()) {

                chatArea.appendText("You: " + message + "\n");

                messageField.clear();

                sendButton.setDisable(true);

                sendButton.setText("Sending...");



                // Create a new thread for API call

                new Thread(() -> {

                    try {

                        String response = geminiService.generateResponse(message);

                        Platform.runLater(() -> {

                            chatArea.appendText("SaarthiX: " + response + "\n\n");

                            // Auto-scroll to bottom

                            chatArea.setScrollTop(Double.MAX_VALUE);

                        });

                    } catch (Exception ex) {

                        Platform.runLater(() -> {

                            chatArea.appendText("SaarthiX: Sorry, I encountered an error: " + ex.getMessage() + "\n\n");

                            chatArea.setScrollTop(Double.MAX_VALUE);

                        });

                    } finally {

                        Platform.runLater(() -> {

                            sendButton.setDisable(false);

                            sendButton.setText("Send");

                            messageField.requestFocus();

                        });

                    }

                }).start();

            }

        });



        // Allow Enter key to send message

        messageField.setOnAction(e -> {

            if (!messageField.getText().trim().isEmpty() && !sendButton.isDisabled()) {

                sendButton.fire();

            }

        });



        // Set initial focus to message field

        Platform.runLater(() -> messageField.requestFocus());



        return root;

    }



    public void setStage(Stage dashStage) {

        this.aiStage = dashStage;

    }



    public void setAiScene(Scene aiScene) {

        this.aiScene = aiScene;

    }

}
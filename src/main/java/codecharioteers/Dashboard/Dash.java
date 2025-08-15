
package codecharioteers.Dashboard;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Dash {
    Stage dashStage;
    Scene dashScene, dashCalendarScene, userProfileSceneDash, signUpScene, gameScene, beatScene;
    BorderPane rootPane;
    VBox quotesBox;
    VBox allTopBox;
    StackPane sp;
    static int indexImage = 0; // for image [] index

    public BorderPane createScene() {

        // quotes array
        String quotesArr[] = {
                "The best way to get started is to quit talking and begin doing.",
                "Success is not in what you have, but who you are.",
                "Opportunities don't happen. You create them.",
                "Don't watch the clock; do what it does. Keep going."
        };

        // background images array
        String bgImage[] = {
                "/Assets/image.png",
                "/Assets/image2.jpg",
                "/Assets/image3.jpeg",
                "/Assets/image1.jpg",
        };

        Text title = new Text("Welcome to Saarthix Dashboard");

        title.setFont(Font.font("Lobster", FontWeight.EXTRA_BOLD, 25)); // Bold + big
        title.setFill(Color.WHITE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setStyle("-fx-effect: dropshadow(one-pass-box, #000000ff, 2, 0.0, 0, 1);");

        Button userProfileButton = new Button("User Profile");
        userProfileButton.setAlignment(Pos.TOP_RIGHT);
        userProfileButton.setVisible(true);
        userProfileButton.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        userProfileButton.setOnMouseEntered(e -> userProfileButton.setStyle(
                "-fx-background-color: #e81de5ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        userProfileButton.setOnMouseExited(e -> userProfileButton.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));

        userProfileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                UserProfile userProfile = new UserProfile(); // Assuming UserProfile is a class that handles user
                                                             // profile logic
                userProfile.setUserProfileStage(dashStage); // Set the stage for the user profile
                VBox userProfileBox = userProfile.creatUserProfileScene(() -> {
                    rootPane.setCenter(allTopBox);
                }); // Create the user profile scene
                rootPane.setCenter(userProfileBox); // Set the user profile scene in the center of the root pane
            }

        });

        // VBox userProfileBox = new VBox(userProfileButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBox = new HBox(); // Title and User Profile Button and Image Change Button
        // they are directly added using topBox.getChildren().addAll(title, spacer,
        // userProfileBox);
        // line 157-158
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 40, 40, 470));
        topBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #232526, #414345);");
        topBox.setSpacing(20);

        Text quoteDayText = new Text(" Quote of the Day");

        quoteDayText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        // quoteDayText.setStyle("-fx-font-size: 28px; -fx-font-family: 'Segoe UI',
        // Arial, sans-serif; -fx-font-weight: bold; -fx-fill: #222;");
        quoteDayText.setTextAlignment(TextAlignment.CENTER);
        quoteDayText.setFont(Font.font("System", FontWeight.BOLD, 20));

        VBox quotesOfDayBox = new VBox(quoteDayText);
        quotesOfDayBox.setAlignment(Pos.CENTER);

        Text quote = new Text();
        quote.setFont(Font.font("System", FontWeight.BOLD, 24));

        quote.setText(quotesArr[(int) (Math.random() * quotesArr.length)]);
        quote.setStyle(
                "-fx-font-size: 20px; -fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-fill: #333; -fx-padding: 5px;");
        quote.setWrappingWidth(600);
 
        quote.setStyle(
                "-fx-font-size: 20px; -fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-fill: #333; -fx-padding: 5px;");
        quote.minHeight(100); 

        quote.setTextAlignment(TextAlignment.CENTER);
        quote.setFont(Font.font("Georgia", FontPosture.ITALIC, 18));

        Button backBtn = new Button("<-");
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        backBtn.setVisible(true);
        backBtn.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px;");
        backBtn.setFont(Font.font("", FontWeight.BOLD, 40));

        Button nextBtn = new Button("->");
        nextBtn.setVisible(true);
        nextBtn.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px;");
        nextBtn.setAlignment(Pos.BOTTOM_RIGHT);
        nextBtn.setFont(Font.font("", FontWeight.BOLD, 40));

        HBox hbbtn = new HBox(100, backBtn, quote, nextBtn);
        hbbtn.setPrefHeight(80); // or any fixed value
        hbbtn.setMinHeight(80);
        hbbtn.setMaxHeight(80);
        hbbtn.setAlignment(Pos.CENTER);

        VBox quoteBox = new VBox(10, quotesOfDayBox, hbbtn);
        // quotesBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);
        // -fx-background-radius: 10;");
        quoteBox.setEffect(new DropShadow(10, Color.GRAY));
        // quoteBox.setOpacity(0.9);
        quoteBox.setStyle("-fx-background-color: rgba(59, 162, 180, 0.3); -fx-background-radius: 20;");
        quoteBox.setEffect(new BoxBlur(10, 0, 0));
        quoteBox.setPrefHeight(200);
        // quoteBox.setPrefWidth(100);
        // quoteBox.setMaxWidth(600);
        // quoteBox.setPrefWidth(300);
        quoteBox.setPadding(new Insets(40, 10, 10, 10));

        quoteBox.setOpacity(100);
        nextBtn.setOnAction(e -> {
            int randomIndex = (int) (Math.random() * quotesArr.length);
            quote.setText(quotesArr[randomIndex]);
        });

        backBtn.setOnAction(e -> {
            int randomIndex = (int) (Math.random() * quotesArr.length);
            quote.setText(quotesArr[randomIndex]);
        });

        String imageUrl = getClass().getResource(bgImage[indexImage]).toExternalForm();
        allTopBox = new VBox(20, topBox, quoteBox);
        allTopBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #67ccffff, #aebeceff);" +
                "-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;");

        Button changeBgBtn = new Button("Change Image"); // image change button
        changeBgBtn.setVisible(true);
        changeBgBtn.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        changeBgBtn.setOnMouseEntered(e -> changeBgBtn.setStyle(
                "-fx-background-color: #e81de5ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        changeBgBtn.setOnMouseExited(e -> changeBgBtn.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));

        changeBgBtn.setVisible(true);
        changeBgBtn.setOnAction(e -> {

            indexImage = (indexImage + 1) % bgImage.length; // Cycle through images
            String newImageUrl = getClass().getResource(bgImage[indexImage]).toExternalForm();

            allTopBox.setStyle("-fx-background-image: url('" + newImageUrl + "');" +
                    "-fx-background-size: cover;" +
                    "-fx-background-repeat: no-repeat;");
        });
        topBox.getChildren().addAll(title, spacer, changeBgBtn, userProfileButton);
        topBox.setSpacing(20);

        Button btn1 = new Button("POMODORO");
        btn1.setPrefSize(150, 50);
        btn1.setVisible(true);
        btn1.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn1.setOnMouseEntered(e -> btn1.setStyle(
                "-fx-background-color: #e81de5ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn1.setOnMouseExited(e -> btn1.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn1.getStyleClass().add("sidebar-button");

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                PomodoroApp pomodoroApp = new PomodoroApp();
                pomodoroApp.setStage(dashStage);
                VBox pomodoroContent = pomodoroApp.createPomodoroContent();
                rootPane.setCenter(pomodoroContent);

            }

        });

        Button btn2 = new Button("TO-DO LIST");
        btn2.setPrefSize(150, 50);
        btn2.setVisible(true);
        btn2.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn2.setPadding(new Insets(10, 10, 10, 10));
        btn2.setOnMouseEntered(e -> btn2.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn2.setOnMouseExited(e -> btn2.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                todolist todoList = new todolist();
                VBox vb = todoList.createScene();
                rootPane.setCenter(vb); // Create and set the To-Do List scene
            }

        });

        Button btn3 = new Button("CALENDAR");
        btn3.setPrefSize(150, 50);
        btn3.setVisible(true);
        btn3.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn3.setOnMouseEntered(e -> btn3.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn3.setOnMouseExited(e -> {
            btn3.setStyle(
                    "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
            btn3.setScaleX(1.0);
            btn3.setScaleY(1.0);
        });

        btn3.setOnAction(e -> {

            CalendarExample cal = new CalendarExample();
            VBox calBox = cal.createScene();
            rootPane.setCenter(calBox);

        });

        Button btn4 = new Button("ABOUT US");
        btn4.setPrefSize(150, 50);
        btn4.setVisible(true);
        btn4.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn4.setOnMouseEntered(e -> btn4.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn4.setOnMouseExited(e -> btn4.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                AboutSaarthiX aboutSaarthiX = new AboutSaarthiX();
                ScrollPane sp = aboutSaarthiX.createScene();
                rootPane.setCenter(sp);

            }
        });

        Button btn5 = new Button("JOURNAL");
        btn5.setPrefSize(150, 50);
        btn5.setVisible(true);
        btn5.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn5.setOnMouseEntered(e -> btn5.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn5.setOnMouseExited(e -> btn5.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                Journal dairyTails = new Journal();
                BorderPane bp = dairyTails.createScene();
                rootPane.setCenter(bp);
            }

        });
        Button btn6 = new Button("BINAURAL BEATS");
        btn6.setPrefSize(150, 50);
        btn6.setVisible(true);
        btn6.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn6.setOnMouseEntered(e -> btn6.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn6.setOnMouseExited(e -> btn6.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));

        btn6.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                BinauralBeatsApp beats = new BinauralBeatsApp();
                beats.setStage(dashStage);
                BorderPane binauralContent = beats.createScene();
                rootPane.setCenter(binauralContent);

            }
        });

        Button btn7 = new Button("GAME");
        btn7.setPrefSize(150, 50);
        btn7.setVisible(true);
        btn7.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30;  -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn7.setOnMouseEntered(e -> btn7.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn7.setOnMouseExited(e -> btn7.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn7.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                Game game = new Game();
                rootPane.setCenter(game.createScene());

            }

        });
        Button btn8 = new Button("DASHBOARD");
        btn8.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                rootPane.setCenter(allTopBox); // Return to the dashboard scene
            }

        });
        btn8.setPrefSize(150, 50);
        btn8.setVisible(true);
        btn8.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2");
        btn8.setOnMouseEntered(e -> btn8.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));
        btn8.setOnMouseExited(e -> btn8.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2"));

        Image appImage = new Image(getClass().getResource("/Assets/app.jpg").toExternalForm());
        ImageView imageView = new ImageView(appImage);

        // Set desired size
        double radius = 60; // half of width/height for circle
        
        imageView.setFitHeight(radius * 2);
        imageView.setFitWidth(radius * 2);
        imageView.setPreserveRatio(false); // Important for a perfect circle

        // Create a circular clip
        Circle clip = new Circle(radius, radius, radius); // centerX, centerY, radius
        imageView.setClip(clip);
        clip.setTranslateY(-10);

        VBox vbSide = new VBox(20, imageView, btn8, btn1, btn2, btn3, btn5, btn6, btn7, btn4); // Side Bar Buttons
        vbSide.setAlignment(Pos.CENTER);
        vbSide.setPrefWidth(230);
        vbSide.setStyle("-fx-background-color: linear-gradient(to bottom right, #232526, #414345);");

        rootPane = new BorderPane();
        rootPane.setLeft(vbSide);
        rootPane.setCenter(allTopBox);

        HBox cardContainer = new HBox(20); // spacing between cards
        cardContainer.setAlignment(Pos.CENTER);

        Region card1 = createCard1("Quote of the Day", "You are what you do, not what you say you'll do.");
        card1.setOnMouseEntered(e -> {
            card1.setScaleX(1.05);
            card1.setScaleY(1.05);
            card1.setStyle(card1.getStyle() + "-fx-cursor: hand;");
        });

        card1.setOnMouseExited(e -> {
            card1.setScaleX(1.0);
            card1.setScaleY(1.0);
        });

        Region card2 = createCard2("Reminder", "Start your Pomodoro cycle now.");
        card2.setOnMouseEntered(e -> {
            card2.setScaleX(1.05);
            card2.setScaleY(1.05);
            card2.setStyle(card2.getStyle() + "-fx-cursor: hand;");
        });

        card2.setOnMouseExited(e -> {
            card2.setScaleX(1.0);
            card2.setScaleY(1.0);
        });

        Region card3 = createCard3("Calendar", "Drink water, take breaks, and plan your tasks.");
        card3.setOnMouseEntered(e -> {
            card3.setScaleX(1.05);
            card3.setScaleY(1.05);
            card3.setStyle(card3.getStyle() + "-fx-cursor: hand;");
        });

        card3.setOnMouseExited(e -> {
            card3.setScaleX(1.0);
            card3.setScaleY(1.0);
        });

        cardContainer.getChildren().addAll(card1, card2, card3);
        cardContainer.setAlignment(Pos.CENTER);

        // BorderPane bp = new BorderPane();
        allTopBox.getChildren().add(cardContainer); // ← display this in the dashboard\
        // allTopBox.setAlignment(Pos.CENTER);

        Button chatBotBtn = new Button("AI ASSISTANT");
        chatBotBtn.setVisible(true);
        chatBotBtn.setAlignment(Pos.BOTTOM_RIGHT);
        chatBotBtn.setPrefSize(150, 50);
        chatBotBtn.setStyle(
                "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2;-fx-alignment: center;");
        chatBotBtn.setOnMouseEntered(e -> chatBotBtn.setStyle(
                "-fx-background-color: #750993ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2;-fx-alignment: center;"));
        chatBotBtn.setOnMouseExited(e -> {
            chatBotBtn.setStyle(
                    "-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #f2f2f2;-fx-alignment: center;");
            chatBotBtn.setScaleX(1.0);
            chatBotBtn.setScaleY(1.0);
        });
        chatBotBtn.setTranslateX(1100);
        chatBotBtn.setTranslateY(150);

        chatBotBtn.setOnAction(e -> {

            SaarthiX ai = new SaarthiX();
            BorderPane aiPane = ai.createScene(); // Now returns BorderPane instead of StackPane
            rootPane.setCenter(aiPane);
        });
        allTopBox.getChildren().add(chatBotBtn);

        dashStage.setTitle("Saarthix Dashboard");

        // dashStage.setScene(scene);
        // dashStage.setFullScreen(true);
        // dashScene=scene;
        // dashStage.show();
        return rootPane;

    }

    protected void initializeSignup() {

        SignUpPage obj = new SignUpPage();
        obj.setStage(dashStage);
        signUpScene = new Scene(obj.createScene(this::handleSign));
        obj.setScene(signUpScene);

    }

    private void handleSign() {
    }

    private Region createCard1(String string, String string2) {

        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.85); " +
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5); " +
                        "-fx-min-width: 300; " +
                        "-fx-max-width: 500; ");

        Label title = new Label("Day & Date");
        title.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #2c3e50;");
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));
        Label body = new Label("🗓 " + formattedDate);
        body.setWrapText(true);
        body.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #555; " +
                        "-fx-text-alignment: center;");

        card.getChildren().addAll(title, body);
        return card;

    }

    private Region createCard2(String titleText, String subtitleText) {
        VBox card2 = new VBox(10);
        card2.setAlignment(Pos.TOP_CENTER);
        card2.setPadding(new Insets(20));
        card2.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; " +
                "-fx-border-color: #dddddd; -fx-border-radius: 15; -fx-min-width: 300;");

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);

        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (int i = 0; i < days.length; i++) {
            Label day = new Label(days[i]);
            day.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            day.setTextFill(Color.DARKBLUE);
            calendarGrid.add(day, i, 0);
        }

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        int startDay = firstDayOfMonth.getDayOfWeek().getValue(); // 1=Mon, 7=Sun

        int col = (startDay % 7); // adjust to 0-indexed column (Sun=0)
        int row = 1;
        YearMonth yearMonth = YearMonth.from(today);
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            Label dayLabel = new Label(String.valueOf(day));

            if (day == today.getDayOfMonth()) {
                dayLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14)); // Bold today
                dayLabel.setTextFill(Color.FIREBRICK);
            } else {
                dayLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
                dayLabel.setTextFill(Color.BLACK);
            }

            calendarGrid.add(dayLabel, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        card2.getChildren().addAll(calendarGrid);
        return card2;
    }

    private Region createCard3(String string, String string2) {
        VBox card3 = new VBox(10);
        card3.setAlignment(Pos.CENTER);
        return card3;
    }

    public void dashSetStage(Stage stage) {

        this.dashStage = stage;

    }

    public void dashSetScene(Scene scene) {
        this.dashScene = scene;
    }

}

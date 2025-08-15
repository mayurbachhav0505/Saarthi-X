package codecharioteers.Dashboard;

import codecharioteers.Controller.AuthController;
//import codecharioteers.Controller.authController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Application {

    private Stage primaryStage;
    private Scene LoginPageScene, SignUpPageScene, DashBoardScene;
    BorderPane root;

       AuthController auth = new AuthController();

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        root = new BorderPane();

        //Logo
        //Image logoImage = new Image("images\\logo.jpg");
        //ImageView logoImageView = new ImageView(logoImage);
        //logoImageView.setFitHeight(200);
        //logoImageView.setPreserveRatio(true);
        //VBox logoBox = new VBox(logoImageView);
        //logoBox.setAlignment(Pos.CENTER);

        //Heading
        Text heading = new Text("Sign In OR Start Fresh with SaarthiX");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        heading.setFill(Color.WHITE);

        //Tagline
        Text tagLine = new Text("Your Mindful Productivity Companion");
        tagLine.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-font-style: italic;");
        tagLine.setFill(Color.WHITE);

        //Email Field
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(300);
        emailField.setPrefSize(500, 40);
        emailField.setFocusTraversable(false);
        emailField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");

        //Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(300);
        passwordField.setPrefSize(500, 40);
        passwordField.setFocusTraversable(false);
        passwordField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");

        //Result Label
        Label resultLabel = new Label();
        resultLabel.setTextFill(Color.WHITE);

        //Login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(200, 40);
        loginButton.setFocusTraversable(false);
        loginButton.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                String email = emailField.getText();
                String password = passwordField.getText();
                AuthController authController= new AuthController();
                boolean success = authController.signInWithEmailAndPassword(email, password);
                resultLabel.setText(success ? "Login Successful" : "Login Failed");
                if (success== true){
                initializeDash();
                primaryStage.setScene(DashBoardScene);
                }
            }
        });
    
        //Sign Up Button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefSize(200, 40);
        signUpButton.setFocusTraversable(false);
        signUpButton.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initializeSignUpPage();
                primaryStage.setScene(SignUpPageScene);
            }
        });

        // Layout
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(80));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #232526, #414345);");
        root.getChildren().addAll(
            //logoBox,
            heading,
            tagLine,
            emailField,
            passwordField,
            loginButton,
            signUpButton,
            resultLabel
        );

        LoginPageScene = new Scene(root, 1200, 800);
        LoginPageScene.setFill(Color.BLACK);

        primaryStage.setScene(LoginPageScene);
        primaryStage.setTitle("Login Page");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    protected void initializeDash() {
        Dash dash = new Dash();
        dash.dashSetStage(primaryStage);
        DashBoardScene= new Scene(dash.createScene());
        dash.dashSetScene(DashBoardScene);
        // primaryStage.setWidth(1200);
        // primaryStage.setHeight(800);
        primaryStage.setFullScreen(true);
    }

    private void initializeSignUpPage() {
        SignUpPage signUpPage = new SignUpPage();
        signUpPage.setStage(primaryStage);
        SignUpPageScene = new Scene(signUpPage.createScene(this::handleBackButton), 1200, 800);
        signUpPage.setScene(SignUpPageScene);
    }

    private void handleBackButton() {
        primaryStage.setScene(LoginPageScene);
    }

   
}

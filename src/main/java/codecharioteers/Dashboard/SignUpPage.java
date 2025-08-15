package codecharioteers.Dashboard;



import codecharioteers.Controller.Signupcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignUpPage {

    Scene signUpPagScene;
    Stage signUpPagStage;
    public static String mainEmail;
    


    public void setScene(Scene scene) {
        this.signUpPagScene=scene;
    }

    public void setStage(Stage stage) {
        this.signUpPagStage=stage;
         signUpPagStage.setWidth(1200);
        signUpPagStage.setHeight(800);
    }


    public static VBox createScene(Runnable back) {

        // Image logoImage = new Image("images\\logo.jpg");
        // ImageView logoImageView = new ImageView(logoImage);
        // logoImageView.setFitHeight(200);
        // logoImageView.setPreserveRatio(true);
        // VBox logoBox = new VBox(logoImageView);
        // logoBox.setAlignment(Pos.CENTER);
        
        Text headText = new Text("Create a New Account");
        headText.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: white; ");
        headText.setFill(Color.WHITE);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter First Name");
        nameField.setFocusTraversable(false);
        nameField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        nameField.setMaxWidth(350);
        nameField.setPrefSize(225, 40);



        TextField surNameField = new TextField();
        surNameField.setPromptText("Enter Last Name");
        surNameField.setFocusTraversable(false);
        surNameField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        surNameField.setMaxWidth(350);
        surNameField.setPrefSize(225, 40);



        HBox hbox=new HBox(50,nameField,surNameField);
        hbox.setAlignment(Pos.CENTER);

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter UserName");
        userNameField.setFocusTraversable(false);
        userNameField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        userNameField.setMaxWidth(500);
        userNameField.setPrefSize(500, 40);



        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setFocusTraversable(false);
        emailField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        emailField.setMaxWidth(500);
        emailField.setPrefSize(500, 40);



        PasswordField passwordField=new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setFocusTraversable(false);
        passwordField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        passwordField.setMaxWidth(500);
        passwordField.setPrefSize(500, 40);


        Button submit=new Button("Submit");
        submit.setStyle(" -fx-font-weight: bold;-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        submit.setFocusTraversable(false);
        submit.setPrefSize(200, 40);

        Label resultLabel=new Label();
        resultLabel.setFocusTraversable(false);
        resultLabel.setTextFill(Color.WHITE);
        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                String name=nameField.getText();
                String surName=surNameField.getText();
                String userName=userNameField.getText();
                String email=emailField.getText();
                mainEmail=email;
                String password=passwordField.getText();
                // boolean success=authController.signUPWithEmailAndPassword(email,password);
                // resultLabel.setText(success?"Signed Up Successfully" : "Sign up Failed");
                Signupcon signUpCon = new Signupcon();
                signUpCon.addSignData(name,surName,userName,email,password);

            }
            
        });

        Button loginButton = new Button(" Back To Login Page");
        loginButton.setStyle(" -fx-font-weight: bold;-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #CCCCCC;");
        loginButton.setFocusTraversable(false);
        loginButton.setPrefSize(200, 40);



        loginButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {                  
                back.run();               
            }            
        });

        // HBox sublogbtn=new HBox(50,SignUp,loginButton);
        // sublogbtn.setAlignment(Pos.CENTER);

    
        VBox vb=new VBox(20,/*logoBox,*/headText,hbox,userNameField,emailField,passwordField,submit,loginButton,resultLabel);
        vb.setStyle("-fx-background-color: linear-gradient(to bottom right, #232526, #414345);");
        vb.setAlignment(Pos.CENTER);

        vb.setPadding(new Insets(20,20,20,20));
        return vb;
    }

        
}




    


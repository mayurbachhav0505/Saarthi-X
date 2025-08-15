package codecharioteers.Dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AboutSaarthiX extends ScrollPane{
    
    public ScrollPane createScene(){
        // Logo
        Image logo = new Image("Assets/app.jpg");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(250);
        logoView.setFitHeight(150);

        // App Name
        Label appName = new Label("SaarthiX");
        appName.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 48));
        appName.setTextFill(Color.web("#4FC3F7"));
        appName.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        // Tagline
        Label tagline = new Label("“Let SaarthiX guide your day, the mindful way!”");
        tagline.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        tagline.setTextFill(Color.LIGHTGRAY);
        tagline.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        // Description Section
        VBox descriptionBox = new VBox(10);
        descriptionBox.setPadding(new Insets(20));
        descriptionBox.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-background-radius: 10;");
        descriptionBox.setMaxWidth(500);

        String[] points = {
                "DASHBOARD : Your daily productivity hub.",
                "POMODORO : Boost focus using time-boxed work sessions.",
                "TO-DO LIST : Organize your tasks with ease.",
                "CALENDAR : Manage your schedule.",
                "BINAURAL BEAT : Relax with focus-enhancing audio.",
                "DAIRY TAILS : Write your daily thoughts.",
                "AI CHATBOT : Your intelligent assistant.",
                "GAME : Quick stress-busting games."


        };

        for (String point : points) {
            Label bullet = new Label("• " + point);
            bullet.setTextFill(Color.WHITE);
            bullet.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            descriptionBox.getChildren().add(bullet);
        }

        // Separator
        Separator sep1 = new Separator();
        sep1.setPrefWidth(500);
        sep1.setStyle("-fx-background-color: white;");

        // Guide Section
        Label guideTitle = new Label("Guide");
        guideTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        guideTitle.setTextFill(Color.WHITE);
        guideTitle.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        Image guideImage = new Image("Assets/sir.jpg");
        ImageView guideView = new ImageView(guideImage);
        guideView.setFitWidth(260);
        guideView.setFitHeight(260);

        Label guideName = new Label("Mr. Shashi Bagal Sir");
        guideName.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        guideName.setTextFill(Color.LIGHTGRAY);
        guideName.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");


        VBox guideSection = new VBox(10, guideTitle, guideView, guideName);
        guideSection.setAlignment(Pos.CENTER);

        Separator sep2 = new Separator();
        sep2.setPrefWidth(500);
        sep2.setStyle("-fx-background-color: white;");

        // Developers Section
        Label devTitle = new Label("Developers");
        devTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        devTitle.setTextFill(Color.WHITE);
        devTitle.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        HBox developerBox = new HBox(30);
        developerBox.setAlignment(Pos.CENTER);

        String[] devNames = {"Vedant Sonar", "Mayur Bachhav", "Rushikesh Talekar"};
        for (String name : devNames) {
            Label devLabel = new Label(name);
            devLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
            devLabel.setTextFill(Color.WHITE);
            devLabel.setAlignment(Pos.CENTER);
            devLabel.setMinSize(180, 50);
            devLabel.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-background-radius: 15;");
            developerBox.getChildren().add(devLabel);
        }

        VBox devSection = new VBox(10, devTitle, developerBox);
        devSection.setAlignment(Pos.CENTER);

        Separator sep3 = new Separator();
        sep3.setPrefWidth(500);
        sep3.setStyle("-fx-background-color: white;");

        // Institute/Partner Section
        Label partnerTitle = new Label("Powered By");
        partnerTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        partnerTitle.setTextFill(Color.WHITE);
        partnerTitle.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        Image coreImage = new Image("Assets/c2w.jpg");
        ImageView coreLogo = new ImageView(coreImage);
        coreLogo.setFitWidth(120);
        coreLogo.setFitHeight(120);

        Label partnerDesc = new Label(
                "Built under the Super-X Program by Core2Web\n" +
                        "— a leading tech upskilling platform empowering future-ready developers."
        );
        partnerDesc.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
        partnerDesc.setTextFill(Color.LIGHTGRAY);
        partnerDesc.setWrapText(true);
        partnerDesc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox partnerSection = new VBox(10, partnerTitle, coreLogo, partnerDesc);
        partnerSection.setAlignment(Pos.CENTER);

        // Main Vertical Layout
        VBox content = new VBox(30,
                logoView,
                appName,
                tagline,
                descriptionBox,
                sep1,
                guideSection,
                sep2,
                devSection,
                sep3,
                partnerSection
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));

        // ScrollPane Wrapper
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        // Scene Setup
        Scene scene = new Scene(scrollPane, 900, 600);
        scene.setFill(Color.web("#1a1a1a"));
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");

        // primaryStage.setTitle("About SaarthiX");
        // primaryStage.setScene(scene);
        // primaryStage.show();
        return scrollPane;
    }
    
}


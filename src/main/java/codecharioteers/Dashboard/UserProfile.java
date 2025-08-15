package codecharioteers.Dashboard;



import codecharioteers.Controller.UserProfileController;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserProfile {
    
      Stage userProfileStage;
      Scene userProfileScene;


      public void setUserProfileStage(Stage userProfileStage) {
          
        this.userProfileStage = userProfileStage;

      }
      public void setUserProfileScene(Scene userProfileScene) {
        
        this.userProfileScene = userProfileScene;
      
    }

    
      public VBox creatUserProfileScene(Runnable back) {
    
        UserProfileController userProfileControllerObj = new UserProfileController();
        userProfileControllerObj.fetchData();
        //userProfileControllerObj.getAge();

        Text userProfileText = new Text("Your Profile");
        Image userProfileImage = new Image("Assets/profileImage.jpg");
             
        ImageView userProfileImageView = new ImageView(userProfileImage);
        userProfileImageView.setFitHeight(150);
        userProfileImageView.setPreserveRatio(true);
        userProfileImageView.setTranslateX(550);
        userProfileImageView.setTranslateY(50);

        Text Name =  new Text("Name:   ");
        Name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text NamePrint = new Text((userProfileControllerObj.getFirstName()+" "+userProfileControllerObj.getLastName()).toUpperCase());
        NamePrint.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox nameBox = new HBox(Name , NamePrint);
        nameBox.setTranslateX(500);
        nameBox.setTranslateY(120);

        //userName Box
        Text userName =  new Text("User Name:   ");
        userName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text userNamePrint = new Text(userProfileControllerObj.getUserName());
        userNamePrint.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox userNameBox = new HBox(userName , userNamePrint);
        userNameBox.setTranslateX(500);
        userNameBox.setTranslateY(120);

        //Age Box
        // Text userAge= new Text("Age:   ");
        // userAge.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // Text userAgePrint= new Text((userProfileControllerObj.getAge()).toString());
        // userAgePrint.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // HBox ageBox = new HBox(userAge,userAgePrint);
        // ageBox.setTranslateX(500);
        // ageBox.setTranslateY(140);

        
        //Email BOx
        Text userEmail = new Text("Email ID: ");
        userEmail.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text userEmailPrint = new Text(userProfileControllerObj.getUserEmail());
        userEmailPrint.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox emailBox = new HBox(userEmail , userEmailPrint);
        emailBox.setTranslateX(500);
        emailBox.setTranslateY(160);

        //Contact Number box
        // Text userNo = new Text("Contact No.: ");
        // userNo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // Text userNoPrint = new Text((userProfileControllerObj.getNum()).toString());
        // userNoPrint.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // HBox numBox= new HBox(userNo,userNoPrint);
        // numBox.setTranslateX(500);
        // numBox.setTranslateY(180);

        //Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setTranslateX(545);
        backButton.setTranslateY(240);
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                back.run();
            }
            
        });
        VBox vbox = new VBox(userProfileImageView,nameBox,userNameBox,emailBox,backButton);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #d2e3f0, #bcceda);");
        return vbox;
     
    
    } 
    



}

package codecharioteers.Controller;
import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import codecharioteers.Dashboard.SignUpPage;

import com.google.cloud.firestore.DocumentSnapshot;

public class UserProfileController {

    String userName ,firstName,lastName;
    String userEmail;
    // Long age;
    // Long num;
      public static final String API_KEY ="AIzaSyDAg60QN37odlnLwhWnww3JI8STy3V8SyQ";

    public void fetchData(){

        String email=SignUpPage.mainEmail;
        System.out.println(email);
                      
   try {
     
         Firestore db = FirebaseInitialize.getFirestoreObject();        
        DocumentSnapshot doc = db.collection("users").document("vedantsonar4@gmail.com").get().get();
        firstName=doc.getString("name");
        lastName=doc.getString("surname");
        userEmail=doc.getString("email");
        userName=doc.getString("username");
        // age=doc.getLong("age");
        // num=doc.getLong("num");

    } catch (Exception e) {
        System.out.println("Firebase initialization failed: " + e.getMessage());
    }
    
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // public Long getAge() {
    //     return age;
    // }

    // public Long getNum() {
    //     return num;
    // }

   
    
}

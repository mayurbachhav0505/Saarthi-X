package codecharioteers.Controller;

import java.net.URL;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class AuthController {

  public static final String API_KEY ="AIzaSyDAg60QN37odlnLwhWnww3JI8STy3V8SyQ";
    
    public boolean signInWithEmailAndPassword(String email,String password){
    
     try{ 
     Firestore db=FirebaseInitialize.getFirestoreObject();
      
      DocumentReference docRef = db.collection("users").document(email);
      ApiFuture<DocumentSnapshot> future = docRef.get();
      DocumentSnapshot document = future.get();
      
      if (document.exists()) {
                String storedPassword = document.getString("password");

                // Check if password matches
                if (storedPassword != null && storedPassword.equals(password)) {
                    System.out.println("Login successful for user: " + email);
                    return true;
                } else {
                    System.out.println("Incorrect password.");
                    return false;
                }
            } else {
                System.out.println("No user found with email: " + email);
                return false;
            }
       } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}

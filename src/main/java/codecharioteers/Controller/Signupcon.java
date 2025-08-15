package codecharioteers.Controller;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.Firestore;

public class Signupcon {

    
     public static void addSignData(String name, String surName, String userName, String email, String password) {
     System.out.println("Inside getdata");

        Firestore db=FirebaseInitialize.getFirestoreObject();
           
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("surname", surName);
        userInfo.put("username", userName);
        userInfo.put("email", email);
        userInfo.put("password", password);
        

        try {

            db.collection("users").document(email).set(userInfo);
        } catch (Exception e) {
            e.printStackTrace();    
    }

     
    }

     
}

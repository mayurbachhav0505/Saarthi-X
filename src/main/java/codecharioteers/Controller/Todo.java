package codecharioteers.Controller;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.DocumentSnapshot;


public class Todo {

    String title;
    String description;
    String category;
    String dueDate;
    String time;
    String priority;
    String email ="vedantsonar@gmail.com";

    public void addData(String title, String description, String category, String dueDate, String time, String priority) {
          System.out.println("Inside getdata");
            
        Firestore db=FirebaseInitialize.getFirestoreObject()    ;  
       
        Map<String,Object> taskobj = new HashMap<>();
        taskobj.put("title", title);
        taskobj.put("description", description);
        taskobj.put("feild", category);
        taskobj.put("date", dueDate);
        taskobj.put("time", time);
        taskobj.put("priority", priority);

        try {

            db.collection("alltask").document().set(taskobj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        
    
    }
}
    
}
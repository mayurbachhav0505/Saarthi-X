package codecharioteers.Controller;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseInitialize {

    static {
        initializeFirebase();
    }
    
    public static void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("saarthix\\src\\main\\resources\\saarthi-x-firebase-adminsdk-fbsvc-eccce8d021.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://your-database-name.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            System.out.println("Firebase initialization failed: " + e.getMessage());
        }
    }

    public static Firestore getFirestoreObject() {
        System.out.println("Multiple call hote ahe ");
        return FirestoreClient.getFirestore();
    }
}

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @Value("\${firebase.private.key}")
    private lateinit var firebasePrivateKey: String

    @Value("\${bucket.name}")
    private lateinit var bucketName: String

    @Bean
    fun firebaseApp(): FirebaseApp {
        return FirebaseApp.getInstance()
    }

    @Bean
    fun firebaseOptions(): FirebaseOptions {
        return FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(firebasePrivateKey.byteInputStream()))
            .setStorageBucket(bucketName)
            .build()
    }

    @PostConstruct
    fun init() {
        try {
            FirebaseApp.initializeApp(firebaseOptions())
        } catch (e: IOException) {
            throw RuntimeException("Could not initialize Firebase", e)
        }
    }
}

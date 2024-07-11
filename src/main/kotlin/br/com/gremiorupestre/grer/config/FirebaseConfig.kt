package br.com.gremiorupestre.grer.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Bean
    fun initializeFirebase(): FirebaseApp {
        val serviceAccount = FileInputStream("/google-services.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("jornal-if-61544.appspot.com")
            .build()

        return FirebaseApp.initializeApp(options)
    }
}

package br.com.gremiorupestre.grer.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class FirebaseConfig {

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Value("\${firebase.private.key}")
    lateinit var privateKey: String

    @Value("\${bucket.name}")
    lateinit var storageBucket: String

    @Bean
    fun initializeFirebase() {
        val resource = resourceLoader.getResource(privateKey)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
            .setStorageBucket(storageBucket)
            .build()

        FirebaseApp.initializeApp(options)
    }
}

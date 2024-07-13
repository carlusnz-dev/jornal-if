package br.com.gremiorupestre.grer.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import javax.annotation.PostConstruct

@Configuration
class FirebaseInitializer() {

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Value("\${firebase.private.key}")
    lateinit var privateKeyPath: String

    @Value("\${bucket.name}")
    lateinit var bucketName: String

    @PostConstruct
    fun initialize() {

        val resource = resourceLoader.getResource(privateKeyPath)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
            .setStorageBucket(bucketName)
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
            println("Firebase initialized with storage bucket: $bucketName")
            println("Firebase initialized with private key: $privateKeyPath")
        }
    }
}

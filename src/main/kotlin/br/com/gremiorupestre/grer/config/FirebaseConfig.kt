package br.com.gremiorupestre.grer.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jdk.internal.loader.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Autowired
    lateinit var resource: ResourceLoader

    @Value("\${firebase.private.key}")
    lateinit var PRIVATE_KEY: String

    @Value("\${bucket.name}")
    lateinit var STORAGE_BUCKET: String

    @Bean
    fun initializeFirebase() {

        val resource = resource.getResource(PRIVATE_KEY)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
            .setStorageBucket(STORAGE_BUCKET)
            .build()

        FirebaseApp.initializeApp(options)
    }
}

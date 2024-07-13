package br.com.gremiorupestre.grer.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Component
class FileUtil {

    private lateinit var storage: Storage

    @Value("\${bucket.name}")
    lateinit var bucketName: String

    @PostConstruct
    fun init() {
        val credentials = GoogleCredentials.fromStream(ClassPathResource("firebase.json").inputStream)
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().service
    }

    fun saveFile(file: MultipartFile): String {
        if (!::bucketName.isInitialized) {
            throw UninitializedPropertyAccessException("bucketName has not been initialized")
        }

        val fileName = "${LocalDateTime.now().toString().replace(":", "-")}${file.originalFilename}"
        val sanitizedFileName = fileName.replace(Regex("[^A-Za-z0-9_.-]"), "")

        if (sanitizedFileName.isBlank()) {
            throw IllegalArgumentException("File name after sanitization is empty or invalid")
        }

        val blobId = BlobId.of(bucketName, sanitizedFileName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()

        try {
            storage.create(blobInfo, file.bytes)
            println("File saved in Firebase: gs://$bucketName/$sanitizedFileName")
            return sanitizedFileName
        } catch (e: Exception) {
            println("Failed to save file: ${e.message}")
            throw RuntimeException("Could not save file. Please try again!", e)
        }
    }
}

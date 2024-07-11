package br.com.gremiorupestre.grer.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.time.LocalDateTime

class FileUtil {

    private val storage: Storage
    private val bucketName: String

    init {
        val credentialsPath = "classpath:google-services.json"
        val credentials = GoogleCredentials.fromStream(ClassPathResource(credentialsPath).inputStream)
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().service

        // Bucket name from application properties or environment variable
        bucketName = "jornal-if-61544.appspot.com"
    }

    fun saveFile(file: MultipartFile): String {
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

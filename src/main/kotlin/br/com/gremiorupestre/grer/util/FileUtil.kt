package br.com.gremiorupestre.grer.util

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

class FileUtil {

    private val storage: Storage = StorageOptions.getDefaultInstance().service

    @Value("\${bucket.name}")
    private lateinit var bucketName: String

    companion object {
        fun create(): FileUtil = FileUtil()
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

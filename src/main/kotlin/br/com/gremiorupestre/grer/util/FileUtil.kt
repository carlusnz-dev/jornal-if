package br.com.gremiorupestre.grer.util

import br.com.gremiorupestre.grer.service.FirebaseStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Component
class FileUtil @Autowired constructor(
    private val firebaseStorageService: FirebaseStorageService
) {

    fun create(file: MultipartFile): String {
        return saveFile(file)
    }

    fun saveFile(file: MultipartFile): String {
        val fileName = "${LocalDateTime.now().toString().replace(":", "-")}${file.originalFilename}"
        val sanitizedFileName = fileName.replace(Regex("[^A-Za-z0-9_.-]"), "")

        if (sanitizedFileName.isBlank()) {
            throw IllegalArgumentException("File name after sanitization is empty or invalid")
        }

        try {
            val fileUrl = firebaseStorageService.uploadFile(file, sanitizedFileName)
            println("File saved in Firebase: $fileUrl")
            return fileUrl
        } catch (e: Exception) {
            println("Failed to save file: ${e.message}")
            throw RuntimeException("Could not save file. Please try again!", e)
        }
    }
}

package br.com.gremiorupestre.grer.util

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class FileUtil {

    private val UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads"

    companion object {
        fun create(): FileUtil = FileUtil()
    }

    init {
        // Create the directory if it doesn't exist
        val uploadPath = Paths.get(UPLOAD_DIRECTORY)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
    }

    fun saveFile(file: MultipartFile): String {
        val fileName = "${LocalDateTime.now().toString().replace(":", "-")}${file.originalFilename}"
        val sanitizedFileName = fileName.replace(Regex("[^A-Za-z0-9_.-]"), "")

        if (sanitizedFileName.isBlank()) {
            throw IllegalArgumentException("File name after sanitization is empty or invalid")
        }

        val fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, sanitizedFileName)

        try {
            Files.write(fileNameAndPath, file.bytes)
            println("File saved in: $fileNameAndPath")
            return "/uploads/$sanitizedFileName"
        } catch (e: Exception) {
            println("Failed to save file: ${e.message}")
            throw RuntimeException("Could not save file. Please try again!", e)
        }
    }
}

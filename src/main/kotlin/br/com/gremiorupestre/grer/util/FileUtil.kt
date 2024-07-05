package br.com.gremiorupestre.grer.util

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class FileUtil {

    private var UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads"

    companion object {
        fun create(): FileUtil = FileUtil()
    }

    fun saveFile( file : MultipartFile) : String {

        // Variables
        val fileNames = StringBuilder()
        val fileName = "${LocalDateTime.now().toString().replace(":","-")}${file.originalFilename}"
        val fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName)

        // Save file
        fileNames.append(fileName)
        Files.write(fileNameAndPath, file.bytes)
        println("File saved in: $fileNameAndPath")
        return "/uploads/${fileName}"

    }

}
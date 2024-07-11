package br.com.gremiorupestre.grer.service

import com.google.firebase.cloud.StorageClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

@Service
class FirebaseStorageService {

    fun uploadFile(file: MultipartFile, fileName: String): String {
        val bucket = StorageClient.getInstance().bucket()
        val blob = bucket.create(fileName, file.bytes, file.contentType)

        return blob.mediaLink
    }

    @Throws(IOException::class)
    fun downloadFile(fileName: String): ByteArray {
        val bucket = StorageClient.getInstance().bucket()
        val blob = bucket.get(fileName) ?: throw IOException("File not found")

        val baos = ByteArrayOutputStream()
        blob.downloadTo(baos)
        return baos.toByteArray()
    }
}

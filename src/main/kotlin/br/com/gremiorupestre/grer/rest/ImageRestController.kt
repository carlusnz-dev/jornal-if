package br.com.gremiorupestre.grer.rest

import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ImageRestController {

    private val storage: Storage = StorageOptions.getDefaultInstance().service

    @Value("\${bucket.name}")
    private lateinit var bucketName: String

    @GetMapping(value = ["/uploads/{imageName}"])
    fun downloadImage(@PathVariable imageName: String): ResponseEntity<Resource> {
        val blob: Blob = storage.get(bucketName, imageName) ?: return ResponseEntity.notFound().build()

        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$imageName\"")
            add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE) // Ajuste conforme o tipo de imagem
        }

        val resource = ByteArrayResource(blob.getContent())
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(blob.size)
            .body(resource)
    }
}

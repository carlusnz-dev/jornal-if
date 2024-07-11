package br.com.gremiorupestre.grer.rest

import br.com.gremiorupestre.grer.service.FirebaseStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

@RestController
class ImageRestController @Autowired constructor(
    private val firebaseStorageService: FirebaseStorageService
) {

    @GetMapping(value = ["/uploads/{imageName}"])
    fun downloadImage(@PathVariable imageName: String): ResponseEntity<Resource> {
        return try {
            val image = firebaseStorageService.downloadFile(imageName)

            val resource = ByteArrayResource(image)
            val headers = HttpHeaders().apply {
                add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$imageName\"")
                add(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            }

            ResponseEntity.ok()
                .headers(headers)
                .contentLength(image.size.toLong())
                .body(resource)
        } catch (e: IOException) {
            ResponseEntity.notFound().build()
        }
    }
}

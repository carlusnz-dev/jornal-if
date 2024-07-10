package br.com.gremiorupestre.grer.rest

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
class ImageRestController {

    private val UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads"

    @GetMapping(value = ["/uploads/{imageName}"])
    fun downloadImage(@PathVariable imageName: String): ResponseEntity<Resource> {
        val file = File("$UPLOAD_DIRECTORY/$imageName")

        if (!file.exists() || !file.isFile) {
            return ResponseEntity.notFound().build()
        }

        val image: ByteArray = toByteArray(ImageIO.read(file)) ?: return ResponseEntity.badRequest().build()

        val resource = ByteArrayResource(image)
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$imageName\"")
            add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE) // Ajuste conforme o tipo de imagem
        }

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(image.size.toLong())
            .body(resource)
    }

    private val formats = listOf("jpg", "webp", "png", "gif", "jpeg", "bmp")

    @Throws(IOException::class)
    private fun toByteArray(bi: BufferedImage?): ByteArray? {
        if (bi == null) return null

        for (format in formats) {
            try {
                val baos = ByteArrayOutputStream()
                ImageIO.write(bi, format, baos)
                return baos.toByteArray()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }
}

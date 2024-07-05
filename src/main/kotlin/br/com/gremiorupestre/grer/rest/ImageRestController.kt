package br.com.gremiorupestre.grer.rest

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
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

    private var UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads"

    @GetMapping(value = ["/uploads/{imageName}"])
    fun downloadImage(@PathVariable imageName: String): Resource? {

        val image: ByteArray? = toByteArray(ImageIO.read(File("${UPLOAD_DIRECTORY}/${imageName}")))

        return image?.let { ByteArrayResource(it) }
    }

    private val formats = listOf("jpg", "webp","png","gif","jpeg", "bmp")

    @Throws(IOException::class)
    fun toByteArray(bi: BufferedImage?): ByteArray? {

        for (format in formats){

            try {
                val baos = ByteArrayOutputStream()
                ImageIO.write(bi, "webp", baos)
                return baos.toByteArray()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        return null
    }


}
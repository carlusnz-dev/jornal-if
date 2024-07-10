package br.com.gremiorupestre.grer.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver


@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/")
        registry.addResourceHandler("/sass/**")
            .addResourceLocations("classpath:/static/sass/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
        registry.addResourceHandler("/img/**")
            .addResourceLocations("classpath:/static/img/")
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/")
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter())
    }

    @Bean
    fun resourceUrlEncodingFilter(): ResourceUrlEncodingFilter {
        return ResourceUrlEncodingFilter()
    }

    @Bean
    fun templateEngine(templateResolver: SpringResourceTemplateResolver): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)
        templateEngine.addDialect(SpringSecurityDialect())
        return templateEngine
    }

}
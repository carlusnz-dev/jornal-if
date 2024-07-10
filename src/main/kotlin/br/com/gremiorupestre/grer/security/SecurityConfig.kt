    package br.com.gremiorupestre.grer.security

    import org.springframework.context.annotation.Bean
    import org.springframework.context.annotation.ComponentScan
    import org.springframework.context.annotation.Configuration
    import org.springframework.context.annotation.Lazy
    import org.springframework.context.annotation.Primary
    import org.springframework.security.authentication.AuthenticationManager
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
    import org.springframework.security.config.annotation.web.builders.HttpSecurity
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
    import org.springframework.security.core.userdetails.UserDetailsService
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
    import org.springframework.security.web.SecurityFilterChain
    import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
    import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
    import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
    import javax.sql.DataSource

    @Configuration
    @EnableWebSecurity
    @ComponentScan(basePackages = ["br.com.gremiorupestre.grer"])
    class SecurityConfig(
        @Lazy private val userDetailsService: UserDetailsService,
        private val dataSource: DataSource
    ) {

        @Primary
        @Bean
        fun securityFilterChain(http : HttpSecurity) : SecurityFilterChain {
            http.csrf { it.disable() }
                .authorizeHttpRequests { authorizeRequests ->
                    authorizeRequests
                        // Public pages
                        .requestMatchers("/", "/login", "/logout", "/register/**", "/articles", "/articles/{id}", "/navbar/**", "/editions", "/editions/{id}", "/editions/{id}/articles").permitAll()
                        // Private pages for admin
                        .requestMatchers("/admin", "/articles/**", "/editions/**").hasRole("ADMIN")
                        // Private pages for news in articles
                        .requestMatchers("/articles/new", "/articles/edit/", "/articles/update/", "/editions/**").hasRole("NEWS")
                        // Public resources
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/uploads", "/uploads/**").permitAll()
                        // For user authenticaded
                        .requestMatchers("/reset-password/**","/forgot-password/**").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                }
                .formLogin { formLogin ->
                    formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successForwardUrl("/")
                        .successHandler { _, response, _ -> response.sendRedirect("/") }
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                }
                .logout { logout ->
                    logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                }
                .passwordManagement {
                    it
                        .changePasswordPage("/reset-password")
                }
                .rememberMe {
                    it
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService)
                        .tokenRepository(persistentTokenRepository())
                }
                .httpBasic {
                    it
                        .disable()
                }
            return http.build()
        }

        @Bean
        fun passwordEncoder() = BCryptPasswordEncoder()

        @Bean
        fun rememberMeServices(): PersistentTokenBasedRememberMeServices {
            return PersistentTokenBasedRememberMeServices("uniqueAndSecret", userDetailsService, persistentTokenRepository())
        }

        @Bean
        fun persistentTokenRepository(): PersistentTokenRepository {
            val tokenRepository = JdbcTokenRepositoryImpl()
            tokenRepository.setDataSource(dataSource)
            return tokenRepository
        }

        @Bean
        fun authenticationManager(configuration: AuthenticationConfiguration) : AuthenticationManager {
            return configuration.authenticationManager
        }

    }
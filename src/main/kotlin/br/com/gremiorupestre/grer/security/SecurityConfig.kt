    package br.com.gremiorupestre.grer.security

    import org.springframework.context.annotation.Bean
    import org.springframework.context.annotation.Configuration
    import org.springframework.context.annotation.Primary
    import org.springframework.security.config.annotation.web.builders.HttpSecurity
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
    import org.springframework.security.core.userdetails.UserDetailsService
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
    import org.springframework.security.crypto.password.PasswordEncoder
    import org.springframework.security.web.SecurityFilterChain
    import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
    import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
    import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
    import javax.sql.DataSource

    @Configuration
    @EnableWebSecurity
    class SecurityConfig(private val userDetailsService: UserDetailsService, private val dataSource: DataSource) {

        @Primary
        @Bean
        @Throws(Exception::class)
        fun securityFilterChain(http : HttpSecurity) : SecurityFilterChain {
            http
                .authorizeHttpRequests { authorizeRequests ->
                    authorizeRequests
                        .requestMatchers("/", "/login", "/register", "/articles/", "/webjars/**", "/register/**", "login/**", "/error", "/logout").permitAll()
                        .anyRequest().authenticated()
                }
                .formLogin { formLogin ->
                    formLogin
                        .loginPage("/login")
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
        fun passwordEncoder() : PasswordEncoder {
            return BCryptPasswordEncoder()
        }

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

    }
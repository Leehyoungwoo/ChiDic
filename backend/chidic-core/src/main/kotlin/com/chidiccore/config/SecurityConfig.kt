package com.chidiccore.config

import com.chidiccore.handler.common.CustomAccessDeniedHandler
import com.chidiccore.handler.entrypoint.CustomAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private final val URL_WHITE_LIST: Array<String> = arrayOf(
        "/error", "/login", ""
    )
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it
                    .requestMatchers(*URL_WHITE_LIST)
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/members")
                    .permitAll()
                    .requestMatchers("/**")
                    .hasAnyRole("ADMIN", "USER")
                    .anyRequest()
                    .authenticated()
            }
            .headers { }
            .oauth2Login { }
            .exceptionHandling { it
                .accessDeniedHandler(CustomAccessDeniedHandler())
                .authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }


        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf(
                "http://localhost:3000",
                "http://localhost:8080"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf(
                "Authorization",
                "Cache-Control",
                "Content-Type"
            )
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
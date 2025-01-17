package com.chidiccore.config

import com.chidiccore.handler.common.CustomAccessDeniedHandler
import com.chidiccore.handler.entrypoint.CustomAuthenticationEntryPoint
import com.chidiccore.jwt.filter.JwtFilter
import com.chidiccore.jwt.util.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val jwtProvider: JwtProvider
) {

    private val URL_WHITE_LIST: List<String> = listOf(
        "/error", "/login", "/signup", "/api/login/kakao", "/oauth2/authorization/kakao", "/login/oauth2/code/kakao"
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .oauth2Login { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*URL_WHITE_LIST.toTypedArray())
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/members")
                    .permitAll()
                    .requestMatchers("/**")
                    .hasAnyRole("ADMIN", "USER")
                    .anyRequest()
                    .authenticated()
            }
            .headers { }
            .exceptionHandling {
                it
                    .accessDeniedHandler(CustomAccessDeniedHandler())
                    .authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .addFilterBefore(
                JwtFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

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
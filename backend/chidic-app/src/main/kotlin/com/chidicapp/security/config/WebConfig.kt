package com.chidicapp.security.config

import com.chidicapp.security.auth.resolver.GetUserIdArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Bean
    fun getUserIdArgumentResolver(): GetUserIdArgumentResolver {
        return GetUserIdArgumentResolver()
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(getUserIdArgumentResolver())
    }
}

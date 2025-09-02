package com.swapreporting.config;

//import com.swapreporting.security.JwtAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;

@Configuration
public class SecurityConfig {

//    @Bean
//    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
//        FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>();
//        reg.setFilter(new JwtAuthFilter());
//        reg.addUrlPatterns("/inference/*", "/report/*");
//        return reg;
//    }
}
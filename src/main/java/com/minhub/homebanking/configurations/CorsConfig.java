package com.minhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Crea una nueva configuración de CORS.

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:5173","http://localhost:5175", "https://homebankingfront.onrender.com")); // Define los orígenes permitidos.

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Define los métodos HTTP permitidos.

        configuration.setAllowedHeaders(List.of("*")); // Define los encabezados permitidos, permitiendo todos.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Crea una fuente basada en URL para la configuración de CORS.
        source.registerCorsConfiguration("/**", configuration); // Asocia la configuración de CORS a todas las rutas.

        return source; // Retorna la configuración de CORS.
    }
}

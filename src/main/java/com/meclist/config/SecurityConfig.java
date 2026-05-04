package com.meclist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final com.meclist.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(com.meclist.security.JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // preflight
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                // públicos (autenticação)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/adms/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/mecanicos/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/clientes/definir-senha").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/clientes/primeiro-acesso").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/clientes/login").permitAll()

                // uploads estáticos
                .requestMatchers("/uploads/**").permitAll()

                // bootstrap (decidir se quer manter aberto)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/adms").hasRole("ADMIN")

                // clientes/veículos de cliente
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/clientes").hasAnyRole("ADMIN", "MECANICO", "CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "MECANICO", "CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/clientes/checklists/*/aprovacao").hasRole("CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/clientes/**").hasAnyRole("ADMIN", "MECANICO", "CLIENTE")
                .requestMatchers("/clientes/*/veiculos/**").hasAnyRole("ADMIN", "MECANICO")

                // mecânicos
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/mecanicos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/mecanicos").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/mecanicos/**").hasAnyRole("ADMIN", "MECANICO")

                // catálogo base
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/categorias-veiculo/**").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/veiculos/**").hasAnyRole("ADMIN", "MECANICO")

                // itens/produtos
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/itens/**").hasAnyRole("ADMIN", "MECANICO")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/itens/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/itens/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/itens/**").hasRole("ADMIN")

                // checklist
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/checklists/veiculos/*/iniciar").hasRole("MECANICO")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/checklists/*/categorias/*").hasRole("MECANICO")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/checklists/*/enviar-para-precificacao").hasRole("MECANICO")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/checklists/*/precificar").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/checklists/*/aprovacao").hasRole("CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/checklists/*/fotos-evidencia").hasAnyRole("CLIENTE", "MECANICO", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/checklists/*/aprovar").hasRole("CLIENTE")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/checklists/**").hasAnyRole("ADMIN", "MECANICO")

                // fallback
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuração CORS permitida para o frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://192.168.18.13:5173")); // Seu front-end
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Se estiver usando cookies ou autenticação

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}

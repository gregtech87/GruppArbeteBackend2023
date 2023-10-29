package com.Grupparbete.API.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    JdbcUserDetailsManager inMemoryUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer

                        //CINEMA
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/movies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/rooms/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/bookings/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings").hasRole("USER")

                        //SUSHI
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/sushis").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/sushis").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/sushis/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/ordersushis").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/bookings/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/bookings/**").hasRole("USER")

                        //Mc rental
                        .requestMatchers(HttpMethod.GET, "/api/v1/available_bikes").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/rent_bike").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/booking/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/bikes/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/v1/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/v1/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "api/v1/bike").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "api/v1//bike/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "api/v1/allBikes").hasRole("ADMIN")
        );
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }



}

package com.app.diamondhotelbackend.security;

import com.app.diamondhotelbackend.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user-profile/login").permitAll()
                .requestMatchers("/api/v1/user-profile/register").permitAll()
                .requestMatchers("/api/v1/room-type/all/info").permitAll()
                .requestMatchers("/api/v1/user-profile/all/info").hasAuthority(Constant.ADMIN)
                .requestMatchers("/api/v1/user-profile/id/{id}/details/info").hasAnyAuthority(Constant.USER, Constant.ADMIN)
                .requestMatchers("/api/v1/user-profile/id/{id}/delete").hasAuthority(Constant.ADMIN)
                .requestMatchers("/api/v1/room-type/configuration/info").hasAnyAuthority(Constant.USER, Constant.ADMIN)
                .requestMatchers("/api/v1/room-type/available/info").hasAuthority(Constant.USER)
                .requestMatchers("/api/v1/room-type/summary/shopping/cart").hasAuthority(Constant.USER)
                .requestMatchers("/api/v1/room-type/summary/shopping/cart/cost/with/car").hasAuthority(Constant.USER)
                .requestMatchers("/api/v1/reservation/all/info").hasAnyAuthority(Constant.USER, Constant.ADMIN)
                .requestMatchers("/api/v1/reservation/create/new").hasAuthority(Constant.USER)
                .requestMatchers("/api/v1/reservation/details/info").hasAnyAuthority(Constant.USER, Constant.ADMIN)
                .requestMatchers("/api/v1/reservation/id/{id}/cancel").hasAnyAuthority(Constant.USER, Constant.ADMIN)
                .requestMatchers("/api/v1/transaction/change/status").hasAuthority(Constant.USER)
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

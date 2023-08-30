package com.app.diamondhotelbackend.security.config;

import com.app.diamondhotelbackend.security.jwt.JwtFilter;
import com.app.diamondhotelbackend.security.oauth2.CustomOAuth2AuthenticationFailureHandler;
import com.app.diamondhotelbackend.security.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import com.app.diamondhotelbackend.service.oauth2.OAuth2ServiceImpl;
import com.app.diamondhotelbackend.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private final OAuth2ServiceImpl OAuth2Service;

    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/v1/auth/account/login").permitAll()
                .requestMatchers("/api/v1/auth/account/registration").permitAll()
                .requestMatchers("/api/v1/auth/confirmation-token/{token}/account/confirmation").permitAll()
                .requestMatchers("/api/v1/auth/email/{email}/account/forgotten/password").permitAll()
                .requestMatchers("/api/v1/auth/account/forgotten/password").permitAll()
                .requestMatchers("/api/v1/auth/auth-token/{token}").permitAll()
                .requestMatchers("/api/v1/auth/confirmation-token/{token}").permitAll()
                .requestMatchers("/api/v1/auth/account/email").hasAnyAuthority(ConstantUtil.ADMIN, ConstantUtil.USER)
                .requestMatchers("/api/v1/auth/account/password").hasAnyAuthority(ConstantUtil.ADMIN, ConstantUtil.USER)
                .requestMatchers(HttpMethod.GET, "/api/v1/user-profile/id/{id}").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/user-profile/id/{id}").hasAuthority(ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/user-profile/email/{email}").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/user-profile/all").hasAuthority(ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/user-profile/email/{email}/picture").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/user-profile/email/{email}/details").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/weather/all").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/room-type/all/info").permitAll()
                .requestMatchers("/api/v1/room-type/configuration/info").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/room-type/available/info").hasAuthority(ConstantUtil.USER)
                .requestMatchers("/api/v1/room-type/summary/shopping/cart").hasAuthority(ConstantUtil.USER)
                .requestMatchers("/api/v1/room-type/summary/shopping/cart/cost/with/car").hasAuthority(ConstantUtil.USER)
                .requestMatchers("/api/v1/reservation/all/info").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/reservation/create/new").hasAuthority(ConstantUtil.USER)
                .requestMatchers("/api/v1/reservation/details/info").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/reservation/id/{id}/cancel").hasAnyAuthority(ConstantUtil.USER, ConstantUtil.ADMIN)
                .requestMatchers("/api/v1/transaction/change/status").hasAuthority(ConstantUtil.USER)
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/api/v1/user-profile/login/oauth2/google")
                .and()
                .userInfoEndpoint()
                .userService(OAuth2Service)
                .and()
                .successHandler(customOAuth2AuthenticationSuccessHandler)
                .failureHandler(customOAuth2AuthenticationFailureHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

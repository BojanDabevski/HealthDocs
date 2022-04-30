package com.healthDocs.healthDocs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {


        private final PasswordEncoder passwordEncoder;
        private final CustomPatientAuthenticationProvider customPatientAuthenticationProvider;

        public WebSecurityConfig(PasswordEncoder passwordEncoder,
                                 CustomPatientAuthenticationProvider customPatientAuthenticationProvider) {
            this.passwordEncoder = passwordEncoder;
            this.customPatientAuthenticationProvider = customPatientAuthenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/doctor", "/doctor/**", "/css/**", "/images/**", "/js/**").permitAll()
                    .antMatchers("/patient/**").hasRole("PATIENT")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/patient/login").permitAll()
                    .failureUrl("/patient/login?error=BadCredentials")
                    .defaultSuccessUrl("/patient/termini", true)
                    .loginProcessingUrl("/patient/processLogin")
                    .and()
                    .logout()
                    .logoutUrl("/patient/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/patient/login");


        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(this.customPatientAuthenticationProvider);
        }
    }






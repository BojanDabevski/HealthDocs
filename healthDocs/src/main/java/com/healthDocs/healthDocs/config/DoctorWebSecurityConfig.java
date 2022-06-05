package com.healthDocs.healthDocs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;



    public  class DoctorWebSecurityConfig extends WebSecurityConfigurerAdapter {
        private final PasswordEncoder passwordEncoder;
        private final CustomPatientAuthenticationProvider customPatientAuthenticationProvider;



        public DoctorWebSecurityConfig(PasswordEncoder passwordEncoder,
                                          CustomPatientAuthenticationProvider customPatientAuthenticationProvider) {
            this.passwordEncoder = passwordEncoder;
            this.customPatientAuthenticationProvider = customPatientAuthenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.headers().frameOptions().sameOrigin();
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2", "/h2/**").permitAll()
                    .antMatchers("/doctor/**").hasRole("DOCTOR")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/doctor/login").permitAll()
                    .failureUrl("/doctor/login?error=BadCredentials")
                    .defaultSuccessUrl("/doctor/termini", true)
                    .loginProcessingUrl("/doctor/processLogin")
                    .and()
                    .logout()
                    .logoutUrl("/doctor/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/doctor/login");


        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(this.customPatientAuthenticationProvider);
        }
    }
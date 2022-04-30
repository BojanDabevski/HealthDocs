package com.healthDocs.healthDocs.config;


import com.healthDocs.healthDocs.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPatientAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public CustomPatientAuthenticationProvider(PasswordEncoder passwordEncoder,UserService userService){
        this.userService=userService;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username=authentication.getName();
        String password=authentication.getCredentials().toString();

        if("".equals(username) || "".equals(password)){
            throw new BadCredentialsException("Invalid Credentials");
        }
        UserDetails userDetails = this.userService.loadUserByUsername(username);
       /* if(!userDetails.getPassword().equals(password)){
            throw new BadCredentialsException("Bad Password");
        }
        */
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Bad password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
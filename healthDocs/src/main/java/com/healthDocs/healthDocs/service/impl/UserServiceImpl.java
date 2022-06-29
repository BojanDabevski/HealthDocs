package com.healthDocs.healthDocs.service.impl;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.UserService;

import javax.annotation.PostConstruct;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public User register(String username, String password, String repeatPassword, String firstName, String lastName, String EMBG, Role role,Boolean insurance)  {
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            return null;
        }
        if(!password.equals(repeatPassword)){
            return null;
        }
        if(this.userRepository.findByUsername(username).isPresent() || !this.userRepository.findByUsername(username).isEmpty())
            return null;
        String encodedPassword = passwordEncoder.encode(password);
        User user=new User(EMBG,username,encodedPassword,firstName,lastName,role,insurance);
        return this.userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(s));
    }
    
    //odkomentiraj PostConstruct za da se kreiraat svezi akounti pri nareden startup na sistemot
    //@PostConstruct
    public void registerTestUsers() {
    	register("testDoctor","test123","test123","Test","Test","1234567890123",Role.ROLE_DOCTOR,true);
    	register("testPatient","test123","test123","Test","Test","1234567890123",Role.ROLE_PATIENT,true);
    	register("testAdmin","test123","test123","Test","Test","1234567890123",Role.ROLE_ADMIN,true);

    }


}
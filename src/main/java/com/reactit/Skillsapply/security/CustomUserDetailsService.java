package com.reactit.Skillsapply.security;

import java.util.*;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User dbUser = this.userRepository.findByEmail(email);

        if (dbUser != null) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                GrantedAuthority authority = new SimpleGrantedAuthority(dbUser.getRoles());
                grantedAuthorities.add(authority);


            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                    dbUser.getEmail(), dbUser.getPassword(), grantedAuthorities);

            System.out.println(user);

            Map<String, Object> response = new HashMap<>();
            response.put("user email",dbUser.getEmail());
            response.put("user firstName",dbUser.getFirstName());
            response.put("user lastName",dbUser.getLastName());
//            System.out.println(response);
            new ResponseEntity<>(response, HttpStatus.OK);
            return user;
        } else {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
    }

}

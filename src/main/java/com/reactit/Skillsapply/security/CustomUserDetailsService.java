package com.reactit.Skillsapply.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

//    public ResponseEntity<Void> getUserinfo(String email){
//        User dbUser = this.userRepository.findByEmail(email);
//
//        if(dbUser!= null){
//            Map<String, Object> response = new HashMap<>();
//            response.put("user email",dbUser.getEmail());
//            response.put("user firstName",dbUser.getFirstName());
//            response.put("user lastName",dbUser.getLastName());
//            System.out.println(response);
//         return  new ResponseEntity(response, HttpStatus.OK);
//        } else
//            new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
//
//        return  new ResponseEntity("ssss", HttpStatus.OK);
//    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User dbUser = this.userRepository.findByEmail(email);

        if (dbUser != null) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                GrantedAuthority authority = new SimpleGrantedAuthority(dbUser.getRoles());
                grantedAuthorities.add(authority);


            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                    dbUser.getEmail(), dbUser.getPassword(), grantedAuthorities);



            Map<String, Object> response = new HashMap<>();
            response.put("user email",dbUser.getEmail());
            response.put("user firstName",dbUser.getFirstName());
            response.put("user lastName",dbUser.getLastName());
            System.out.println(response);
            new ResponseEntity<>(response, HttpStatus.OK);
            return user;
        } else {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
    }

}

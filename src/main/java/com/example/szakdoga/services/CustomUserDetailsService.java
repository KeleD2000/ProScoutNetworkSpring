package com.example.szakdoga.services;

import com.example.szakdoga.model.User;
import com.example.szakdoga.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            //kell dobni egy exceptiont
            throw new UsernameNotFoundException("A felhasználó nem található.");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(); //ez van a spring securitybe
        authorities.add(new SimpleGrantedAuthority(user.get().getRoles().name()));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
    }
}

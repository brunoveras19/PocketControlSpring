package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.User;
import com.veras.pocketcontrol.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if(!user.isPresent()){
            System.out.println("Usuário não encontrado");
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        //Preencher com roles quando implementar
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getPassword(), authorities);
    }

    public String getLoggedUserId() {
        return this.getUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
    }


    public Optional<List<User>> getAllCategories() {
        return Optional.of(userRepository.findAll());
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.toString());
        User userInserted = userRepository.insert(user);
        return userInserted;
    }

    public User updateUser(User user) {
        User userToUpdate = userRepository.save(user);
        return userToUpdate;
    }

    public User deleteUser(String id) {
        User userDeleted = userRepository.findById(id).get();
        userRepository.deleteById(id);
        return userDeleted;
    }

}
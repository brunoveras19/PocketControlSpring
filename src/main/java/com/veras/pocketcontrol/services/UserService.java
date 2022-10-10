package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.User;
import com.veras.pocketcontrol.repositories.UserRepository;
import com.veras.pocketcontrol.utils.Consts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            System.out.println(Consts.USER_NOT_FOUND_MESSAGE);
            throw new UsernameNotFoundException(Consts.USER_NOT_FOUND_MESSAGE);
        }

        //Preencher com roles quando implementar
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    public String getLoggedUserId() {
        return this.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
    }


    public Optional<List<User>> getAllCategories() {
        return Optional.of(userRepository.findAll());
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.toString());
        User userInserted = userRepository.insert(user);
        return userInserted;
    }

    public User updateUser(User user) throws UsernameNotFoundException{
        Optional<User> opUserToUpdate = getUserByUsername(user.getUsername());
        if(opUserToUpdate.isPresent()){
            User userToUpdate = opUserToUpdate.get();
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            User userUpdated = userRepository.save(user);
            return userUpdated;
        } else {
            throw new UsernameNotFoundException(Consts.USER_NOT_FOUND_MESSAGE);
        }
    }

    public User deleteUser(String id) {
        User userDeleted = userRepository.findById(id).get();
        userRepository.deleteById(id);
        return userDeleted;
    }

    public void updateUserLastLogin(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    public Integer getLastLoginDay() {
        return this.getUser(this.getLoggedUserId()).get().getLastLogin().getDayOfMonth();
    }
}

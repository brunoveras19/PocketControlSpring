package com.veras.pocketcontrol.webresources;

import com.veras.pocketcontrol.models.User;
import com.veras.pocketcontrol.services.UserService;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/user").toUriString());
        System.out.println(user.toString());
        userService.insertUser(user);
        return ResponseEntity.created(uri).body("Usuário criado com sucesso!");
    }

}

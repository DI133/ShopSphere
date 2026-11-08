package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.User;
import com.project.ecommerce_app.security.JwtUtil;
import com.project.ecommerce_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserService userService;

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {

        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody User user) {

        User updatedUser = userService.updateUser(id, user);

        String newToken = jwtUtils.generateToken(updatedUser.getUsername());

        return ResponseEntity.ok(Map.of(
                "user", updatedUser,
                "token", newToken
        ));
    }

}
package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.dto.CustomerDetailsDTO;
import com.project.ecommerce_app.dto.SellerDetailsDTO;
import com.project.ecommerce_app.entity.User;
import com.project.ecommerce_app.repository.UserRepository;
import com.project.ecommerce_app.security.JwtUtil;
import com.project.ecommerce_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        // check if user exists with same username
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        // check if user exists with same email
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already registered");
        }

        // if not then create new user entry in the database
        User newUser = new User(
                user.getUsername(),
                user.getEmail(),
                encoder.encode(user.getPassword()),
                user.getRole()
        );

        userService.createUser(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User existingUser = userRepository.findByUsername(user.getUsername());

        // token generation
        String jwt = jwtUtils.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "user", existingUser
        ));

    }


    // for changing password using current password
    @PostMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(
            @PathVariable Integer userId,
            @RequestBody Map<String,String> request
    ) {

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        //get user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // verify current password
        if(!encoder.matches(currentPassword, user.getPassword())){
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        // update password
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }


    @PostMapping("/forgot-password/{userId}")
    public ResponseEntity<?> forgotPassword(
            @PathVariable Integer userId,
            @RequestBody Map<String,String> request
    ){

        String username = request.get("username");
        String email = request.get("email");
        String newPassword = request.get("password");

        // find user by userID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check username and email matches or not
        if(!user.getUsername().equals(username) || !user.getEmail().equals(email)){
            return ResponseEntity.badRequest().body("Username or email does not match");
        }

        // if matches set new password
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }


    // update password if user is not logged in with email and username
    @PostMapping("/forgot-password-w")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> request){

        String username = request.get("username");
        String email = request.get("email");
        String newPassword = request.get("password");

        // find user by username
        User user = userRepository.findByUsername(username);

        if(user == null){
            return ResponseEntity.badRequest().body("User not found");
        }

        if(!user.getEmail().equals(email)){
            return ResponseEntity.badRequest().body("Email does not match");
        }

        // set the new password
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }


    // get all the sellers
    @GetMapping("/admin/users/sellers")
    public ResponseEntity<List<User>> getAllSellers() {
        return ResponseEntity.ok(userService.getAllSellers());
    }


    // get all the customers
    @GetMapping("/admin/users/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }


    // delete user by userID (not used because user entity has many relationship with other entities)
    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }


    // get all the sellers details
    @GetMapping("/admin/seller-details")
    public ResponseEntity<List<SellerDetailsDTO>> getSellerDetails() {
        return ResponseEntity.ok(userService.getSellerDetails());
    }

    // get all the customers details
    @GetMapping("/admin/customer-details")
    public ResponseEntity<List<CustomerDetailsDTO>> getCustomerDetails() {
        return ResponseEntity.ok(userService.getCustomerDetails());
    }
}
package com.delibrary.lib_backend.controller;

import com.delibrary.lib_backend.dto.AuthResponseDto;
import com.delibrary.lib_backend.dto.LoginDto;
import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.Librarian;
import com.delibrary.lib_backend.service_librarian.AuthService;
import com.delibrary.lib_backend.service_librarian.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/client/register")
    public ResponseEntity<?> registerClient(@RequestBody Client client) {

        try {
            userService.registerUser(client);
            return ResponseEntity.ok("Client registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/librarian/register")
    public ResponseEntity<?> registerLibrarian(@RequestBody Librarian librarian) {

        try {
            userService.registerUser(librarian);
            return ResponseEntity.ok("Librarian registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

        try {
            // Authenticate the user and retrieve the token and role
            String[] tokenAndRole = authService.login(loginDto).split(",");
            String token = tokenAndRole[0];
            String role = tokenAndRole[1];

            // Prepare the response with the token and role
            AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setAccessToken(token);
            authResponseDto.setRole(role);

            return ResponseEntity.ok(authResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Login Unsuccessful");
        }
    }
}
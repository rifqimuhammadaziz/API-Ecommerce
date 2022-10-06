package com.rifqimuhammadaziz.ecommerce.controller;

import com.rifqimuhammadaziz.ecommerce.entity.User;
import com.rifqimuhammadaziz.ecommerce.model.JWTResponse;
import com.rifqimuhammadaziz.ecommerce.model.LoginRequest;
import com.rifqimuhammadaziz.ecommerce.model.RefreshTokenRequest;
import com.rifqimuhammadaziz.ecommerce.model.SignUpRequest;
import com.rifqimuhammadaziz.ecommerce.security.jwt.JWTUtils;
import com.rifqimuhammadaziz.ecommerce.security.service.UserDetailsImpl;
import com.rifqimuhammadaziz.ecommerce.security.service.UserDetailsServiceImpl;
import com.rifqimuhammadaziz.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<JWTResponse> authenticateUser(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok().body(new JWTResponse(
                token, refreshToken, principal.getUsername(), principal.getEmail()
        ));
    }

    @PostMapping("/signup")
    public User signUp(@RequestBody SignUpRequest request) {
        User user = new User();
        user.setId(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRoles("USER");

        User newUser = userService.create(user);
        return newUser;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        boolean valid = jwtUtils.validateJwtToken(token);
        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String username = jwtUtils.getUsernameByJwtToken(token);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String newToken = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);

        return ResponseEntity.ok(new JWTResponse(newToken, refreshToken, username, userDetails.getEmail()));
    }
}

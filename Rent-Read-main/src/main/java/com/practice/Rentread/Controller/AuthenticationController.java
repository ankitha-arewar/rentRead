package com.practice.Rentread.Controller;

import com.practice.Rentread.Entities.User;
import com.practice.Rentread.Exceptions.UserExistException;
import com.practice.Rentread.Service.AuthenticationService;
import com.practice.Rentread.dto.ErrorResponse;
import com.practice.Rentread.dto.JwtAuthenticationResponse;
import com.practice.Rentread.dto.SignInRequest;
import com.practice.Rentread.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
        try{
            return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
        }catch(UserExistException e){
            log.info(e.getMessage());
            return new ResponseEntity (new ErrorResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
    }
}

package com.chitra.demospringsecurity.controller;

import com.chitra.demospringsecurity.Security.JwtUtil;
import com.chitra.demospringsecurity.UserServiceClass;
import com.chitra.demospringsecurity.configureCustom.MyUserDetails;
import com.chitra.demospringsecurity.model.AuthenticateRequest;
import com.chitra.demospringsecurity.model.AuthenticateResponse;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserServiceClass userServiceClass;
    @Autowired
    JwtUtil jwtTokenUtil;

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String sayhelloadmin(){
        return "Hello admin";
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String sayhellouser(){
        return "Hello user";
    }

    @RequestMapping(value = "/authenticate",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequest authenticateRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("bad credentioals",e);
        }
        final UserDetails userDetails = userServiceClass
                .loadUserByUsername(authenticateRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticateResponse(jwt));

    }
}

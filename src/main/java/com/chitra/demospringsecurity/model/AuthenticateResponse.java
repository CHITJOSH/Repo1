package com.chitra.demospringsecurity.model;

public class AuthenticateResponse {
    final String jwt;


    public AuthenticateResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}

package com.chitra.demospringsecurity;

import com.chitra.demospringsecurity.Security.JwtUtil;
import com.chitra.demospringsecurity.configureCustom.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Myfilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserServiceClass userServiceClass;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String user = null;
        String jwt = null;
        String strHeader = httpServletRequest.getHeader("Authorization");
        if (strHeader != null && strHeader.startsWith("Bearer ")) {
            jwt = strHeader.substring(7);
            user = jwtUtil.extractUsername(jwt);
        }
        System.out.println("SecurityContextHolder.getContext().getAuthentication() is chitra = "+SecurityContextHolder.getContext().getAuthentication());
        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userServiceClass.loadUserByUsername(user);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.
                            setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
        }

    }


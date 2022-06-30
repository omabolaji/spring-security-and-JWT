package com.famousb.securityjwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${spring.secret.token}")
    private String app_secret;

    @Value("${spring.expiry.time}")
    private int app_secret_expiry_time;

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter (AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //todo: attempt when the user want to login and get the credentials
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("user email {} ", email);
        log.info("user password {} ", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //todo: check whenever the use was successfully authenticated and get the credential
        User user = (User) authResult.getPrincipal();

        //todo: call JWT algorithm
        String appSecret = "qw&330$er1@kjg"; //todo: this can be access from application.properties
        int timeMill = 3600; //todo: this can be access from application.properties
        Algorithm algorithm = Algorithm.HMAC256(appSecret.getBytes());

        //todo: generate the access token whenever the user authentication was success
        String access_token = JWT.create()
                .withSubject(user.getUsername())
//                .withSubject(String.format("%s,%s",user.getUsername(),user.getAuthorities()))
                .withExpiresAt(new Date(System.currentTimeMillis() + timeMill * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        //todo: generate the refresh token whenever the user authentication was success
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + timeMill * 1000)) // you can also increase the expiry time
                .withIssuer(request.getRequestURI())
                .sign(algorithm);

        //todo: set the header for both token
//        response.setHeader("access_token", access_token);
//        response.setHeader("refresh_token", refresh_token);
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}

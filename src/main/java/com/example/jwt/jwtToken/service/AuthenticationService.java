package com.example.jwt.jwtToken.service;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticationService {

    @Value("${authentication.api.url}")
    private String authenticationApiUrl;

    public String authenticateUser(String loginId, String password) {
        RestTemplate restTemplate = new RestTemplate();


        // Create the request body and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("{\"login_id\": \"" + loginId + "\", \"password\": \"" + password + "\"}", headers);

        // Send the POST request to the authentication API
        ResponseEntity<String> response = restTemplate.exchange(authenticationApiUrl, HttpMethod.POST, request, String.class);


        Gson gson = new Gson();
        AuthenticationResponse response1 = gson.fromJson(response.getBody(),AuthenticationResponse.class);
        // Extract the Bearer token from the response
        String bearerToken = response1.getToken();

        return bearerToken;
    }

    // Class to represent the response from the authentication API
    private static class AuthenticationResponse {
        @JsonProperty("access_token")
        private String token;

        // Getters and setters

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
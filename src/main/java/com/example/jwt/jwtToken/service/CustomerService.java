package com.example.jwt.jwtToken.service;

import com.example.jwt.jwtToken.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {

    @Value("${customer.create.api.url}")
    private String customerCreateApiUrl;

    @Value("${customer.list.api.url}")
    private String customerListApiUrl;

    @Value("${customer.delete.api.url}")
    private String customerDeleteApiUrl;

    @Value("${customer.update.api.url}")
    private String customerUpdateApiUrl;




    public void createCustomer(String firstName, String lastName, String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request body and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        // Create the request body with mandatory parameters
        CustomerRequest customerRequest = new CustomerRequest(firstName, lastName);

        // Send the POST request to the customer creation API
        HttpEntity<CustomerRequest> request = new HttpEntity<>(customerRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(customerCreateApiUrl, HttpMethod.POST, request, String.class);

        // Check the response status for success or failure
        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Customer successfully created.");
        } else {
            System.out.println("Failed to create customer. Status: " + response.getStatusCodeValue());
        }
    }

    public List<Customer> getCustomerList(String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        // Create the request entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send the GET request to the customer list API
        ResponseEntity<Customer[]> response = restTemplate.exchange(customerListApiUrl, HttpMethod.GET, requestEntity, Customer[].class);

        // Parse the response body into a list of customers
        List<Customer> customers = Arrays.asList(response.getBody());

        return customers;
    }


    public boolean deleteCustomer(String uuid, String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        // Create the request body with cmd and uuid parameters
        String requestBody = "{\"cmd\": \"delete\", \"uuid\": \"" + uuid + "\"}";

        // Create the request entity with body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the customer deletion API
        ResponseEntity<Void> response = restTemplate.exchange(
                customerDeleteApiUrl,
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        // Check the response status for success or failure
        return response.getStatusCode() == HttpStatus.OK;
    }

    public boolean updateCustomer(String uuid, Customer customer, String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        // Create the request body with cmd, uuid, and customer details
        String requestBody = "{\"cmd\": \"update\", \"uuid\": \"" + uuid + "\", \"first_name\": \"" + customer.getFirst_name() +
                "\", \"last_name\": \"" + customer.getLast_name() + "\", \"street\": \"" + customer.getStreet() +
                "\", \"address\": \"" + customer.getAddress() + "\", \"city\": \"" + customer.getCity() +
                "\", \"state\": \"" + customer.getState() + "\", \"email\": \"" + customer.getEmail() +
                "\", \"phone\": \"" + customer.getPhone() + "\"}";

        // Create the request entity with body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the customer update API
        ResponseEntity<Void> response = restTemplate.exchange(
                customerUpdateApiUrl,
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        // Check the response status for success or failure
        return response.getStatusCode() == HttpStatus.OK;
    }


    // Inner class to represent the request body for customer creation
    private static class CustomerRequest {
        private String first_name;
        private String last_name;

        // Constructor

        public CustomerRequest(String first_name, String last_name) {
            this.first_name = first_name;
            this.last_name = last_name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }
    }
}
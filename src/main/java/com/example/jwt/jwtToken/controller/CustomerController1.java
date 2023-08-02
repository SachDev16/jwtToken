package com.example.jwt.jwtToken.controller;

import com.example.jwt.jwtToken.model.Customer;
import com.example.jwt.jwtToken.service.AuthenticationService;
import com.example.jwt.jwtToken.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController1 {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;

    public CustomerController1(AuthenticationService authenticationService, CustomerService customerService) {
        this.authenticationService = authenticationService;
        this.customerService = customerService;
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        String loginId = "test@sunbasedata.com";
        String password = "Test@123";

        // Authenticate the user and get the bearer token
        String bearerToken = authenticationService.authenticateUser(loginId, password);

        // Create the new customer
        customerService.createCustomer(customerRequestDto.getFirst_name(), customerRequestDto.getLast_name(), bearerToken);

        return ResponseEntity.ok("Customer creation request sent.");
    }


    @GetMapping("/getCustomerList")
    public ResponseEntity<List<Customer>> getCustomerList() {
        String loginId = "test@sunbasedata.com";
        String password = "Test@123";

        // Authenticate the user and get the bearer token
        String bearerToken = authenticationService.authenticateUser(loginId, password);

        // Get the list of customers
        List<Customer> customers = customerService.getCustomerList(bearerToken);

        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/deleteCustomer/{uuid}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String uuid) {
        String loginId = "test@sunbasedata.com";
        String password = "Test@123";

        // Authenticate the user and get the bearer token
        String bearerToken = authenticationService.authenticateUser(loginId, password);

        // Delete the customer with the specified UUID
        boolean isDeleted = customerService.deleteCustomer(uuid, bearerToken);

        if (isDeleted) {
            return ResponseEntity.ok("Successfully deleted the customer.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the customer.");
        }
    }

    @PostMapping("/updateCustomer/{uuid}")
    public ResponseEntity<String> updateCustomer(@PathVariable String uuid, @RequestBody Customer customer) {
        String loginId = "test@sunbasedata.com";
        String password = "Test@123";

        // Authenticate the user and get the bearer token
        String bearerToken = authenticationService.authenticateUser(loginId, password);

        // Update the customer with the specified UUID and new details
        boolean isUpdated = customerService.updateCustomer(uuid, customer, bearerToken);

        if (isUpdated) {
            return ResponseEntity.ok("Successfully updated the customer.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the customer.");
        }
    }

    // DTO class to represent the request body for creating a new customer
    private static class CustomerRequestDto {
        private String first_name;
        private String last_name;

        // Getters and setters

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
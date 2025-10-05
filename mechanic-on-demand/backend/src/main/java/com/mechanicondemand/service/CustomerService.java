package com.mechanicondemand.service;

import com.mechanicondemand.model.Customer;
import com.mechanicondemand.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 👤 Customer Service
 * 
 * This class contains all business logic related to customers.
 * It implements UserDetailsService for Spring Security authentication.
 */
@Service
public class CustomerService implements UserDetailsService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new customer
     */
    public Customer registerCustomer(Customer customer) {
        // Check if customer already exists
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
        }
        
        // Encrypt password before saving
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        
        // Save customer to database
        return customerRepository.save(customer);
    }
    
    /**
     * Find customer by email
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    /**
     * Find customer by ID
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Update customer information
     */
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    /**
     * Delete customer
     */
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    
    /**
     * Authenticate customer (check email and password)
     */
    public Customer authenticateCustomer(String email, String password) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return customer;
            }
        }
        return null;
    }
    
    /**
     * Required by UserDetailsService for Spring Security
     * This method is called when Spring Security needs to load user details
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
        
        // Return UserDetails object for Spring Security
        return new User(
            customer.getEmail(),
            customer.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
    }
}
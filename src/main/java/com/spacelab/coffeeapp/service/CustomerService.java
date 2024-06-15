package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CustomerService {

    void saveCustomer(Customer customer);
    Customer getCustomer(Long id);
    List<Customer> getAllCustomer();
    void updateCustomer(Long id, Customer customer);
    void deleteCustomer(Customer customer);
    void deleteCustomer(Long id);
    Page<Customer> findAllCustomer(int page, int pageSize);
    Page<Customer> findCustomerByRequest(int page, int pageSize, String search);
}

package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CustomerService {

    void saveCustomer(Customer customer);
    void saveCustomer(CustomerDto customerDto);
    Customer getCustomer(Long id);

    CustomerDto getCustomerDto(Long id);
    List<Customer> getAllCustomer();
    void updateCustomer(Long id, Customer customer);

    void updateCustomer(Long id, CustomerDto customerDto);
    void deleteCustomer(Customer customer);
    void deleteCustomer(Long id);
    Page<Customer> findAllCustomer(int page, int pageSize);
    Page<Customer> findCustomerByRequest(int page, int pageSize, String search);

    long countCustomers();

    Integer countAllCustomers();

    Integer countTodayNewCustomers();

    Long getCountOfCustomersWithOrdersLastWeek();


    Page<CustomerDto> findCustomersPageByRequest(int page, Integer size, String search);
}

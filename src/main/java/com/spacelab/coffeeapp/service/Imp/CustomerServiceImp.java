package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import com.spacelab.coffeeapp.service.CustomerService;
import com.spacelab.coffeeapp.specification.CustomerSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;


    @Override
    public void saveCustomer(Customer customer) {
        log.info("Save customer: {}", customer);
        customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        log.info("Get customer by id: {}", id);
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(Long id, Customer customer) {
        customerRepository.findById(id).map(customer1 -> {
            customer1.setName(customer.getName());
            customer1.setEmail(customer.getEmail());
            customer1.setAddress(customer.getAddress());
            customer1.setPhoneNumber(customer.getPhoneNumber());
            customer1.setLanguage(customer.getLanguage());
            customer1.setDateOfBirth(customer.getDateOfBirth());
            customer1.setStatus(customer.getStatus());
            customerRepository.save(customer1);
            return customer1;
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public void deleteCustomer(Customer customer) {
        log.info("Delete customer: {}", customer);
        customerRepository.delete(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Delete customer by id: {}", id);
        customerRepository.deleteById(id);
    }

    @Override
    public Page<Customer> findAllCustomer(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get customers with pageable: {}", pageable);
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findCustomerByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Customer> specification = new CustomerSpecification(search);
        log.info("Get customers by request: {}", search);
        return customerRepository.findAll(specification, pageable);
    }

    @Override
    public long countCustomers() {
        return customerRepository.count();
    }

    @Override
    public Integer countAllCustomers() {
        return customerRepository.countAllCustomers();
    }

    @Override
    public Integer countTodayNewCustomers() {
        return customerRepository.countTodayNewCustomers();
    }

    @Override
    public Long getCountOfCustomersWithOrdersLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return customerRepository.countUniqueCustomersWithOrdersSince(oneWeekAgo);
    }

    @Override
    public Double calculateChangesLastWeek() {
        return (double) 0;
    }
}

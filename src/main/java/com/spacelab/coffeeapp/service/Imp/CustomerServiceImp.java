package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.mapper.CustomerMapper;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import com.spacelab.coffeeapp.service.CustomerService;
import com.spacelab.coffeeapp.service.DeliveryService;
import com.spacelab.coffeeapp.service.OrderService;
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
    private final CustomerMapper customerMapper;
    private final OrderService orderService;
    private final DeliveryService deliveryService;



    @Override
    public void saveCustomer(Customer customer) {
        log.info("Save customer: {}", customer);
        customerRepository.save(customer);
    }

    @Override
    public void saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        log.info("Get customer by id: {}", id);
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerDto getCustomerDto(Long id) {
        return customerMapper.toDto(getCustomer(id));
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(Long id, Customer customer) {
        customerRepository.findById(id).map(customer1 -> {
            customer1.setName(customer.getName().trim());
            customer1.setEmail(customer.getEmail());
            customer1.setPhoneNumber(customer.getPhoneNumber());
            customer1.setLanguage(customer.getLanguage());
            customer1.setDateOfBirth(customer.getDateOfBirth());
            customer1.setStatus(customer.getStatus());
            customerRepository.save(customer1);
            return customer1;
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public void updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        updateCustomer(id, customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        Customer customer1 = customerRepository.findById(customer.getId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Order> orderList = orderService.CancelAllOrdersByCustomer(customer1);
        customer1.setDeleted(true);
        customerRepository.save(customer1);
        log.info("Delete customer: {}", customer);

    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer1 = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Order> orderList = orderService.CancelAllOrdersByCustomer(customer1);
        customer1.setDeleted(true);
        customerRepository.save(customer1);
        log.info("Delete customer by id: {}", id);
    }



    @Override
    public Page<Customer> findAllCustomer(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get customers with pageable: {}", pageable);
        return customerRepository.findAll(CustomerSpecification.byNotDeleted(), pageable);
    }

    @Override
    public Page<Customer> findCustomerByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get customers by request: {}", CustomerSpecification.search(search));
        return customerRepository.findAll(CustomerSpecification.search(search).and(CustomerSpecification.byNotDeleted()), pageable);
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
    public Page<CustomerDto> findCustomersPageByRequest(int page, Integer size, String search) {
        if (search.isEmpty()) {
            return customerMapper.toDtoListPage(findAllCustomer(page, size));
        } else {
            return customerMapper.toDtoListPage(findCustomerByRequest(page, size, search));
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.getCustomerByEmail(email);
    }

    @Override
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
}

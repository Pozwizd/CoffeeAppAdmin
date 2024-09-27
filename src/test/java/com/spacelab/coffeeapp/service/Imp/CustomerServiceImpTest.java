package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.mapper.CustomerMapper;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerServiceImpTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderServiceImp orderService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImp customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
    }

    @Test
    void testSaveCustomer_Entity() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.saveCustomer(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testSaveCustomer_Dto() {
        when(customerMapper.toEntity(any(CustomerDto.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.saveCustomer(customerDto);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomer(1L);
        assertNotNull(foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomer_NotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.getCustomer(1L));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void testGetCustomerDto() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto foundCustomerDto = customerService.getCustomerDto(1L);
        assertNotNull(foundCustomerDto);
        assertEquals(customerDto.getId(), foundCustomerDto.getId());
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.getAllCustomer();
        assertEquals(1, customers.size());
    }

    @Test
    void testUpdateCustomer_Entity() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.updateCustomer(1L, customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomer_Dto() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerMapper.toEntity(any(CustomerDto.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.updateCustomer(1L, customerDto);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteCustomer() {
        // Arrange
        Customer customer = Customer.builder().id(1L).name("John Doe").build();
        Customer customer1 = Customer.builder().id(1L).name("John Doe").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        List<Order> orders = new ArrayList<>();
        when(orderService.CancelAllOrdersByCustomer(customer1)).thenReturn(orders);

        // Act
        customerService.deleteCustomer(customer);

        // Assert
        verify(customerRepository, times(1)).findById(1L);
        verify(orderService, times(1)).CancelAllOrdersByCustomer(customer1);
        verify(customerRepository, times(1)).save(customer1);
        assertTrue(customer1.isDeleted());
    }

    @Test
    void testDeleteCustomer_ById() {
        // Arrange
        Customer customer = Customer.builder().id(1L).name("John Doe").build();
        Customer customer1 = Customer.builder().id(1L).name("John Doe").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        List<Order> orders = new ArrayList<>();
        when(orderService.CancelAllOrdersByCustomer(customer1)).thenReturn(orders);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).findById(1L);
        verify(orderService, times(1)).CancelAllOrdersByCustomer(customer1);
        verify(customerRepository, times(1)).save(customer1);
        assertTrue(customer1.isDeleted());
    }

    @Test
    void testFindAllCustomers() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(customerPage);

        Page<Customer> result = customerService.findAllCustomer(0, 10);
        assertNotNull(result);
    }

    @Test
    void testFindCustomerByRequest() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);

        Page<Customer> result = customerService.findCustomerByRequest(0, 10, "John");
        assertNotNull(result);
    }

    @Test
    void testCountCustomers() {
        when(customerRepository.count()).thenReturn(1L);
        long count = customerService.countCustomers();
        assertEquals(1, count);
    }

    @Test
    void testCountAllCustomers() {
        when(customerRepository.countAllCustomers()).thenReturn(1);
        int count = customerService.countAllCustomers();
        assertEquals(1, count);
    }

    @Test
    void testCountTodayNewCustomers() {
        when(customerRepository.countTodayNewCustomers()).thenReturn(1);
        int count = customerService.countTodayNewCustomers();
        assertEquals(1, count);
    }

    @Test
    void testGetCountOfCustomersWithOrdersLastWeek() {
        when(customerRepository.countUniqueCustomersWithOrdersSince(any(LocalDateTime.class))).thenReturn(1L);
        long count = customerService.getCountOfCustomersWithOrdersLastWeek();
        assertEquals(1, count);
    }

    @Test
    void testFindCustomersPageByRequest_NoSearch() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(customerDto)));

        Page<CustomerDto> result = customerService.findCustomersPageByRequest(0, 10, "");
        assertNotNull(result);
        verify(customerMapper, times(1)).toDtoListPage(any(Page.class));
    }

    @Test
    void testFindCustomersPageByRequest_WithSearch() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(customerDto)));

        Page<CustomerDto> result = customerService.findCustomersPageByRequest(0, 10, "John");
        assertNotNull(result);
        verify(customerMapper, times(1)).toDtoListPage(any(Page.class));
    }
}

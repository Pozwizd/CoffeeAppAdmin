package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    Customer getCustomerByEmail(@NotEmpty(message = "Поле не может быть пустым") @Size(max=100, message = "Размер поля должен быть не более 50 символов") @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Неверный формат email") String email);

    Customer getCustomerByPhoneNumber(@NotEmpty(message = "Поле не может быть пустым") @Size(max=13, message = "Размер номера должен быть не более 13 символов") @Pattern(regexp = "\\+380(50|66|95|99|67|68|96|97|98|63|93|73)[0-9]{7}", message = "Неверный формат номера") String phoneNumber);
}

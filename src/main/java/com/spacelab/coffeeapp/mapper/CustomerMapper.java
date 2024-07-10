package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.CustomerStatus;
import com.spacelab.coffeeapp.entity.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerMapper {

    public Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setLanguage(Language.valueOf(customerDto.getLanguage()));
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setStatus(CustomerStatus.valueOf(customerDto.getStatus()));
        return customer;
    }

    public CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setLanguage(customer.getLanguage().getLanguageName());
        customerDto.setDateOfBirth(customer.getDateOfBirth());
        customerDto.setStatus(customer.getStatus().toString());
        return customerDto;
    }

}

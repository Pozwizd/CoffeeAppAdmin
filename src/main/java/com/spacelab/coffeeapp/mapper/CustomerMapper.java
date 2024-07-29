package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.CustomerStatus;
import com.spacelab.coffeeapp.entity.Language;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Page<CustomerDto> toDtoListPage(Page<Customer> customers) {
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer l : customers.getContent()) {
            customerDtos.add(toDto(l));
        }
        return new PageImpl<>(customerDtos, customers.getPageable(), customers.getTotalElements());
    }

    public List<Customer> toEntityListPage(List<CustomerDto> customerDtos) {
        List<Customer> locationDtoList = new ArrayList<>();
        for (CustomerDto l : customerDtos) {
            locationDtoList.add(toEntity(l));
        }
        return locationDtoList;
    }
}

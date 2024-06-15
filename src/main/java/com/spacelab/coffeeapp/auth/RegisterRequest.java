package com.spacelab.coffeeapp.auth;


import com.spacelab.coffeeapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String email;
  private String password;
  private Role role;
}

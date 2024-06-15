package com.spacelab.coffeeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "city")
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;

  private String postalCode;

  private String region;

  @JsonIgnore
  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<Location> locations = new ArrayList<>();

}
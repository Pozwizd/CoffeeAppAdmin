package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "attribute_product")
public class AttributeProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private TypeAttribute type;

  @ManyToMany(mappedBy = "attributeProducts")
  private List<Product> products = new ArrayList<>();

  @OneToMany(mappedBy = "attributeProduct")
  private List<AttributeValue> attributeValues = new ArrayList<>();

  private Boolean deleted = false;

  private Boolean status = true;

  public enum TypeAttribute {
    Basic,
    Option
  }
}
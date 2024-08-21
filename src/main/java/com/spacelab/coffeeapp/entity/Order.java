package com.spacelab.coffeeapp.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'TT'HH:mm")
    private LocalDateTime dateTimeOfCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'TT'HH:mm")
    private LocalDateTime dateTimeOfReady;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private double totalAmount = 0;

    private boolean deleted = false;

    public enum OrderStatus {
        NEW,
        PAYMENT_WAITING,
        IN_PROGRESS,
        DELIVERING,
        DONE,
        CANCELLED
    }

    public enum Payment {
        CASH,
        CARD
    }

    @PrePersist
    @PreUpdate
    @PostPersist
    @PostConstruct
    @PostLoad
    public void updateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

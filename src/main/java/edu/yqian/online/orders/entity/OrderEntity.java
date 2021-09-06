package edu.yqian.online.orders.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "MY_ORDER")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductEntity> productList;

    private double total;
}

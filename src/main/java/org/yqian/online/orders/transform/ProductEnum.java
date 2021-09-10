package org.yqian.online.orders.transform;

import lombok.Getter;

@Getter
public enum ProductEnum {
    APPLE("apple", 0.6),
    ORANGE("orange", 0.25);

    private final String name;
    private final double price;

    ProductEnum(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
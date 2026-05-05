package com.coffee.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Fruit {
    private String id;
    private String name;
    private int price;

    public Fruit(){}

    public Fruit(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

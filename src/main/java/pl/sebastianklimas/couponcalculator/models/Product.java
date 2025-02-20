package pl.sebastianklimas.couponcalculator.models;

import java.util.Objects;

public class Product {
    private double price;
    private String name;

    public Product() {
    }

    public Product(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product - " + name + " - " + price + "PLN";
    }
}

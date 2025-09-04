package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;

public class Product {
    private BigDecimal price;
    private String name;

    public Product() {
    }

    public Product(BigDecimal price, String name) {
        this.price = price;
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
//    public String toString() {
//        return "Product - " + name + " - " + price + "PLN";
//    }
    public String toString() {
        return name;
    }
}

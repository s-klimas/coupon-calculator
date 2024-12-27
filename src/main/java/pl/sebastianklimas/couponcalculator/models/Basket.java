package pl.sebastianklimas.couponcalculator.models;

import java.util.Set;

public class Basket {
    private Set<Product> products;
    private double sumPrice = 0;

    public Basket() {
    }

    public Basket(Set<Product> products) {
        this.products = products;
        calculateSumPrice();
    }

    public Basket(Set<Product> products, double sumPrice) {
        this.products = products;
        this.sumPrice = sumPrice;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void calculateSumPrice() {
        products.forEach(product -> sumPrice += product.getPrice());
    }
}

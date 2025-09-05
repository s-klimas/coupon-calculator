package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;
import java.util.Set;

public class Subset {
    private Set<Product> products;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Subset(Set<Product> products) {
        this.products = products;
        calculateTotalPrice();
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void calculateTotalPrice() {
        products.forEach(product -> totalPrice = totalPrice.add(product.getPrice()));
    }
}

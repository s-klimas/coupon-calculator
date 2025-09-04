package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;

public class Basket {
    private ProductSet products;
    private BigDecimal sumPrice = BigDecimal.ZERO;

    public Basket() {
    }

    public Basket(ProductSet products) {
        this.products = products;
        calculateSumPrice();
    }

    public Basket(ProductSet products, BigDecimal sumPrice) {
        this.products = products;
        this.sumPrice = sumPrice;
    }

    public ProductSet getProducts() {
        return products;
    }

    public void setProducts(ProductSet products) {
        this.products = products;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void calculateSumPrice() {
        products.getProducts().forEach(product -> sumPrice = sumPrice.add(product.getPrice()));
    }

    @Override
    public String toString() {
        return "Basket = {" + products + " sum: " + sumPrice + "}";
    }
}

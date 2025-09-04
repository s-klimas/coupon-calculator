package pl.sebastianklimas.couponcalculator.models;

import java.util.HashSet;
import java.util.Set;

public class ProductSet {
    Set<Product> products = new HashSet<>();

    public ProductSet() {
    }

    public ProductSet(Set<Product> products) {
        this.products = products;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public String toString() {
        return "ProductSet = {" + products.toString() + "}";
    }

    public void print() {
        System.out.print(products);
    }
}

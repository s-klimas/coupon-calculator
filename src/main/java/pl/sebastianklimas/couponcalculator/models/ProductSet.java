package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Packaging class for a set of products")
public class ProductSet {
    Set<Product> products = new HashSet<>();

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

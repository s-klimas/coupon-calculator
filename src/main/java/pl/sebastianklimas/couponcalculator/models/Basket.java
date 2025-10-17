package pl.sebastianklimas.couponcalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Basket {
    private ProductSet products;
    private BigDecimal sumPrice = BigDecimal.ZERO;

    public Basket(ProductSet products) {
        this.products = products;
        calculateSumPrice();
    }

    public void calculateSumPrice() {
        products.getProducts().forEach(product -> sumPrice = sumPrice.add(product.getPrice()));
    }

    @Override
    public String toString() {
        return "Basket = {" + products + " sum: " + sumPrice + "}";
    }
}

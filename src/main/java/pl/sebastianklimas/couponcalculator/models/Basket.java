package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "A helper class representing a basket of products and the total sum of the basket")
public class Basket {
    private ProductSet products;
    @Schema(description = "The total sum of the basket")
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

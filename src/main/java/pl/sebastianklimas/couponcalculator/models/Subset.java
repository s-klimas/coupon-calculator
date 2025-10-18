package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Packaging class for a set of products with calculated total sum")
public class Subset {
    private Set<Product> products;
    @Schema(description = "The total price of products in set")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Subset(Set<Product> products) {
        this.products = products;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        products.forEach(product -> totalPrice = totalPrice.add(product.getPrice()));
    }
}

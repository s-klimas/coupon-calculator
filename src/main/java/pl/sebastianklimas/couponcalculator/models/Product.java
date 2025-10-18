package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Schema(description = "The class containing information about product")
public class Product {
    @Schema(name = "price", example = "50", description = "The price of the product")
    private BigDecimal price;
    @Schema(name = "name", example = "Product 1", description = "The name of the product")
    private String name;

    @Override
    public String toString() {
        return "Product - " + name + " - " + price + "PLN";
    }
}

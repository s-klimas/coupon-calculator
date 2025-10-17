package pl.sebastianklimas.couponcalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Product {
    private BigDecimal price;
    private String name;

    @Override
    public String toString() {
        return "Product - " + name + " - " + price + "PLN";
    }
}

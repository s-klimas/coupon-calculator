package pl.sebastianklimas.couponcalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FullShoppingList {
    List<ProductSet> productSets;

    @Override
    public String toString() {
        return "FullShoppingList = {" + productSets + "}";
    }
}

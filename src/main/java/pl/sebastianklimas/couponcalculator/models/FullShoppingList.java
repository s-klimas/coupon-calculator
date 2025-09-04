package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class FullShoppingList {
    List<ProductSet> productSets;

    public FullShoppingList(List<ProductSet> productSets) {
        this.productSets = productSets;
    }

    public List<ProductSet> getProductSets() {
        return productSets;
    }

    @Override
    public String toString() {
        return "FullShoppingList = {" + productSets + "}";
    }
}

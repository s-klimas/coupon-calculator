package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class FinalShoppingListDto {
    private List<List<BasketCoupon>> shoppingList;

    public FinalShoppingListDto(List<List<BasketCoupon>> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<List<BasketCoupon>> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<List<BasketCoupon>> shoppingList) {
        this.shoppingList = shoppingList;
    }
}

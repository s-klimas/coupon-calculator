package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class FinalShoppingListDto {
    private List<FullShoppingListWithCoupon> shoppingListsWithCoupons;

    public FinalShoppingListDto(List<FullShoppingListWithCoupon> shoppingList) {
        this.shoppingListsWithCoupons = shoppingList;
    }

    public List<FullShoppingListWithCoupon> getShoppingListsWithCoupons() {
        return shoppingListsWithCoupons;
    }
}

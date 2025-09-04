package pl.sebastianklimas.couponcalculator.models;

import java.util.ArrayList;
import java.util.List;

public class FinalShoppingListDto {
    private List<FullShoppingListWithCoupon> shoppingListsWithCoupons;

    public FinalShoppingListDto() {
    }

    public FinalShoppingListDto(List<FullShoppingListWithCoupon> shoppingList) {
        this.shoppingListsWithCoupons = shoppingList;
    }

    public List<FullShoppingListWithCoupon> getShoppingListsWithCoupons() {
        return shoppingListsWithCoupons;
    }

    public List<Coupon> getAllUsedCoupons() {
        List<Coupon> usedCoupons = new ArrayList<>();

        shoppingListsWithCoupons
                .forEach(shoppingListWithCoupon -> shoppingListWithCoupon.getBasketCoupons()
                        .forEach(basketCoupon -> usedCoupons.add(basketCoupon.getCoupon())));

        return usedCoupons;
    }
}

package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "List of baskets with an assigned coupons containing all products from the initial product list")
public class FullShoppingListWithCoupon {
    private List<BasketCoupon> basketCoupons;
    @Schema(description = "The total price of all products on the list")
    private BigDecimal totalPrice;

    public FullShoppingListWithCoupon(List<BasketCoupon> basketCoupons) {
        this.basketCoupons = basketCoupons;
    }

    public void calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BasketCoupon basketCoupon : basketCoupons) {
            sum = sum.add(basketCoupon.getFinalSum());
        }
        totalPrice = sum;
    }

    @Schema(description = "The list of coupons used in this shopping list")
    public List<Coupon> getAllUsedCoupons() {
        List<Coupon> usedCoupons = new ArrayList<>();

        basketCoupons.forEach(basketCoupon -> usedCoupons.add(basketCoupon.getCoupon()));

        return usedCoupons;
    }

    @Override
    public String toString() {
        return "FullShoppingListWithCoupon = { " + "basketCoupons = " + basketCoupons + " }";
    }
}

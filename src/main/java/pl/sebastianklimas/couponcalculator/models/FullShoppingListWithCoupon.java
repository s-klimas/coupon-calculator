package pl.sebastianklimas.couponcalculator.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FullShoppingListWithCoupon {
    private List<BasketCoupon> basketCoupons;
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

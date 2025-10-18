package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "A helper class containing a basket with an assigned coupon, and the final sum including the discount")
public class BasketCoupon {
    private Basket basket;
    private Coupon coupon;
    @Schema(description = "The final sum of the basket with this coupon")
    private BigDecimal finalSum;

    public BasketCoupon(Basket basket) {
        this.basket = basket;
    }

    public BasketCoupon(Basket basket, Coupon coupon) {
        this.basket = basket;
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        return "BasketCoupon " + basket.getProducts().getProducts().stream().map(Product::getName).collect(Collectors.joining()) + " ( " + basket.getSumPrice() + ") KUPOPN: " + (coupon == null ? "<<BRAK>>" : coupon.getCode()) + " Final sum: " + finalSum;
    }

    public void calculateFinalSum() {
        if (coupon == null || basket.getSumPrice().compareTo(coupon.getMinPrice()) < 0) finalSum = basket.getSumPrice();
        else {
            BigDecimal proposedDiscount = basket.getSumPrice().multiply(BigDecimal.valueOf(coupon.getPercentDiscount() / 100.0));
            BigDecimal discount = proposedDiscount.compareTo(coupon.getMaxDiscount()) <= 0 ? proposedDiscount : coupon.getMaxDiscount();
            finalSum = basket.getSumPrice().subtract(discount);
        }
    }
}

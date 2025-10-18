package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "A helper class containing a Subset with an assigned coupon, and the final sum including the discount")
public class PotentialCart {
    private Subset subset;
    private Coupon coupon;
    @Schema(description = "The total sum of the potential cart with discount applied")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public PotentialCart(Subset subset) {
        this.subset = subset;
        calculateTotalPrice();
    }

    public PotentialCart(Subset subset, Coupon coupon) {
        this.subset = subset;
        this.coupon = coupon;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        if (coupon == null || subset.getTotalPrice().compareTo(coupon.getMinPrice()) < 0) totalPrice = subset.getTotalPrice();
        else {
            BigDecimal proposedDiscount = subset.getTotalPrice().multiply(BigDecimal.valueOf(coupon.getPercentDiscount() / 100.0));
            BigDecimal discount = proposedDiscount.compareTo(coupon.getMaxDiscount()) <= 0 ? proposedDiscount : coupon.getMaxDiscount();
            totalPrice = subset.getTotalPrice().subtract(discount);
        }
    }
}

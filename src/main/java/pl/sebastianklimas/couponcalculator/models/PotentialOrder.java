package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Schema(description = "List of potential carts with an assigned coupons containing all products from the initial product list ")
public class PotentialOrder {
    private List<PotentialCart> potentialCarts;
    @Schema(description = "The total sum of the order")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public PotentialOrder(List<PotentialCart> potentialCarts) {
        this.potentialCarts = potentialCarts;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalPrice = potentialCarts.stream()
                .map(PotentialCart::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Coupon> findAllUsedCoupons() {
        List<Coupon> usedCoupons = new ArrayList<>();

        potentialCarts.forEach(potentialCart -> usedCoupons.add(potentialCart.getCoupon()));

        return usedCoupons;
    }
}

package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PotentialOrder {
    private List<PotentialCart> potentialCarts;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public PotentialOrder(List<PotentialCart> potentialCarts) {
        this.potentialCarts = potentialCarts;
        calculateTotalPrice();
    }

    public List<PotentialCart> getPotentialCarts() {
        return potentialCarts;
    }

    public void setPotentialCarts(List<PotentialCart> potentialCarts) {
        this.potentialCarts = potentialCarts;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

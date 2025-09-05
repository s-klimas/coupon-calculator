package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;

public class PotentialCart {
    private Subset subset;
    private Coupon coupon;
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

    public Subset getSubset() {
        return subset;
    }

    public void setSubset(Subset subset) {
        this.subset = subset;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

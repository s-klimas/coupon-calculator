package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Coupon {
    private BigDecimal minPrice;
    private BigDecimal maxDiscount;
    private int percentDiscount;
    private String code;

    public Coupon() {
    }

    public Coupon(BigDecimal minPrice, BigDecimal maxDiscount, int percentDiscount, String code) {
        this.minPrice = minPrice;
        this.maxDiscount = maxDiscount;
        this.percentDiscount = percentDiscount;
        this.code = code;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return percentDiscount == coupon.percentDiscount && Objects.equals(minPrice, coupon.minPrice) && Objects.equals(maxDiscount, coupon.maxDiscount) && Objects.equals(code, coupon.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minPrice, maxDiscount, percentDiscount, code);
    }

    @Override
    public String toString() {
        return "Coupon " + code + " minimal price - " + minPrice + " max discount - " + maxDiscount + " percent " + percentDiscount + "%";
    }
}

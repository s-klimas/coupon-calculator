package pl.sebastianklimas.couponcalculator.models;

import java.util.Objects;

public class Coupon {
    private double minPrice;
    private double maxDiscount;
    private int percentDiscount;
    private String code;

    public Coupon() {
    }

    public Coupon(double minPrice, double maxDiscount, int percentDiscount, String code) {
        this.minPrice = minPrice;
        this.maxDiscount = maxDiscount;
        this.percentDiscount = percentDiscount;
        this.code = code;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Double.compare(coupon.minPrice, minPrice) == 0 && Double.compare(coupon.maxDiscount, maxDiscount) == 0 && percentDiscount == coupon.percentDiscount && Objects.equals(code, coupon.code);
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

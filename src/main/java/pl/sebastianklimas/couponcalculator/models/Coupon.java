package pl.sebastianklimas.couponcalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon {
    private BigDecimal minPrice;
    private BigDecimal maxDiscount;
    private int percentDiscount;
    private String code;

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

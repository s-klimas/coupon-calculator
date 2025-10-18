package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "The class containing information about coupon")
public class Coupon {
    @Schema(name = "minPrice", example = "10", description = "Minimum total price of products to use the coupon")
    private BigDecimal minPrice;
    @Schema(name = "maxDiscount", example = "1000", description = "The maximum discount you can get with coupon")
    private BigDecimal maxDiscount;
    @Schema(name = "percentDiscount", example = "50", description = "Percentage discount on the coupon")
    private int percentDiscount;
    @Schema(name = "code", example = "Code 1", description = "The code of the coupon")
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

package pl.sebastianklimas.couponcalculator.models.dtos;

import pl.sebastianklimas.couponcalculator.models.Coupon;

public class CouponDtoMapper {
    public static CouponDto map(Coupon coupon) {
        return new CouponDto(
                coupon.getMinPrice(),
                coupon.getMaxDiscount(),
                coupon.getPercentDiscount(),
                coupon.getCode());
    }

    public static Coupon map(CouponDto couponDto) {
        return new Coupon(
                couponDto.getMinPrice(),
                couponDto.getMaxDiscount(),
                couponDto.getPercentDiscount(),
                couponDto.getCode());
    }
}

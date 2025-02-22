package pl.sebastianklimas.couponcalculator.models.dtos;

public class CouponDto {
    private double minPrice;
    private double maxDiscount;
    private int percentDiscount;
    private String code;

    public CouponDto() {
    }

    public CouponDto(double minPrice, double maxDiscount, int percentDiscount, String code) {
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
}

package pl.sebastianklimas.couponcalculator.models;

import java.util.stream.Collectors;

public class BasketCoupon {
    private Basket basket;
    private Coupon coupon;
    private double finalSum;

    public BasketCoupon() {
    }

    public BasketCoupon(Basket basket, Coupon coupon) {
        this.basket = basket;
        this.coupon = coupon;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public double getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(double finalSum) {
        this.finalSum = finalSum;
    }

    @Override
    public String toString() {
        return "BasketCoupon " + basket.getProducts().stream().map(Product::getName).collect(Collectors.joining()) + " ( " + basket.getSumPrice() + ") KUPOPN: " + (coupon == null ? "<<BRAK>>" : coupon.getCode()) + " Final sum: " + finalSum;
    }

    public void calculateFinalSum() {
        if (coupon == null || basket.getSumPrice() < coupon.getMinPrice()) finalSum = basket.getSumPrice();
        else {
            double proposedDiscount = basket.getSumPrice() * coupon.getPercentDiscount() / 100;
            double discount = Math.min(proposedDiscount, coupon.getMaxDiscount());
            finalSum = basket.getSumPrice() - discount;
        }
    }
}

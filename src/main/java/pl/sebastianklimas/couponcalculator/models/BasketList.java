package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class BasketList {
    private List<Basket> baskets;

    public BasketList(List<Basket> baskets) {
        this.baskets = baskets;
    }

    public List<Basket> getBaskets() {
        return baskets;
    }

    @Override
    public String toString() {
        return "BasketList = { " + baskets + " }";
    }
}

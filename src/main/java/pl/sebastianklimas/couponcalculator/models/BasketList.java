package pl.sebastianklimas.couponcalculator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BasketList {
    private List<Basket> baskets;

    @Override
    public String toString() {
        return "BasketList = { " + baskets + " }";
    }
}

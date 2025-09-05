package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class Combination {
    private List<Subset> subsets;

    public Combination(List<Subset> subsets) {
        this.subsets = subsets;
    }

    public List<Subset> getSubsets() {
        return subsets;
    }

    public void setSubsets(List<Subset> subsets) {
        this.subsets = subsets;
    }
}

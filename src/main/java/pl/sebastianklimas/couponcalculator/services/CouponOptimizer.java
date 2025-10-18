package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class CouponOptimizer {
    public List<PotentialOrder> generateAllPotentialOrders(List<Coupon> coupons,
                                                           List<Combination> combinations) {
        if (coupons == null || combinations == null) {
            throw new IllegalArgumentException("Coupons and combinations must not be null");
        }

        return combinations
                .parallelStream()
                .flatMap(combination ->
                        generateCombinations(combination.getSubsets(), coupons).stream())
                .map(PotentialOrder::new)
                .toList();
    }

    private List<List<PotentialCart>> generateCombinations(List<Subset> subsets,
                                                           List<Coupon> coupons) {
        return backtrack(subsets, coupons, 0, new ArrayList<>(), new HashSet<>());
    }

    private List<List<PotentialCart>> backtrack(List<Subset> subsets,
                                                List<Coupon> coupons,
                                                int subsetIndex,
                                                List<PotentialCart> currentCombination,
                                                Set<Integer> usedCoupons) {
        if (subsetIndex == subsets.size()) {
            return List.of(new ArrayList<>(currentCombination));
        }

        Subset currentSubset = subsets.get(subsetIndex);

        Stream<List<PotentialCart>> withoutCouponStream = Stream.of(currentSubset)
                .map(b -> {
                    List<PotentialCart> newCombination = new ArrayList<>(currentCombination);
                    newCombination.add(new PotentialCart(b));
                    return backtrack(subsets, coupons, subsetIndex + 1, newCombination, new HashSet<>(usedCoupons));
                })
                .flatMap(List::stream);

        Stream<List<PotentialCart>> withCouponStream =
                IntStream.range(0, coupons.size())
                        .filter(i -> !usedCoupons.contains(i))
                        .mapToObj(i -> {
                            List<PotentialCart> newCombination = new ArrayList<>(currentCombination);
                            newCombination.add(new PotentialCart(currentSubset, coupons.get(i)));
                            Set<Integer> newUsed = new HashSet<>(usedCoupons);
                            newUsed.add(i);
                            return backtrack(subsets, coupons, subsetIndex + 1, newCombination, newUsed);
                        })
                        .flatMap(List::stream);

        return Stream.concat(withoutCouponStream, withCouponStream).toList();
    }
}

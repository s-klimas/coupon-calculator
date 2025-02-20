package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.util.*;

@Service
public class APIService {
    public FinalShoppingListDto calculateShoppingList(List<Product> products, List<Coupon> coupons) {
        List<Set<Product>> subset = sublistGenerator(products);

        CouponsProducts couponsProducts = new CouponsProducts();

        subset.forEach(list -> coupons.forEach(coupon -> couponsProducts.addEntryToMap(coupon, list)));

        List<List<Set<Product>>> allCombinations = findAllCombinations(new HashSet<>(products), subset, coupons.size());

        List<List<Basket>> allCombinationsBaskets = allCombinations.stream()
                .map(list -> list.stream()
                        .map(Basket::new)
                        .toList())
                .toList();

        List<List<BasketCoupon>> bcCombinations = matchCouponsToBaskets(coupons, allCombinationsBaskets);

        bcCombinations.forEach(list -> list.forEach(BasketCoupon::calculateFinalSum));

        List<List<BasketCoupon>> bcSorted = bcCombinations.stream()
                .sorted(Comparator.comparing(list -> list.stream()
                        .mapToDouble(BasketCoupon::getFinalSum)
                        .sum()))
                .limit(10)
                .toList();

        return new FinalShoppingListDto(bcSorted);
    }

    private List<Set<Product>> sublistGenerator(List<Product> products) {
        List<Set<Product>> result = new ArrayList<>();
        int totalSubsets = 1 << products.size(); // 2^n
        for (int i = 0; i < totalSubsets; i++) {
            Set<Product> sublist = new HashSet<>();
            for (int j = 0; j < products.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    sublist.add(products.get(j));
                }
            }
            if (!sublist.isEmpty()) {
                result.add(sublist);
            }
        }
        return result;
    }
    private List<List<Set<Product>>> findAllCombinations(Set<Product> fullList,
                                                         List<Set<Product>> subsets,
                                                         int maxSubsets) {
        List<List<Set<Product>>> result = new ArrayList<>();
        generateAllCombinations(subsets, fullList, maxSubsets, new ArrayList<>(), 0, new HashSet<>(), result);
        return result;
    }
    private void generateAllCombinations(List<Set<Product>> subsets,
                                         Set<Product> fullSet,
                                         int maxSubsets,
                                         List<Set<Product>> currentCombination,
                                         int startIndex,
                                         Set<Product> usedElements,
                                         List<List<Set<Product>>> result) {
        if (currentCombination.size() > maxSubsets) return;
        if (usedElements.equals(fullSet)) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }
        for (int i = startIndex; i < subsets.size(); i++) {
            Set<Product> subset = subsets.get(i);

            Set<Product> intersection = new HashSet<>(subset);
            intersection.retainAll(usedElements);
            if (!intersection.isEmpty()) continue;

            currentCombination.add(subset);
            usedElements.addAll(subset);

            generateAllCombinations(subsets, fullSet, maxSubsets, currentCombination, i + 1, usedElements, result);

            currentCombination.remove(currentCombination.size() - 1);
            usedElements.removeAll(subset);
        }
    }

    private static List<List<BasketCoupon>> matchCouponsToBaskets(List<Coupon> coupons, List<List<Basket>> allCombinationsBaskets) {
        List<List<BasketCoupon>> result = new ArrayList<>();

        for (List<Basket> basketCombination : allCombinationsBaskets) {
            List<List<BasketCoupon>> combinations = generateCombinations(basketCombination, coupons);
            result.addAll(combinations);
        }

        return result;
    }

    private static List<List<BasketCoupon>> generateCombinations(List<Basket> baskets, List<Coupon> coupons) {
        List<List<BasketCoupon>> result = new ArrayList<>();
        backtrack(baskets, coupons, 0, new ArrayList<>(), new HashSet<>(), result);
        return result;
    }

    private static void backtrack(List<Basket> baskets, List<Coupon> coupons, int basketIndex,
                                  List<BasketCoupon> currentCombination, Set<Integer> usedCoupons,
                                  List<List<BasketCoupon>> result) {
        if (basketIndex == baskets.size()) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        Basket currentBasket = baskets.get(basketIndex);

        currentCombination.add(new BasketCoupon(currentBasket, null));
        backtrack(baskets, coupons, basketIndex + 1, currentCombination, usedCoupons, result);
        currentCombination.remove(currentCombination.size() - 1);

        for (int i = 0; i < coupons.size(); i++) {
            if (usedCoupons.contains(i)) continue;

            usedCoupons.add(i);
            currentCombination.add(new BasketCoupon(currentBasket, coupons.get(i)));
            backtrack(baskets, coupons, basketIndex + 1, currentCombination, usedCoupons, result);
            currentCombination.remove(currentCombination.size() - 1);
            usedCoupons.remove(i);
        }
    }
}

package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class APIService {
    public FinalShoppingListDto calculateShoppingList(List<Product> products, List<Coupon> coupons) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Request received");

        List<Set<Product>> subset = sublistGenerator(products);

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Sublist created");

        CouponsProducts couponsProducts = new CouponsProducts();

        subset.forEach(list -> coupons.forEach(coupon -> couponsProducts.addEntryToMap(coupon, list)));

        System.out.println(subset.size());

        List<List<Set<Product>>> allCombinations = findAllCombinations(new HashSet<>(products), subset, coupons.size());

        System.out.println(allCombinations.size());
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " All combinations created");

        List<List<Basket>> allCombinationsBaskets = allCombinations.stream()
                .map(list -> list.stream()
                        .map(Basket::new)
                        .toList())
                .toList();

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " All combinations baskets created");

        List<List<BasketCoupon>> bcCombinations = matchCouponsToBaskets(coupons, allCombinationsBaskets);

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Matched coupons to baskets");

        bcCombinations.forEach(list -> list.forEach(BasketCoupon::calculateFinalSum));

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Calculated final sums");

        List<List<BasketCoupon>> bcSorted = bcCombinations.stream()
                .sorted(Comparator.comparing(list -> list.stream()
                        .mapToDouble(BasketCoupon::getFinalSum)
                        .sum()))
                .limit(5)
                .toList();

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Sorted and ready to return");

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
        return generateAllCombinations(subsets, fullList, maxSubsets,
                new ArrayList<>(), 0, new HashSet<>());
    }

    private List<List<Set<Product>>> generateAllCombinations(List<Set<Product>> subsets,
                                                             Set<Product> fullSet,
                                                             int maxSubsets,
                                                             List<Set<Product>> currentCombination,
                                                             int startIndex,
                                                             Set<Product> usedElements) {
        if (currentCombination.size() > maxSubsets) return Collections.emptyList();
        if (usedElements.equals(fullSet)) {
            return List.of(new ArrayList<>(currentCombination));
        }

        return subsets.subList(startIndex, subsets.size())
                .parallelStream()
                .flatMap(subset -> {
                    Set<Product> intersection = new HashSet<>(subset);
                    intersection.retainAll(usedElements);
                    if (!intersection.isEmpty()) {
                        return Stream.empty();
                    }

                    List<Set<Product>> newCombination = new ArrayList<>(currentCombination);
                    newCombination.add(subset);

                    Set<Product> newUsed = new HashSet<>(usedElements);
                    newUsed.addAll(subset);

                    return generateAllCombinations(subsets, fullSet, maxSubsets,
                            newCombination, startIndex + 1, newUsed)
                            .stream();
                })
                .toList();
    }

    private static List<List<BasketCoupon>> matchCouponsToBaskets(List<Coupon> coupons,
                                                                  List<List<Basket>> allCombinationsBaskets) {
        return allCombinationsBaskets
                .parallelStream()
                .flatMap(basketCombination ->
                        generateCombinations(basketCombination, coupons).stream())
                .toList();
    }

    private static List<List<BasketCoupon>> generateCombinations(List<Basket> baskets,
                                                                 List<Coupon> coupons) {
        return backtrack(baskets, coupons, 0, new ArrayList<>(), new HashSet<>());
    }

    private static List<List<BasketCoupon>> backtrack(List<Basket> baskets,
                                                      List<Coupon> coupons,
                                                      int basketIndex,
                                                      List<BasketCoupon> currentCombination,
                                                      Set<Integer> usedCoupons) {
        if (basketIndex == baskets.size()) {
            return List.of(new ArrayList<>(currentCombination));
        }

        Basket currentBasket = baskets.get(basketIndex);

        Stream<List<BasketCoupon>> withoutCouponStream = Stream.of(currentBasket)
                .map(b -> {
                    List<BasketCoupon> newCombination = new ArrayList<>(currentCombination);
                    newCombination.add(new BasketCoupon(b, null));
                    return backtrack(baskets, coupons, basketIndex + 1, newCombination, new HashSet<>(usedCoupons));
                })
                .flatMap(List::stream);

        Stream<List<BasketCoupon>> withCouponStream =
                IntStream.range(0, coupons.size())
                        .parallel()
                        .filter(i -> !usedCoupons.contains(i))
                        .mapToObj(i -> {
                            List<BasketCoupon> newCombination = new ArrayList<>(currentCombination);
                            newCombination.add(new BasketCoupon(currentBasket, coupons.get(i)));
                            Set<Integer> newUsed = new HashSet<>(usedCoupons);
                            newUsed.add(i);
                            return backtrack(baskets, coupons, basketIndex + 1, newCombination, newUsed);
                        })
                        .flatMap(List::stream);

        return Stream.concat(withoutCouponStream, withCouponStream).toList();
    }
}

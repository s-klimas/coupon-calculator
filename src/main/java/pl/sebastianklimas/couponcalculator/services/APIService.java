package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class APIService {
    public FinalShoppingListDto calculateShoppingList(List<Product> products, List<Coupon> coupons) {
        AllSubsetsList allSubsetsList = generateListOfAllSubsets(products);

        List<FullShoppingList> allCombinations = findAllCombinations(new HashSet<>(products), allSubsetsList, coupons.size());

        List<BasketList> allCombinationsBaskets = allCombinations.stream()
                .map(list -> list.getProductSets().stream()
                        .map(Basket::new)
                        .toList())
                .map(BasketList::new)
                .toList();

        List<FullShoppingListWithCoupon> bcCombinations = matchCouponsToBaskets(coupons, allCombinationsBaskets);

        bcCombinations.forEach(list -> {
            list.getBasketCoupons().forEach(BasketCoupon::calculateFinalSum);
            list.calculateTotalPrice();
        });

        PriorityQueue<FullShoppingListWithCoupon> top5 = new PriorityQueue<>(
                5,
                Comparator.comparingDouble((FullShoppingListWithCoupon f) -> f.getTotalPrice().doubleValue()).reversed()
        );

        for (FullShoppingListWithCoupon combination : bcCombinations) {
            if (top5.size() < 5) {
                top5.offer(combination);
            } else {
                BigDecimal currentSum = combination.getTotalPrice();
                FullShoppingListWithCoupon worst = Objects.requireNonNull(top5.peek());
                BigDecimal worstTop = worst.getTotalPrice();

                if (currentSum.compareTo(worstTop) < 0) {
                    top5.poll();
                    top5.offer(combination);
                }
            }
        }

        List<FullShoppingListWithCoupon> bcSorted = new ArrayList<>(top5);
        bcSorted.sort(Comparator.comparing(list ->
                list.getTotalPrice().doubleValue()));

        return new FinalShoppingListDto(bcSorted);
    }

    private AllSubsetsList generateListOfAllSubsets(List<Product> products) {
        AllSubsetsList result = new AllSubsetsList();
        int totalSubsets = 1 << products.size(); // 2^n
        for (int i = 0; i < totalSubsets; i++) {
            ProductSet productSet = new ProductSet();
            for (int j = 0; j < products.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    productSet.addProduct(products.get(j));
                }
            }
            if (!productSet.getProducts().isEmpty()) {
                result.addProductSet(productSet);
            }
        }
        return result;
    }

    private List<FullShoppingList> findAllCombinations(Set<Product> fullList,
                                                       AllSubsetsList allSubsetsList,
                                                       int maxSubsets) {
        return generateAllCombinations(allSubsetsList, fullList, maxSubsets,
                new AllSubsetsList(), 0, new HashSet<>());
    }

    private List<FullShoppingList> generateAllCombinations(AllSubsetsList allSubsetsList,
                                                           Set<Product> fullSet,
                                                           int maxSubsets,
                                                           AllSubsetsList currentCombination,
                                                           int startIndex,
                                                           Set<Product> usedElements) {
        if (currentCombination.getProductSets().size() > maxSubsets) return Collections.emptyList();
        if (usedElements.equals(fullSet)) {
            return List.of(new FullShoppingList(currentCombination.getProductSets()));
        }

        List<ProductSet> subsets = allSubsetsList.getProductSets();

        Stream<ProductSet> stream = IntStream.range(startIndex, subsets.size())
                .mapToObj(subsets::get);

        return stream
                .parallel()
                .filter(subset -> {
                    for (Product p : subset.getProducts()) {
                        if (usedElements.contains(p)) return false;
                    }
                    return true;
                })
                .flatMap(subset -> {
                    List<ProductSet> newCombination = new ArrayList<>(currentCombination.getProductSets());
                    newCombination.add(subset);

                    Set<Product> newUsed = new HashSet<>(usedElements);
                    newUsed.addAll(subset.getProducts());

                    return generateAllCombinations(
                            allSubsetsList, fullSet, maxSubsets,
                            new AllSubsetsList(newCombination),
                            startIndex + 1,
                            newUsed
                    ).stream();
                })
                .toList();
    }

    private static List<FullShoppingListWithCoupon> matchCouponsToBaskets(List<Coupon> coupons,
                                                                          List<BasketList> allCombinationsBaskets) {
        return allCombinationsBaskets
                .parallelStream()
                .flatMap(basketCombination ->
                        generateCombinations(basketCombination.getBaskets(), coupons).stream())
                .map(FullShoppingListWithCoupon::new)
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

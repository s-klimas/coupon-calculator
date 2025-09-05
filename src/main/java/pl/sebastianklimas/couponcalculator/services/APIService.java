package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class APIService {
    public List<PotentialOrder> splitLists(List<Product> products, List<Coupon> coupons) {
        final int MAX_SIZE_PRODUCT_BUNCHES = 8;
        final int MAX_SIZE_COUPON_BUNCHES = 4;

        if (products.isEmpty() || coupons.isEmpty()) return new ArrayList<>();

        List<Product> sortedProducts = products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).toList();
        List<Coupon> sortedCoupons = new ArrayList<>();
        coupons.stream().sorted(Comparator.comparing(Coupon::getPercentDiscount).reversed()).forEach(sortedCoupons::add);

        List<PotentialOrder> potentialOrders = new ArrayList<>();

        for (int i = 0; i <= (sortedProducts.size() - 1) / MAX_SIZE_PRODUCT_BUNCHES; i++) {
            int fromProducts = Math.min(i * MAX_SIZE_PRODUCT_BUNCHES, sortedProducts.size() - 1);
            int toProducts = Math.min(fromProducts + MAX_SIZE_PRODUCT_BUNCHES, sortedProducts.size());
            List<Product> smallProductList = sortedProducts.subList(fromProducts, toProducts);
            List<Coupon> smallCouponList = new ArrayList<>();

            BigDecimal fullPrice = smallProductList.stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            sortedCoupons.forEach(coupon -> {
                if (smallCouponList.size() < Math.min(MAX_SIZE_COUPON_BUNCHES, sortedCoupons.size())
                        &&
                        coupon.getMinPrice().compareTo(fullPrice) <= 0) {
                    smallCouponList.add(coupon);
                }
            });

            PotentialOrder potentialOrder = calculateShoppingList(smallProductList, smallCouponList);

            potentialOrders.add(potentialOrder);

            List<Coupon> allUsedCoupons = potentialOrder.findAllUsedCoupons();
            sortedCoupons.removeAll(allUsedCoupons);
        }

        return potentialOrders;
    }

    private PotentialOrder calculateShoppingList(List<Product> products, List<Coupon> coupons) {
        List<Subset> allSubsetsList = generateListOfAllSubsets(products);

        List<Combination> allCombinations = findAllCombinations(new HashSet<>(products), allSubsetsList, coupons.size());

        List<PotentialOrder> potentialOrders = generateAllPotentialOrders(coupons, allCombinations);

        PriorityQueue<PotentialOrder> top5 = new PriorityQueue<>(
                1,
                Comparator.comparingDouble((PotentialOrder f) -> f.getTotalPrice().doubleValue()).reversed()
        );

        for (PotentialOrder potentialOrder : potentialOrders) {
            if (top5.isEmpty()) {
                top5.offer(potentialOrder);
            } else {
                BigDecimal currentSum = potentialOrder.getTotalPrice();
                PotentialOrder worst = Objects.requireNonNull(top5.peek());
                BigDecimal worstTop = worst.getTotalPrice();

                if (currentSum.compareTo(worstTop) < 0) {
                    top5.poll();
                    top5.offer(potentialOrder);
                }
            }
        }

        List<PotentialOrder> orders = new ArrayList<>(top5);
        orders.sort(Comparator.comparing(list -> list.getTotalPrice().doubleValue()));

        if (orders.isEmpty()) {
            Set<Product> setOfProducts = new HashSet<>(products);
            Subset subset = new Subset(setOfProducts);
            PotentialCart potentialCart = new PotentialCart(subset);
            return new PotentialOrder(List.of(potentialCart));
        }

        return orders.get(0);
    }

    private List<Subset> generateListOfAllSubsets(List<Product> products) {
        List<Subset> allCombinations = new ArrayList<>();
        int totalSubsets = 1 << products.size(); // 2^n
        for (int i = 0; i < totalSubsets; i++) {
            Set<Product> productSet = new HashSet<>();
            for (int j = 0; j < products.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    productSet.add(products.get(j));
                }
            }
            if (!productSet.isEmpty()) {
                allCombinations.add(new Subset(productSet));
            }
        }
        return allCombinations;
    }

    private List<Combination> findAllCombinations(Set<Product> fullList,
                                                  List<Subset> allSubsetsList,
                                                  int maxSubsets) {
        return generateAllCombinations(allSubsetsList, fullList, maxSubsets,
                new ArrayList<>(), 0, new HashSet<>());
    }

    private List<Combination> generateAllCombinations(List<Subset> subsets,
                                                      Set<Product> fullSet,
                                                      int maxSubsets,
                                                      List<Subset> currentCombination,
                                                      int startIndex,
                                                      Set<Product> usedElements) {
        if (currentCombination.size() > maxSubsets) return Collections.emptyList();
        if (usedElements.equals(fullSet)) {
            return List.of(new Combination(currentCombination));
        }

        Stream<Subset> stream = IntStream.range(startIndex, subsets.size())
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
                    List<Subset> newCombination = new ArrayList<>(currentCombination);
                    newCombination.add(subset);

                    Set<Product> newUsed = new HashSet<>(usedElements);
                    newUsed.addAll(subset.getProducts());

                    return generateAllCombinations(
                            subsets, fullSet, maxSubsets,
                            newCombination,
                            startIndex + 1,
                            newUsed
                    ).stream();
                })
                .toList();
    }

    private static List<PotentialOrder> generateAllPotentialOrders(List<Coupon> coupons,
                                                                   List<Combination> combinations) {
        return combinations
                .parallelStream()
                .flatMap(combination ->
                        generateCombinations(combination.getSubsets(), coupons).stream())
                .map(PotentialOrder::new)
                .toList();
    }

    private static List<List<PotentialCart>> generateCombinations(List<Subset> baskets,
                                                                 List<Coupon> coupons) {
        return backtrack(baskets, coupons, 0, new ArrayList<>(), new HashSet<>());
    }

    private static List<List<PotentialCart>> backtrack(List<Subset> baskets,
                                                      List<Coupon> coupons,
                                                      int basketIndex,
                                                      List<PotentialCart> currentCombination,
                                                      Set<Integer> usedCoupons) {
        if (basketIndex == baskets.size()) {
            return List.of(new ArrayList<>(currentCombination));
        }

        Subset currentBasket = baskets.get(basketIndex);

        Stream<List<PotentialCart>> withoutCouponStream = Stream.of(currentBasket)
                .map(b -> {
                    List<PotentialCart> newCombination = new ArrayList<>(currentCombination);
                    newCombination.add(new PotentialCart(b));
                    return backtrack(baskets, coupons, basketIndex + 1, newCombination, new HashSet<>(usedCoupons));
                })
                .flatMap(List::stream);

        Stream<List<PotentialCart>> withCouponStream =
                IntStream.range(0, coupons.size())
                        .parallel()
                        .filter(i -> !usedCoupons.contains(i))
                        .mapToObj(i -> {
                            List<PotentialCart> newCombination = new ArrayList<>(currentCombination);
                            newCombination.add(new PotentialCart(currentBasket, coupons.get(i)));
                            Set<Integer> newUsed = new HashSet<>(usedCoupons);
                            newUsed.add(i);
                            return backtrack(baskets, coupons, basketIndex + 1, newCombination, newUsed);
                        })
                        .flatMap(List::stream);

        return Stream.concat(withoutCouponStream, withCouponStream).toList();
    }
}

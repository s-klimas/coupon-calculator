package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class APIService {
    public List<FullShoppingListWithCoupon> splitLists(List<Product> products, List<Coupon> coupons) {
        /* --- You can increase them to 8 and 4 but calculating will take longer. I don't recommend to increase it further. --- */
//        final int MAX_SIZE_PRODUCT_BUNCHES = 7;
//        final int MAX_SIZE_COUPON_BUNCHES = 3;
    private final int MAX_SIZE_PRODUCT_BUNCHES = 9;
    private final int MAX_SIZE_COUPON_BUNCHES = 5;
    private final CombinationGenerator combinationGenerator;
    private final CouponOptimizer couponOptimizer;

    public APIService(CombinationGenerator combinationGenerator, CouponOptimizer couponOptimizer) {
        this.combinationGenerator = combinationGenerator;
        this.couponOptimizer = couponOptimizer;
    }

    public List<PotentialOrder> splitLists(List<Product> products, List<Coupon> coupons) {

        if (products.isEmpty() || coupons.isEmpty()) return new ArrayList<>();

        sortProducts(products);
        sortCoupons(coupons);

        List<PotentialOrder> potentialOrders = new ArrayList<>();

        for (int i = 0; i <= (products.size() - 1) / MAX_SIZE_PRODUCT_BUNCHES; i++) {
            List<Product> smallProductList = createSmallProductList(products, i);
            BigDecimal fullPrice = calculateFullPrice(smallProductList);
            List<Coupon> smallCouponList = createSmallCouponList(coupons, fullPrice);

            PotentialOrder potentialOrder = calculateShoppingList(smallProductList, smallCouponList);

            potentialOrders.add(potentialOrder);

            List<Coupon> allUsedCoupons = potentialOrder.findAllUsedCoupons();
            coupons.removeAll(allUsedCoupons);
        }

        return potentialOrders;
    }

    private void sortProducts(List<Product> products) {
        products.sort(Comparator.comparing(Product::getPrice).reversed());
    }

    private void sortCoupons(List<Coupon> coupons) {
        coupons.sort(Comparator.comparing(Coupon::getPercentDiscount).reversed());
    }

    private List<Product> createSmallProductList(List<Product> products, int i) {
        int fromProducts = i * MAX_SIZE_PRODUCT_BUNCHES;
        int toProducts = Math.min(fromProducts + MAX_SIZE_PRODUCT_BUNCHES, products.size());

        return products.subList(fromProducts, toProducts);
    }

    private List<Coupon> createSmallCouponList(List<Coupon> coupons, BigDecimal fullPrice) {
        return coupons.stream()
                .filter(coupon -> coupon.getMinPrice().compareTo(fullPrice) <= 0)
                .limit(Math.min(MAX_SIZE_COUPON_BUNCHES, coupons.size()))
                .collect(
                        Collectors.toCollection(
                                ArrayList::new
                        )
                );
    }

    private BigDecimal calculateFullPrice(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PotentialOrder calculateShoppingList(List<Product> products, List<Coupon> coupons) {
        List<Subset> allSubsetsList = combinationGenerator.generateListOfAllSubsets(products);

        List<Combination> allCombinations = combinationGenerator.findAllCombinations(new HashSet<>(products), allSubsetsList, coupons.size());

        List<PotentialOrder> potentialOrders = couponOptimizer.generateAllPotentialOrders(coupons, allCombinations);

        PriorityQueue<PotentialOrder> top1 = new PriorityQueue<>(
                1,
                Comparator.comparingDouble((PotentialOrder f) -> f.getTotalPrice().doubleValue()).reversed()
        );

        for (PotentialOrder potentialOrder : potentialOrders) {
            if (top1.isEmpty()) {
                top1.offer(potentialOrder);
            } else {
                BigDecimal currentSum = potentialOrder.getTotalPrice();
                PotentialOrder worst = Objects.requireNonNull(top1.peek());
                BigDecimal worstTop = worst.getTotalPrice();

                if (currentSum.compareTo(worstTop) < 0) {
                    top1.poll();
                    top1.offer(potentialOrder);
                }
            }
        }

        List<PotentialOrder> orders = new ArrayList<>(top1);
        orders.sort(Comparator.comparing(list -> list.getTotalPrice().doubleValue()));

        if (orders.isEmpty()) {
            Set<Product> setOfProducts = new HashSet<>(products);
            Subset subset = new Subset(setOfProducts);
            PotentialCart potentialCart = new PotentialCart(subset);
            return new PotentialOrder(List.of(potentialCart));
        }

        return orders.get(0);
    }
}

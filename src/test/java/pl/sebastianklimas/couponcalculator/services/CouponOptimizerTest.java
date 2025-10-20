package pl.sebastianklimas.couponcalculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CouponOptimizerTest {

    private CouponOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new CouponOptimizer();
    }

    private Product product(String name, double price) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(BigDecimal.valueOf(price));
        return p;
    }

    private Coupon coupon(String code, int percentDiscount, double minPrice) {
        Coupon c = new Coupon();
        c.setCode(code);
        c.setPercentDiscount(percentDiscount);
        c.setMinPrice(BigDecimal.valueOf(minPrice));
        c.setMaxDiscount(BigDecimal.valueOf((double) 1000));
        return c;
    }

    @Test
    void shouldReturnEmptyList_whenNoCombinationsProvided() {
        List<PotentialOrder> result = optimizer.generateAllPotentialOrders(List.of(), List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowException_whenArgumentsAreNull() {
        assertThatThrownBy(() -> optimizer.generateAllPotentialOrders(null, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> optimizer.generateAllPotentialOrders(List.of(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldGenerateOneOrder_whenSingleSubsetWithoutCoupons() {
        Subset subset = new Subset(
                Set.of(product("P1", 10), product("P2", 20))
        );
        Combination combination = new Combination(List.of(subset));

        List<PotentialOrder> result = optimizer.generateAllPotentialOrders(List.of(), List.of(combination));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getPotentialCarts()).hasSize(1);
        assertThat(result.getFirst().getPotentialCarts().getFirst().getCoupon()).isNull();
    }

    @Test
    void shouldGenerateTwoOrders_whenOneCouponAvailable() {
        Subset subset = new Subset(
                Set.of(product("P1", 10), product("P2", 20))
        );
        Combination combination = new Combination(List.of(subset));
        Coupon coupon = coupon("C1", 50, 0);

        List<PotentialOrder> result = optimizer.generateAllPotentialOrders(List.of(coupon), List.of(combination));

        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(order ->
                assertThat(order.getPotentialCarts().getFirst().getCoupon()).isNull());
        assertThat(result).anySatisfy(order ->
                assertThat(order.getPotentialCarts().getFirst().getCoupon()).isEqualTo(coupon));
    }

    @Test
    void shouldGenerateAllValidCombinations_whenMultipleSubsetsAndCoupons() {
        Subset s1 = new Subset(
                Set.of(product("P1", 10), product("P2", 20))
        );
        Subset s2 = new Subset(
                Set.of(product("P3", 30), product("P4", 40))
        );
        Coupon c1 = coupon("C1", 50, 0);
        Coupon c2 = coupon("C2", 75, 50);
        Combination combination = new Combination(List.of(s1, s2));

        List<PotentialOrder> result = optimizer.generateAllPotentialOrders(List.of(c1, c2), List.of(combination));

        // 1st subset: 3 options (no coupon, c1, c2)
        // 2nd subset: for each previous, again 3 options but coupons can't repeat
        // total = 9 - duplicates (where same coupon reused) = 7
        assertThat(result).hasSize(7);
    }
}
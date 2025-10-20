package pl.sebastianklimas.couponcalculator.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class APIServiceTest {

    @Mock
    private CombinationGenerator combinationGenerator;

    @Mock
    private CouponOptimizer couponOptimizer;

    @InjectMocks
    private APIService apiService;

    private Product productA, productB, productC;
    private Coupon coupon10, coupon20;
    private PotentialOrder mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productA = product("A", 100);
        productB = product("B", 50);
        productC = product("C", 25);

        coupon10 = coupon("C1", 10, 0);
        coupon20 = coupon("C2", 20, 50);

        mockOrder = mock(PotentialOrder.class);
        when(mockOrder.findAllUsedCoupons()).thenReturn(List.of(coupon10));
        when(mockOrder.getTotalPrice()).thenReturn(BigDecimal.valueOf(120));
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

// ---- splitLists() Tests ----

    @Test
    @DisplayName("splitLists should return empty list when products or coupons are empty")
    void splitLists_ShouldReturnEmptyList_WhenEmptyInput() {
        List<PotentialOrder> result1 = apiService.splitLists(Collections.emptyList(), List.of(coupon10));
        List<PotentialOrder> result2 = apiService.splitLists(List.of(productA), Collections.emptyList());

        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        verifyNoInteractions(combinationGenerator, couponOptimizer);
    }

    @Test
    @DisplayName("splitLists should sort products and coupons correctly and call internal methods")
    void splitLists_ShouldSortListsAndInvokeDependencies_WhenValidInput() {
        // given
        List<Product> products = new ArrayList<>(List.of(productC, productB, productA));
        List<Coupon> coupons = new ArrayList<>(List.of(coupon10, coupon20));

        // mocks
        List<Subset> subsets = List.of(new Subset(Set.of(productA)));
        List<Combination> combinations = List.of(new Combination(List.of(new Subset(Set.of(productA)))));
        List<PotentialOrder> potentialOrders = List.of(mockOrder);

        when(combinationGenerator.generateListOfAllSubsets(anyList())).thenReturn(subsets);
        when(combinationGenerator.findAllCombinations(anySet(), anyList(), anyInt())).thenReturn(combinations);
        when(couponOptimizer.generateAllPotentialOrders(anyList(), anyList())).thenReturn(potentialOrders);

        // when
        List<PotentialOrder> results = apiService.splitLists(products, coupons);

        // then
        assertThat(results).hasSize(1);
        assertThat(products).isSortedAccordingTo(Comparator.comparing(Product::getPrice).reversed());
        assertThat(coupons).isSortedAccordingTo(Comparator.comparing(Coupon::getPercentDiscount).reversed());
        verify(combinationGenerator).generateListOfAllSubsets(anyList());
        verify(couponOptimizer).generateAllPotentialOrders(anyList(), anyList());
    }

    @Test
    @DisplayName("splitLists should remove used coupons from subsequent iterations")
    void splitLists_ShouldRemoveUsedCoupons_WhenPotentialOrderUsesThem() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(productA);
        products.add(productB);
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon10);
        coupons.add(coupon20);

        when(combinationGenerator.generateListOfAllSubsets(anyList())).thenReturn(List.of(new Subset(Set.of(productA))));
        when(combinationGenerator.findAllCombinations(anySet(), anyList(), anyInt()))
                .thenReturn(List.of(new Combination(List.of(new Subset(Set.of(productA))))));
        when(couponOptimizer.generateAllPotentialOrders(anyList(), anyList()))
                .thenReturn(List.of(mockOrder));

        // when
        List<PotentialOrder> result = apiService.splitLists(products, coupons);

        // then
        assertThat(result).hasSize(1);
        assertThat(coupons).doesNotContain(coupon10);
    }

    @Test
    @DisplayName("splitLists should create new PotentialOrder if none found in calculateShoppingList()")
    void splitLists_ShouldCreateDefaultPotentialOrder_WhenNoOrdersFound() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(productA);
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon10);

        when(combinationGenerator.generateListOfAllSubsets(anyList())).thenReturn(List.of(new Subset(Set.of(productA))));
        when(combinationGenerator.findAllCombinations(anySet(), anyList(), anyInt())).thenReturn(Collections.emptyList());
        when(couponOptimizer.generateAllPotentialOrders(anyList(), anyList())).thenReturn(Collections.emptyList());

        // when
        List<PotentialOrder> result = apiService.splitLists(products, coupons);

        // then
        assertThat(result).hasSize(1);
        PotentialOrder order = result.getFirst();
        assertThat(order.getPotentialCarts().getFirst().getSubset().getProducts())
                .containsExactlyInAnyOrder(productA);
    }
}
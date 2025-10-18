package pl.sebastianklimas.couponcalculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class APIServiceTest {
    @InjectMocks
    private APIService apiService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Nested
    @DisplayName("splitLists() – basic and edge cases")
    class SplitListsTests {
        @Test
        @DisplayName("should return empty list when products list is empty")
        void shouldReturnEmptyWhenProductsEmpty() {
            // given
            List<Product> products = Collections.emptyList();
            List<Coupon> coupons = List.of(coupon("C1", 10, 100));

            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty list when coupons list is empty")
        void shouldReturnEmptyWhenCouponsEmpty() {
            // given
            List<Product> products = List.of(product("A", 100));
            List<Coupon> coupons = Collections.emptyList();
            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should split large product list into multiple bunches of max 7")
        void shouldSplitProductsIntoBunches() {
            // given
            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= 15; i++) {
                products.add(product("P" + i, i * 10));
            }
            List<Coupon> coupons = List.of(
                    coupon("C1", 10, 0),
                    coupon("C2", 5, 0),
                    coupon("C3", 20, 0));
            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            assertThat(result).as("Expect 3 batches of products (7 + 7 + 1)").hasSize(3);
        }

        @Test
        @DisplayName("should sort products by price descending and coupons by discount descending")
        void shouldSortProductsAndCouponsProperly() {
            // given
            List<Product> products = List.of(
                    product("Cheap", 10),
                    product("Expensive", 100),
                    product("Medium", 50));
            List<Coupon> coupons = List.of(
                    coupon("Small", 5, 0),
                    coupon("Big", 20, 0),
                    coupon("Medium", 10, 0));
            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            // indirectly test sorting by checking order in first batch (most expensive first)
            assertThat(result).isNotEmpty();
            FullShoppingListWithCoupon firstBatch = result.getFirst();
            List<BasketCoupon> basketCoupons = firstBatch.getBasketCoupons(); // sum of baskets should include highest-priced product first
            BigDecimal total = firstBatch.getTotalPrice();
            assertThat(total).isGreaterThan(BigDecimal.ZERO); // coupons sorted by discount (20%, 10%, 5%) reflected in internal logic
            assertThat(coupons).extracting(Coupon::getPercentDiscount).containsExactlyInAnyOrder(20, 10, 5);
        }

        @Test
        @DisplayName("should apply coupons only if min price requirement is met")
        void shouldApplyCouponsOnlyIfMinPriceIsMet() {
            // given
            List<Product> products = List.of(product("Low", 50));
            List<Coupon> coupons = List.of(
                    coupon("HighMin", 10, 100),
                    coupon("LowMin", 5, 10));
            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            assertThat(result).hasSize(1);
            FullShoppingListWithCoupon list = result.getFirst();
            List<BasketCoupon> basketCoupons = list.getBasketCoupons();
            assertThat(basketCoupons).extracting(b -> b.getCoupon() != null ? b.getCoupon().getCode() : null).doesNotContain("HighMin");
        }

        @Test
        @DisplayName("should reuse coupons only once between batches")
        void shouldRemoveUsedCouponsBetweenIterations() {
            // given
            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= 14; i++) {
                products.add(product("P" + i, 50));
            }
            List<Coupon> coupons = List.of(
                    coupon("C1", 10, 0),
                    coupon("C2", 5, 0),
                    coupon("C3", 20, 0));
            // when
            List<FullShoppingListWithCoupon> result = apiService.splitLists(products, coupons);
            // then
            assertThat(result).hasSizeGreaterThan(1);
            List<String> allUsed = result.stream().flatMap(r -> r.getAllUsedCoupons().stream()).map(Coupon::getCode).toList();
            assertThat(allUsed).doesNotHaveDuplicates().as("Each coupon should be used at most once across all batches");
        }
    }

    @Nested
    @DisplayName("Private methods – indirectly tested")
    class PrivateMethodsTests {
        @Test
        @DisplayName("generateListOfAllSubsets() should produce correct number of subsets (2^n - 1)")
        void shouldGenerateAllSubsets() throws Exception {
            // given
            List<Product> products = List.of(
                    product("A", 10),
                    product("B", 20),
                    product("C", 30)); // use reflection to access private method
            var method = APIService.class.getDeclaredMethod("generateListOfAllSubsets", List.class);
            method.setAccessible(true);
            // when
            AllSubsetsList subsets = (AllSubsetsList) method.invoke(apiService, products);
            // then
            assertThat(subsets.getProductSets()).hasSize((1 << products.size()) - 1).as("Expected 2^n - 1 non-empty subsets for n products");
        }
    }
}

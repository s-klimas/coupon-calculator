package pl.sebastianklimas.couponcalculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sebastianklimas.couponcalculator.models.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APIServiceTest {
    private APIService apiService;

    @BeforeEach
    void setUp() {
        apiService = new APIService();
    }

    @Test
    void testCalculateShoppingList() {
        // given
        Product product1 = new Product(BigDecimal.valueOf(10), "Mleko");
        Product product2 = new Product(BigDecimal.valueOf(10), "Mleko");
        Product product3 = new Product(BigDecimal.valueOf(20), "Lego");
        Coupon coupon1 = new Coupon(BigDecimal.valueOf(40), BigDecimal.valueOf(100), 50, "K1");
        Coupon coupon2 = new Coupon(BigDecimal.valueOf(50), BigDecimal.valueOf(100), 50, "K2");
        Coupon coupon3 = new Coupon(BigDecimal.valueOf(15), BigDecimal.valueOf(10), 70, "K3");

        List<Product> products = List.of(product1, product2, product3);
        List<Coupon> coupons = List.of(coupon1, coupon2, coupon3);

        // when
        FinalShoppingListDto result = apiService.calculateShoppingList(products, coupons);

        // then
        assertNotNull(result);
        assertFalse(result.getShoppingListsWithCoupons().isEmpty());
        List<FullShoppingListWithCoupon> basketCoupons = result.getShoppingListsWithCoupons();
        for (int i = 0; i < basketCoupons.size() - 1; i++) {
            double sumCurrent = basketCoupons.get(i).getBasketCoupons().stream().mapToDouble(bc -> bc.getFinalSum().doubleValue()).sum();
            double sumNext = basketCoupons.get(i + 1).getBasketCoupons().stream().mapToDouble(bc -> bc.getFinalSum().doubleValue()).sum();
            assertTrue(sumCurrent <= sumNext, "Lists are not sorted correctly");
        }
    }
}
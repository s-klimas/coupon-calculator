package pl.sebastianklimas.couponcalculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sebastianklimas.couponcalculator.models.BasketCoupon;
import pl.sebastianklimas.couponcalculator.models.Coupon;
import pl.sebastianklimas.couponcalculator.models.FinalShoppingListDto;
import pl.sebastianklimas.couponcalculator.models.Product;

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
        Product product1 = new Product(10, "Mleko");
        Product product2 = new Product(10, "Mleko");
        Product product3 = new Product(20, "Lego");
        Coupon coupon1 = new Coupon(40, 100, 50, "K1");
        Coupon coupon2 = new Coupon(50, 100, 50, "K2");
        Coupon coupon3 = new Coupon(15, 10, 70, "K3");

        List<Product> products = List.of(product1, product2, product3);
        List<Coupon> coupons = List.of(coupon1, coupon2, coupon3);

        // when
        FinalShoppingListDto result = apiService.calculateShoppingList(products, coupons);

        // then
        assertNotNull(result);
        assertFalse(result.getShoppingList().isEmpty());
        List<List<BasketCoupon>> basketCoupons = result.getShoppingList();
        for (int i = 0; i < basketCoupons.size() - 1; i++) {
            double sumCurrent = basketCoupons.get(i).stream().mapToDouble(BasketCoupon::getFinalSum).sum();
            double sumNext = basketCoupons.get(i + 1).stream().mapToDouble(BasketCoupon::getFinalSum).sum();
            assertTrue(sumCurrent <= sumNext, "Lists are not sorted correctly");
        }
    }
}
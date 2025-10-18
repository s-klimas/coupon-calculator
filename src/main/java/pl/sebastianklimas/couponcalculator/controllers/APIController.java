package pl.sebastianklimas.couponcalculator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sebastianklimas.couponcalculator.exceptions.TooManyCouponsException;
import pl.sebastianklimas.couponcalculator.exceptions.TooManyProductsException;
import pl.sebastianklimas.couponcalculator.models.ApiRequest;
import pl.sebastianklimas.couponcalculator.models.FullShoppingListWithCoupon;
import pl.sebastianklimas.couponcalculator.services.APIService;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Calculate shopping list")
public class APIController {
    private final APIService apiService;

    @CrossOrigin(origins = "*")
    @PostMapping("/calculate-shopping-list")
    @Operation(summary = "Calculates which products to buy using which coupon.")
    public ResponseEntity<List<FullShoppingListWithCoupon>> getListOfProductsAndCoupons(
            @RequestBody ApiRequest inputLists
    ) {
        return ResponseEntity.ok(apiService.splitLists(inputLists.getProducts(), inputLists.getCoupons()));
    }

    @ExceptionHandler(TooManyProductsException.class)
    public ResponseEntity<String> handleTooManyProductsException(TooManyProductsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(TooManyCouponsException.class)
    public ResponseEntity<String> handleTooManyCouponsException(TooManyCouponsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

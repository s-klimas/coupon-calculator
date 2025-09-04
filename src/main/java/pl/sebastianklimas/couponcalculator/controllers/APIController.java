package pl.sebastianklimas.couponcalculator.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sebastianklimas.couponcalculator.exceptions.TooManyCouponsException;
import pl.sebastianklimas.couponcalculator.exceptions.TooManyProductsException;
import pl.sebastianklimas.couponcalculator.models.ApiRequest;
import pl.sebastianklimas.couponcalculator.models.FinalShoppingListDto;
import pl.sebastianklimas.couponcalculator.services.APIService;

@RestController
public class APIController {

    private final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/calculate-shopping-list")
    public ResponseEntity<FinalShoppingListDto> getListOfProductsAndCoupons(@RequestBody ApiRequest inputLists) {
        if(inputLists.getProducts().size() > 8) throw new TooManyProductsException("Too many products, max 8 products are allowed");
        if(inputLists.getCoupons().size() > 4) throw new TooManyCouponsException("Too many coupons, max 4 coupons are allowed");

        return ResponseEntity.ok(apiService.calculateShoppingList(inputLists.getProducts(), inputLists.getCoupons()));
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

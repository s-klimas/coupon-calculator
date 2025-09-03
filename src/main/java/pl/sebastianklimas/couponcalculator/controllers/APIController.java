package pl.sebastianklimas.couponcalculator.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sebastianklimas.couponcalculator.models.FinalShoppingListDto;
import pl.sebastianklimas.couponcalculator.models.InputLists;
import pl.sebastianklimas.couponcalculator.services.APIService;

@RestController
public class APIController {

    private final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/calculate-shopping-list")
    public ResponseEntity<FinalShoppingListDto> getListOfProductsAndCoupons(@RequestBody InputLists inputLists) {
        return ResponseEntity.ok(apiService.calculateShoppingList(inputLists.getProducts(), inputLists.getCoupons()));
    }
}

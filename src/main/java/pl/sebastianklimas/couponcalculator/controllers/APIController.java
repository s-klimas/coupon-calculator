package pl.sebastianklimas.couponcalculator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sebastianklimas.couponcalculator.models.ApiRequest;
import pl.sebastianklimas.couponcalculator.models.PotentialOrder;
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
    public ResponseEntity<List<PotentialOrder>> getListOfProductsAndCoupons(
            @RequestBody ApiRequest inputLists
    ) {
        return ResponseEntity.ok(apiService.splitLists(inputLists.getProducts(), inputLists.getCoupons()));
    }
}

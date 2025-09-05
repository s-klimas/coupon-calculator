package pl.sebastianklimas.couponcalculator.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sebastianklimas.couponcalculator.models.ApiRequest;
import pl.sebastianklimas.couponcalculator.models.PotentialOrder;
import pl.sebastianklimas.couponcalculator.services.APIService;

import java.util.List;

@RestController
public class APIController {

    private final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/calculate-shopping-list")
    public ResponseEntity<List<PotentialOrder>> getListOfProductsAndCoupons(@RequestBody ApiRequest inputLists) {
        return ResponseEntity.ok(apiService.splitLists(inputLists.getProducts(), inputLists.getCoupons()));
    }
}

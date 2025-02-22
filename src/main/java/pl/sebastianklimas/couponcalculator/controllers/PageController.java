package pl.sebastianklimas.couponcalculator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sebastianklimas.couponcalculator.models.FinalShoppingListDto;
import pl.sebastianklimas.couponcalculator.models.ShoppingListDto;
import pl.sebastianklimas.couponcalculator.models.dtos.CouponDto;
import pl.sebastianklimas.couponcalculator.models.dtos.CouponDtoMapper;
import pl.sebastianklimas.couponcalculator.models.dtos.ProductDto;
import pl.sebastianklimas.couponcalculator.models.dtos.ProductDtoMapper;
import pl.sebastianklimas.couponcalculator.services.APIService;

import java.util.stream.Collectors;

@Controller
public class PageController {
    private final APIService apiService;

    public PageController(APIService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/")
    public String home(Model model) {
        ShoppingListDto shoppingList = new ShoppingListDto();
        shoppingList.getProducts().add(new ProductDto());
        shoppingList.getCoupons().add(new CouponDto());

        model.addAttribute("shoppingList", shoppingList);

        return "home";
    }

    @PostMapping("/r")
    public String cal(
            Model model,
            @ModelAttribute("shoppingList") ShoppingListDto shoppingList
    ) {
        FinalShoppingListDto fsld = apiService.calculateShoppingList(
                shoppingList.getProducts().stream().map(ProductDtoMapper::map).collect(Collectors.toList()),
                shoppingList.getCoupons().stream().map(CouponDtoMapper::map).collect(Collectors.toList()));
        model.addAttribute("finalShoppingListDto", fsld);
        return "result";
    }
}

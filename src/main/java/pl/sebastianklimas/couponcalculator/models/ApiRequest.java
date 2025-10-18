package pl.sebastianklimas.couponcalculator.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "The input object containing list of products and list of coupons")
public class ApiRequest {
    @Schema(name = "products",
            example = "[\n" +
                    "    {\n" +
                    "      \"price\": 10,\n" +
                    "      \"name\": \"P1\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"price\": 20,\n" +
                    "      \"name\": \"P2\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"price\": 30,\n" +
                    "      \"name\": \"P3\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"price\": 40,\n" +
                    "      \"name\": \"P4\"\n" +
                    "    }\n" +
                    "  ]",
            description = "List of products consisting of name and price")
    private List<Product> products;

    @Schema(name = "coupons",
            example = "[\n" +
                    "    {\n" +
                    "      \"minPrice\": 0,\n" +
                    "      \"maxDiscount\": 100,\n" +
                    "      \"percentDiscount\": 50,\n" +
                    "      \"code\": \"K1\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"minPrice\": 0,\n" +
                    "      \"maxDiscount\": 100,\n" +
                    "      \"percentDiscount\": 50,\n" +
                    "      \"code\": \"K2\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"minPrice\": 0,\n" +
                    "      \"maxDiscount\": 100,\n" +
                    "      \"percentDiscount\": 50,\n" +
                    "      \"code\": \"K3\"\n" +
                    "    }\n" +
                    "  ]",
            description = "List of coupons consisting of minimal price, maximum discount, percent od discount and code")
    private List<Coupon> coupons;
}

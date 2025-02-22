package pl.sebastianklimas.couponcalculator.models.dtos;

import pl.sebastianklimas.couponcalculator.models.Product;

public class ProductDtoMapper {
    public static ProductDto map(Product product) {
        return new ProductDto(product.getPrice(), product.getName());
    }

    public static Product map(ProductDto productDto) {
        return new Product(productDto.getPrice(), productDto.getName());
    }
}

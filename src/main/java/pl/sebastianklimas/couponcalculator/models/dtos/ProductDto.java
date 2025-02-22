package pl.sebastianklimas.couponcalculator.models.dtos;

public class ProductDto {
    private double price;
    private String name;

    public ProductDto() {
    }

    public ProductDto(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package pl.sebastianklimas.couponcalculator.models;


import java.util.ArrayList;
import java.util.List;


public class AllSubsetsList {
    private List<ProductSet> productSets = new ArrayList<>();

    public AllSubsetsList() {
    }

    public AllSubsetsList(List<ProductSet> productSets) {
        this.productSets = productSets;
    }

    public void addProductSet(ProductSet productSet) {
        productSets.add(productSet);
    }

    @Override
    public String toString() {
        return "AllSubsetsList {" + "productSets=" + productSets + '}';
    }

    public void print() {
        System.out.println("AllSubsetsList {");
        for (ProductSet productSet : productSets) {
            System.out.print("\t(");
            productSet.print();
            System.out.print(")");
            System.out.println();
        }
        System.out.println("}");
    }

    public List<ProductSet> getProductSets() {
        return productSets;
    }
}

package pl.sebastianklimas.couponcalculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.sebastianklimas.couponcalculator.models.Combination;
import pl.sebastianklimas.couponcalculator.models.Product;
import pl.sebastianklimas.couponcalculator.models.Subset;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CombinationGeneratorTest {

    private CombinationGenerator generator;
    private Product productA;
    private Product productB;
    private Product productC;

    @BeforeEach
    void setUp() {
        generator = new CombinationGenerator();
        productA = product("A");
        productB = product("B");
        productC = product("C");
    }

    private Product product(String name) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(BigDecimal.valueOf(1));
        return p;
    }

// ---- Tests for generateListOfAllSubsets ----

    @Test
    @DisplayName("generateListOfAllSubsets should return all non-empty subsets for given products")
    void generateListOfAllSubsets_ShouldReturnAllSubsets_WhenListHasMultipleElements() {
        List<Product> products = List.of(productA, productB, productC);

        List<Subset> subsets = generator.generateListOfAllSubsets(products);

        assertThat(subsets).hasSize(7);
        assertThat(subsets)
                .extracting(s -> s.getProducts().size())
                .containsExactlyInAnyOrder(1, 1, 1, 2, 2, 2, 3);
    }

    @Test
    @DisplayName("generateListOfAllSubsets should return empty list when input is empty")
    void generateListOfAllSubsets_ShouldReturnEmptyList_WhenInputIsEmpty() {
        List<Subset> subsets = generator.generateListOfAllSubsets(Collections.emptyList());
        assertThat(subsets).isEmpty();
    }

    @Test
    @DisplayName("generateListOfAllSubsets should contain only combinations from the input set")
    void generateListOfAllSubsets_ShouldContainOnlyInputElements_WhenCalled() {
        List<Product> products = List.of(productA, productB);
        List<Subset> subsets = generator.generateListOfAllSubsets(products);

        Set<Product> allProductsUsed = subsets.stream()
                .flatMap(s -> s.getProducts().stream())
                .collect(Collectors.toSet());

        assertThat(allProductsUsed).containsExactlyInAnyOrder(productA, productB);
    }

// ---- Tests for findAllCombinations ----

    @Test
    @DisplayName("findAllCombinations should return all valid combinations covering full set within maxSubsets")
    void findAllCombinations_ShouldReturnValidCombinations_WhenConditionsMet() {
        Set<Product> fullSet = Set.of(productA, productB, productC);
        List<Subset> subsets = List.of(
                new Subset(Set.of(productA)),
                new Subset(Set.of(productB)),
                new Subset(Set.of(productC)),
                new Subset(Set.of(productA, productB)),
                new Subset(Set.of(productB, productC))
        );

        List<Combination> results = generator.findAllCombinations(fullSet, subsets, 3);

        assertThat(results)
                .isNotEmpty()
                .allSatisfy(c -> {
                    Set<Product> combined = c.getSubsets().stream()
                            .flatMap(s -> s.getProducts().stream())
                            .collect(Collectors.toSet());
                    assertThat(combined).containsExactlyInAnyOrderElementsOf(fullSet);
                    assertThat(c.getSubsets()).hasSizeLessThanOrEqualTo(3);
                });
    }

    @Test
    @DisplayName("findAllCombinations should return empty list when no combination covers full set")
    void findAllCombinations_ShouldReturnEmptyList_WhenImpossible() {
        Set<Product> fullSet = Set.of(productA, productB);
        List<Subset> subsets = List.of(new Subset(Set.of(productA)));

        List<Combination> results = generator.findAllCombinations(fullSet, subsets, 2);

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("findAllCombinations should handle maxSubsets limit correctly")
    void findAllCombinations_ShouldRespectMaxSubsetsLimit_WhenExceeded() {
        Set<Product> fullSet = Set.of(productA, productB, productC);
        List<Subset> subsets = List.of(
                new Subset(Set.of(productA)),
                new Subset(Set.of(productB)),
                new Subset(Set.of(productC))
        );

        List<Combination> results = generator.findAllCombinations(fullSet, subsets, 2);

        // With maxSubsets=2, it’s impossible to cover 3 products
        assertThat(results).isEmpty();
    }
}
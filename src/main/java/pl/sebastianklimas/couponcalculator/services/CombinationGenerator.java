package pl.sebastianklimas.couponcalculator.services;

import org.springframework.stereotype.Service;
import pl.sebastianklimas.couponcalculator.models.Combination;
import pl.sebastianklimas.couponcalculator.models.Product;
import pl.sebastianklimas.couponcalculator.models.Subset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CombinationGenerator {
    public List<Subset> generateListOfAllSubsets(List<Product> products) {
        List<Subset> allCombinations = new ArrayList<>();
        int totalSubsets = 1 << products.size(); // 2^n
        for (int i = 0; i < totalSubsets; i++) {
            Set<Product> productSet = new HashSet<>();
            for (int j = 0; j < products.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    productSet.add(products.get(j));
                }
            }
            if (!productSet.isEmpty()) {
                allCombinations.add(new Subset(productSet));
            }
        }
        return allCombinations;
    }

    public List<Combination> findAllCombinations(Set<Product> fullList,
                                                 List<Subset> allSubsetsList,
                                                 int maxSubsets) {
        List<Combination> results = new ArrayList<>();
        generateAllCombinations(allSubsetsList, fullList, maxSubsets, new ArrayList<>(), 0, new HashSet<>(), results);
        return results;
    }

    private void generateAllCombinations(
            List<Subset> subsets,
            Set<Product> fullSet,
            int maxSubsets,
            List<Subset> currentCombination,
            int startIndex,
            Set<Product> usedElements,
            List<Combination> results) {
        if (currentCombination.size() > maxSubsets) return;
        if (usedElements.equals(fullSet)) {
            results.add(new Combination(new ArrayList<>(currentCombination)));
            return;
        }

        for (int i = startIndex; i < subsets.size(); i++) {
            Subset subset = subsets.get(i);

            if (subset.getProducts().stream().anyMatch(usedElements::contains)) {
                continue;
            }

            currentCombination.add(subset);
            usedElements.addAll(subset.getProducts());

            generateAllCombinations(subsets, fullSet, maxSubsets, currentCombination, i + 1, usedElements, results);

            currentCombination.remove(currentCombination.size() - 1);
            subset.getProducts().forEach(usedElements::remove);
        }
    }
}

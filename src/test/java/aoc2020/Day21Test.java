package aoc2020;

import org.junit.Test;
import util.Util;
import util.Bipartite;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day21Test {

  public static final String DAY = "21";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(2627, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(5, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals("hn,dgsdtj,kpksf,sjcvsr,bstzgn,kmmqmv,vkdxfj,bsfqgb", solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals("mxmxvkd,sqjhc,fvjkl", solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    List<Recipe> recipes = input.stream().map(Recipe::new).collect(Collectors.toList());

    List<String> allIngredients = allIngredients(recipes);
    List<String> allAllergens = allAllergens(recipes);
    Map<String, Set<String>> edges = edges(recipes, allIngredients, allAllergens);

    return recipes.stream().flatMap(r -> r.ingredients.stream())
            .filter(ingredient -> edges.get(ingredient).isEmpty())
            .count();
  }

  private String solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<Recipe> recipes = input.stream().map(Recipe::new).collect(Collectors.toList());

    List<String> allIngredients = allIngredients(recipes);
    List<String> allAllergens = allAllergens(recipes);
    Map<String, Set<String>> edges = edges(recipes, allIngredients, allAllergens);

    Bipartite<String, String> bipartite = Bipartite.from(allIngredients, allAllergens, edges);

    return allAllergens.stream()
            .sorted()
            .map(bipartite::getLeftMatch)
            .collect(Collectors.joining(","));
  }

  private Map<String, Set<String>> edges(List<Recipe> recipes, List<String> allIngredients, List<String> allAllergens) {
    Map<String, Set<String>> edges = allIngredients.stream()
            .collect(Collectors.toMap(Function.identity(), ignore -> new HashSet<>()));

    for (Recipe recipe : recipes) {
      for (String ingredient : recipe.ingredients) {
        edges.get(ingredient).addAll(recipe.allergens);
        for (String allergen : recipe.allergens) {
          edges.get(ingredient).add(allergen);
        }
      }
    }

    for (Recipe recipe : recipes) {
      for (String ingredient : allIngredients) {
        if (!recipe.ingredients.contains(ingredient)) {
          edges.get(ingredient).removeAll(recipe.allergens);
        }
      }
    }

    return edges;
  }

  private List<String> allAllergens(List<Recipe> recipes) {
    return recipes.stream()
            .flatMap(r -> r.allergens.stream())
            .distinct()
            .collect(Collectors.toList());
  }

  private List<String> allIngredients(List<Recipe> recipes) {
    return recipes.stream()
              .flatMap(r -> r.ingredients.stream())
              .distinct()
              .collect(Collectors.toList());
  }



  private class Recipe {
    final Set<String> ingredients = new HashSet<>();
    final Set<String> allergens = new HashSet<>();
    public Recipe(String line) {
      boolean b = true;
      String[] parts = line.split("[ \\(\\),]+");
      for (String part : parts) {
        if (part.equals("contains")) {
          b = false;
        } else if (b) {
          ingredients.add(part);
        } else {
          allergens.add(part);
        }
      }
    }
  }
}

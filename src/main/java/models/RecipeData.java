package models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecipeData {
    private long craftType;
    private long recipeLevel;
    private long itemId;
    private long amount;
    private long ingredientId1;
    private long ingredientAmount1;
    private long ingredientId2;
    private long ingredientAmount2;
    private long ingredientId3;
    private long ingredientAmount3;
    private long ingredientId4;
    private long ingredientAmount4;
    private long ingredientId5;
    private long ingredientAmount5;
    private long ingredientId6;
    private long ingredientAmount6;
    private long ingredientId7;
    private long ingredientAmount7;
    private long ingredientId8;
    private long ingredientAmount8;
    private long ingredientId9;
    private long ingredientAmount9;
    private long ingredientId10;
    private long ingredientAmount10;
    private long recipeElement;
    private long difficultyFactor;
    private long qualityFactor;
    private long durabilityFactor;
    private String canHQ;
    private String expRewarded;
    private String isSpecialization;
    private List<Ingredient> ingredientList = new ArrayList<Ingredient>();

    public void initializeIngredientData() {
        if (ingredientId1 > 0 && ingredientAmount1 > 0) {
            ingredientList.add(new Ingredient(ingredientId1, ingredientAmount1));
        }
        if (ingredientId2 > 0 && ingredientAmount2 > 0) {
            ingredientList.add(new Ingredient(ingredientId2, ingredientAmount2));
        }
        if (ingredientId3 > 0 && ingredientAmount3 > 0) {
            ingredientList.add(new Ingredient(ingredientId3, ingredientAmount3));
        }
        if (ingredientId4 > 0 && ingredientAmount4 > 0) {
            ingredientList.add(new Ingredient(ingredientId4, ingredientAmount4));
        }
        if (ingredientId5 > 0 && ingredientAmount5 > 0) {
            ingredientList.add(new Ingredient(ingredientId5, ingredientAmount5));
        }
        if (ingredientId6 > 0 && ingredientAmount6 > 0) {
            ingredientList.add(new Ingredient(ingredientId6, ingredientAmount6));
        }
        if (ingredientId7 > 0 && ingredientAmount7 > 0) {
            ingredientList.add(new Ingredient(ingredientId7, ingredientAmount7));
        }
        if (ingredientId8 > 0 && ingredientAmount8 > 0) {
            ingredientList.add(new Ingredient(ingredientId8, ingredientAmount8));
        }
        if (ingredientId9 > 0 && ingredientAmount9 > 0) {
            ingredientList.add(new Ingredient(ingredientId9, ingredientAmount9));
        }
        if (ingredientId10 > 0 && ingredientAmount10 > 0) {
            ingredientList.add(new Ingredient(ingredientId10, ingredientAmount10));
        }
    }
}

package org.launchcode.RecipeKeeper.models.forms;

import org.launchcode.RecipeKeeper.models.Ingredient;
import org.launchcode.RecipeKeeper.models.Recipe;

import javax.validation.constraints.NotNull;

public class AddIngredientAndQuantityToRecipeForm {

    private Recipe recipe;

    private Ingredient ingredient;

    @NotNull
    private int recipeId;

    @NotNull
    private int ingredientId;

    @NotNull
    private String amount;

    public AddIngredientAndQuantityToRecipeForm() {
    }

    public AddIngredientAndQuantityToRecipeForm(Recipe recipe, Ingredient ingredient, String amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

package org.launchcode.RecipeKeeper.models.forms;

import org.launchcode.RecipeKeeper.models.Ingredient;
import org.launchcode.RecipeKeeper.models.Recipe;

import javax.validation.constraints.NotNull;

public class AddIngredientAndQuantityToRecipeForm {

    private Recipe recipe;

    private Iterable<Ingredient> ingredients;

    @NotNull
    private int recipeId;

    @NotNull
    private int ingredientId;

    @NotNull
    private String amount;

    public AddIngredientAndQuantityToRecipeForm() {
    }

    public AddIngredientAndQuantityToRecipeForm(Recipe recipe, Iterable<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Iterable<Ingredient> getIngredients() {
        return ingredients;
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

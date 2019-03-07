package org.launchcode.RecipeKeeper.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class IngredientAndQuantity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    private Recipe recipe;

    @NotNull
    @ManyToOne
    private Ingredient ingredient;

    @NotNull
    private String amount;

    public IngredientAndQuantity(Recipe recipe, Ingredient ingredient, String amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public IngredientAndQuantity() {
    }

    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}

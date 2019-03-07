package org.launchcode.RecipeKeeper.models.forms;

import org.launchcode.RecipeKeeper.models.Recipe;

import javax.validation.constraints.NotNull;

public class AddRateCommentToRecipeForm {

    private Recipe recipe;

    @NotNull
    private String rating;

    @NotNull
    private String comment;

    @NotNull
    private int recipeId;

    public AddRateCommentToRecipeForm() {
    }

    public AddRateCommentToRecipeForm(Recipe recipe, String rating, String comment) {
        this.recipe = recipe;
        this.rating = rating;
        this.comment = comment;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}

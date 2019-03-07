package org.launchcode.RecipeKeeper.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class RateComment {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    private Recipe recipe;

    @NotNull
    private String rating;

    @NotNull
    private String comment;

    public RateComment(Recipe recipe, String rating, String comment) {
        this.recipe = recipe;
        this.rating = rating;
        this.comment = comment;
    }

    public RateComment() {
    }

    public int getId() {
        return id;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}

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
    private int rating;

    @NotNull
    private String comment;

//    public RateComment(Recipe recipe, i rating, String comment) {
//        this.recipe = recipe;
//        this.rating = rating;
//        this.comment = comment;
//    }


    public RateComment(Recipe recipe, int rating, String comment) {
        this.recipe = recipe;
        this.rating = rating;
        this.comment = comment;
    }

    public RateComment() {
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    //    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }

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

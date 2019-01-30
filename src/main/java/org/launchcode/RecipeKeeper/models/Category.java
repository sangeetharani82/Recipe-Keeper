package org.launchcode.RecipeKeeper.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String categoryName;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Recipe> recipes = new ArrayList<>();

    public Category(){}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}

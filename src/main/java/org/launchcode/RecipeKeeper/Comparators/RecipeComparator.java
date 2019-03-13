package org.launchcode.RecipeKeeper.Comparators;

import org.launchcode.RecipeKeeper.models.Recipe;

import java.util.Comparator;

public class RecipeComparator implements Comparator<Recipe> {
    @Override
    public int compare(Recipe o1, Recipe o2){
        return o1.getRecipeName().compareToIgnoreCase(o2.getRecipeName());
    }
}

package org.launchcode.RecipeKeeper.Comparators;

import org.launchcode.RecipeKeeper.models.Category;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Category> {
    @Override
    public int compare(Category o1, Category o2){
        return o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName());
    }
}

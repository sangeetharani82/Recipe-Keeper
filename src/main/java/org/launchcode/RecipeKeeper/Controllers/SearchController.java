package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparator.RecipeComparator;
import org.launchcode.RecipeKeeper.models.AddIngredientsToRecipe;
import org.launchcode.RecipeKeeper.models.Recipe;
import org.launchcode.RecipeKeeper.models.data.CategoryDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RecipeDao recipeDao;

    RecipeComparator recipeComparator = new RecipeComparator();

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("title", "Enter a search term");
        return "search/result";
    }

    @RequestMapping(value = "results")
    public String results(Model model, @RequestParam String searchTerm){
        int count = 0;
        //int n = 0;
        ArrayList<Recipe> lists = new ArrayList<>();
        ArrayList<Recipe> newLists = new ArrayList<>();
        for (Recipe recipe : recipeDao.findAll()) {
            if (recipe.getRecipeName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    (recipe.getCategory().getCategoryName().toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (recipe.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()))) {
                int id = recipe.getId();
                Recipe recipe1 = recipeDao.findOne(id);
                lists.add(recipe1);
                for(Recipe list : lists){
                    if (!newLists.contains(list)){
                        newLists.add(list);
                    }
                }
                newLists.sort(recipeComparator);
                model.addAttribute("recipes", newLists);
                count = count + 1;
                model.addAttribute("title", count + " item(s) found");
            }else{
                for (AddIngredientsToRecipe i : recipe.getAddIngredientsToRecipes()){
                    if (i.getIngredient().getIngredientName().toLowerCase().contains(searchTerm.toLowerCase())){
                        int id = recipe.getId();
                        Recipe recipe1 = recipeDao.findOne(id);
                        lists.add(recipe1);
                        for(Recipe list : lists){
                            if (!newLists.contains(list)){
                                newLists.add(list);
                            }
                        }
                        newLists.sort(recipeComparator);
                        model.addAttribute("recipes", newLists);
                        count = count + 1;
                        model.addAttribute("title", count + " item(s) found");
                    }
                }
            }
        }

        if (count == 0){
            model.addAttribute("message", "No recipes found under the name " + searchTerm);
            return "recipe/message";
        }
        return "search/result";
    }
}

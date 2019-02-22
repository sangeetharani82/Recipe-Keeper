package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.models.AddIngredientsToRecipe;
import org.launchcode.RecipeKeeper.models.Ingredient;
import org.launchcode.RecipeKeeper.models.Recipe;
import org.launchcode.RecipeKeeper.models.data.AddIngredientsDao;
import org.launchcode.RecipeKeeper.models.data.IngredientDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("addIngredients")
public class AddIngredientsController {

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private IngredientDao ingredientDao;

    @Autowired
    private AddIngredientsDao addIngredientsDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Pick a recipe and add the ingredients");
        model.addAttribute("recipes", recipeDao.findAll());
        return "addIngredients/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddIngredientsForm(Model model){
        model.addAttribute("title", "Add ingredients to the Recipe");
        model.addAttribute("recipes", recipeDao.findAll());
        model.addAttribute("ingredients", ingredientDao.findAll());
        model.addAttribute("add", new AddIngredientsToRecipe());
        return "addIngredients/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddIngredientsForm(@RequestParam int recipeId, @RequestParam int ingredientId,
                                            Model model, @ModelAttribute @Valid AddIngredientsToRecipe newSet,
                                            Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add ingredients to the Recipe");
            model.addAttribute("recipes", recipeDao.findAll());
            model.addAttribute("ingredients", ingredientDao.findAll());
            return "addIngredients/add";
        }
        Recipe recipe = recipeDao.findOne(recipeId);
        newSet.setRecipe(recipe);

        Ingredient ingredient = ingredientDao.findOne(ingredientId);
        newSet.setIngredient(ingredient);

        addIngredientsDao.save(newSet);
        model.addAttribute("message", "Successfully added!");
        List<AddIngredientsToRecipe> lists = recipe.getAddIngredientsToRecipes();
        model.addAttribute("title", "Ingredients needed for " + recipe.getRecipeName());
        model.addAttribute("ingredientLists", lists);
        return "addIngredients/viewWithMsg";
    }

    // delete each Category
    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable int id , Model model){
        addIngredientsDao.delete(id);
        model.addAttribute("message", "Successfully deleted!");
        return "addIngredients/message";
    }

    @RequestMapping(value = "recipe", method = RequestMethod.GET)
    public String Recipe(Model model, @RequestParam int id){
        Recipe recipe = recipeDao.findOne(id);
        List<AddIngredientsToRecipe> lists = recipe.getAddIngredientsToRecipes();
        model.addAttribute("title", "Ingredients needed for " + recipe.getRecipeName());
        model.addAttribute("ingredientLists", lists);
        return "addIngredients/view";
    }
}

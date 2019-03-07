package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparator.RecipeComparator;
import org.launchcode.RecipeKeeper.models.*;
import org.launchcode.RecipeKeeper.models.data.*;
import org.launchcode.RecipeKeeper.models.forms.AddIngredientAndQuantityToRecipeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "recipe")
public class RecipeController {

    @Autowired
    RecipeDao recipeDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    IngredientDao ingredientDao;

    @Autowired
    IngredientAndQuantityDao ingredientAndQuantityDao;

    @Autowired
    CategoryDao categoryDao;

    RecipeComparator recipeComparator = new RecipeComparator();

    // Request path: /recipe
    @RequestMapping(value = "")
    public String index(Model model) {
        ArrayList<Recipe> lists = new ArrayList<>();
        for (Recipe recipe : recipeDao.findAll()){
            lists.add(recipe);
        }
        lists.sort(recipeComparator);
        model.addAttribute("recipes", lists);
        model.addAttribute("title", "Recipes");
        return "recipe/index";
    }

    // add/create a recipe
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddRecipeForm(Model model) {
        model.addAttribute("title", "Add a recipe");
        model.addAttribute(new Recipe());
        model.addAttribute("courses", courseDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        return "recipe/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddRecipeForm(Model model, @ModelAttribute @Valid Recipe newRecipe,
                                       Errors errors, @RequestParam int courseId, @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a recipe");
            model.addAttribute("courses", courseDao.findAll());
            model.addAttribute("categories", categoryDao.findAll());
            return "recipe/add";
        }
        Course cor = courseDao.findOne(courseId);
        Category cat = categoryDao.findOne(categoryId);
        newRecipe.setCourse(cor);
        newRecipe.setCategory(cat);
        recipeDao.save(newRecipe);

        model.addAttribute("message", "Recipe added successfully!");
        model.addAttribute("title", "Add ingredients to " + newRecipe.getRecipeName());

        return "redirect:view/" + newRecipe.getId();
    }

    @RequestMapping(value="view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable int id, Model model){
        Recipe recipe = recipeDao.findOne(id);
        model.addAttribute("title", recipe.getRecipeName());
        model.addAttribute("recipe", recipe);
        model.addAttribute("message", "Added successfully!");
        return "recipe/view";
    }

    @RequestMapping(value = "add-ingredient/{recipeId}", method = RequestMethod.GET)
    public String displayAddIngredientAndQuantityForm(@PathVariable int recipeId, Model model){
        Recipe recipe = recipeDao.findOne(recipeId);
        AddIngredientAndQuantityToRecipeForm form = new AddIngredientAndQuantityToRecipeForm(recipe, ingredientDao.findAll());
        model.addAttribute("title", "Add Ingredient and Quantity to "+recipe.getRecipeName());
        model.addAttribute("form", form);
        model.addAttribute("ingredients", ingredientDao.findAll());
        return "recipe/add-ingredient";
    }

    @RequestMapping(value = "add-ingredient", method = RequestMethod.POST)
    public String processAddIngredientAndQuantityForm(Model model, @ModelAttribute @Valid AddIngredientAndQuantityToRecipeForm form,
                                                      Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("form", form);
            return "recipe/add-ingredient";
        }

        Recipe recipe = recipeDao.findOne(form.getRecipeId());
        Ingredient theIngredient = ingredientDao.findOne(form.getIngredientId());
        IngredientAndQuantity ingredientAndQuantity = new IngredientAndQuantity(recipe, theIngredient, form.getAmount());
        recipe.setIngredientAndQuantityList(ingredientAndQuantity);
        ingredientAndQuantityDao.save(ingredientAndQuantity);
        recipeDao.save(recipe);
        return "redirect:view/"+ recipe.getId();
    }

    //view single recipe
    @RequestMapping(value="single/{id}", method = RequestMethod.GET)
    public String singleRecipe(@PathVariable int id, Model model){
        Recipe recipe = recipeDao.findOne(id);
        model.addAttribute("title", recipe.getRecipeName());
        model.addAttribute("course", recipe.getCourse());
        model.addAttribute("category", recipe.getCategory());
        model.addAttribute("recipe", recipe);
        model.addAttribute("title", recipe.getRecipeName());
        model.addAttribute("ingredientLists", recipe.getIngredientAndQuantities());
        return "recipe/single";
    }

    //recipes in a course
    @RequestMapping(value = "course", method = RequestMethod.GET)
    public String course(Model model, @RequestParam int id){
        Course cor = courseDao.findOne(id);
        List<Recipe> recipes = cor.getRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("title", cor.getCourseName() + " recipes");
        return "recipe/list-under";
    }
    //recipes in a category
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String category(Model model, @RequestParam int id){
        Category cat = categoryDao.findOne(id);
        List<Recipe> recipes = cat.getRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("title", cat.getCategoryName() + " recipes");
        return "recipe/list-under";
    }

    // delete each recipe instantly
    @RequestMapping(value = "delete/{recipeId}")
    public String delete(@PathVariable int recipeId, Model model){
        recipeDao.delete(recipeId);
        model.addAttribute("message", "Recipe deleted successfully!");
        return "recipe/message";
        //return "redirect:/recipe";
    }

    //delete the ingredient and quantity from the recipe
    @RequestMapping(value = "remove/{ingredientAndQuantityId}")
    public String removeIngredientAndQuantity(@PathVariable int ingredientAndQuantityId, Model model){
        IngredientAndQuantity ingredientAndQuantity = ingredientAndQuantityDao.findOne(ingredientAndQuantityId);
        ingredientAndQuantityDao.delete(ingredientAndQuantity);
        model.addAttribute("message", "Ingredient and Quantity removed successfully");
        return "recipe/message";
    }

    //Edit a recipe
    @RequestMapping(value="edit/{recipeId}", method = RequestMethod.GET)
    public String displayEditRecipeForm(Model model, @PathVariable int recipeId){
        model.addAttribute(recipeDao.findOne(recipeId));
        model.addAttribute("title", "Edit " + recipeDao.findOne(recipeId).getRecipeName());
        model.addAttribute("courses", courseDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        return "recipe/edit";
    }

    @RequestMapping(value = "edit/{recipeId}", method = RequestMethod.POST)
    public String processEditForm(@PathVariable int recipeId, @RequestParam String recipeName,
                                  @RequestParam int courseId, @RequestParam int categoryId,
                                  @RequestParam int servingSize, @RequestParam String prepTime,
                                  @RequestParam String cookTime, Model model,
                                  @RequestParam String direction){
        Recipe edited = recipeDao.findOne(recipeId);
        edited.setRecipeName(recipeName);
        edited.setServingSize(servingSize);
        edited.setPrepTime(prepTime);
        edited.setCookTime(cookTime);
        edited.setDirection(direction);


        Course cor = courseDao.findOne(courseId);
        edited.setCourse(cor);

        Category cat = categoryDao.findOne(categoryId);
        edited.setCategory(cat);

        recipeDao.save(edited);

        model.addAttribute("message", "Successfully edited!");
        model.addAttribute("recipe", edited);
        model.addAttribute("title", "Ingredients needed for " + edited.getRecipeName());
        model.addAttribute("ingredientLists", edited.getIngredientAndQuantities());
        return "recipe/view";
    }
}

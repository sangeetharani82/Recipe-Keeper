package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparators.CategoryComparator;
import org.launchcode.RecipeKeeper.Comparators.CourseComparator;
import org.launchcode.RecipeKeeper.Comparators.IngredientComparator;
import org.launchcode.RecipeKeeper.Comparators.RecipeComparator;
import org.launchcode.RecipeKeeper.models.*;
import org.launchcode.RecipeKeeper.models.data.*;
import org.launchcode.RecipeKeeper.models.forms.AddIngredientAndQuantityToRecipeForm;
import org.launchcode.RecipeKeeper.models.forms.AddRateCommentToRecipeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "all")
public class RecipeController {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Autowired
    RecipeDao recipeDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    IngredientDao ingredientDao;

    @Autowired
    IngredientAndQuantityDao ingredientAndQuantityDao;

    @Autowired
    RateCommentDao rateCommentDao;

    @Autowired
    CategoryDao categoryDao;

    RecipeComparator recipeComparator = new RecipeComparator();
    CourseComparator courseComparator = new CourseComparator();
    CategoryComparator categoryComparator = new CategoryComparator();
    IngredientComparator ingredientComparator = new IngredientComparator();

    // Request path: /all
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
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseDao.findAll()){
            courses.add(course);
        }
        courses.sort(courseComparator);

        ArrayList<Category> categories = new ArrayList<>();
        for (Category category : categoryDao.findAll()){
            categories.add(category);
        }
        categories.sort(categoryComparator);

        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
        return "recipe/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddRecipeForm(Model model, @ModelAttribute @Valid Recipe newRecipe,
                                       Errors errors, @RequestParam int courseId, @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a recipe");
            ArrayList<Course> courses = new ArrayList<>();
            for (Course course : courseDao.findAll()){
                courses.add(course);
            }
            courses.sort(courseComparator);

            ArrayList<Category> categories = new ArrayList<>();
            for (Category category : categoryDao.findAll()){
                categories.add(category);
            }
            categories.sort(categoryComparator);

            model.addAttribute("courses", courses);
            model.addAttribute("categories", categories);
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
        model.addAttribute("title", "Ingredients needed for " + recipe.getRecipeName());
        model.addAttribute("recipe", recipe);
        model.addAttribute("message", "Added successfully!");
        return "recipe/view";
    }

    @RequestMapping(value = "add-ingredient/{recipeId}", method = RequestMethod.GET)
    public String displayAddIngredientAndQuantityForm(@PathVariable int recipeId, Model model){
        Recipe recipe = recipeDao.findOne(recipeId);
        String amount = "";
        Ingredient ingredient = new Ingredient();
        AddIngredientAndQuantityToRecipeForm form = new AddIngredientAndQuantityToRecipeForm(recipe, ingredient, amount);
        model.addAttribute("title", "Add Ingredient and Quantity to "+recipe.getRecipeName());
        model.addAttribute("form", form);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient i : ingredientDao.findAll()){
            ingredients.add(i);
        }
        ingredients.sort(ingredientComparator);
        model.addAttribute("ingredients", ingredients);
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

    // delete each recipe instantly
    @RequestMapping(value = "delete/{recipeId}")
    public String delete(@PathVariable int recipeId, Model model){
        Recipe recipe = recipeDao.findOne(recipeId);
        rateCommentDao.delete(recipe.getRateCommentList());
        ingredientAndQuantityDao.delete(recipe.getIngredientAndQuantities());
        recipeDao.delete(recipeId);
        model.addAttribute("message", "Recipe deleted successfully!");
        return "recipe/message";
    }

    //delete the ingredient and quantity from the recipe
    @RequestMapping(value = "remove/{recipeId}/{ingredientAndQuantityId}")
    public String removeIngredientAndQuantity(@PathVariable int recipeId, @PathVariable int ingredientAndQuantityId,
                                              Model model){
        Recipe recipe = recipeDao.findOne(recipeId);
        IngredientAndQuantity ingredientAndQuantity = ingredientAndQuantityDao.findOne(ingredientAndQuantityId);
        ingredientAndQuantityDao.delete(ingredientAndQuantity);
        model.addAttribute("message", "Ingredient and Quantity removed successfully");
        //return "redirect:/recipe/view/"+ recipe.getId();
        model.addAttribute("title", "Ingredients needed for " + recipe.getRecipeName());
        model.addAttribute("recipe", recipe);
        return "recipe/viewWithMsg";
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
        List<RateComment> ratings = recipe.getRateCommentList();
        double total = 0;
        double average = 0;
        if (ratings.size() > 0){
            for (RateComment rating : ratings){
                total = total + rating.getRating();
            }
            average = total / ratings.size();
            model.addAttribute("average", df2.format(average));
        }else {
            model.addAttribute("average", 0);
        }
        return "recipe/single";
    }

    //recipes in a course
    @RequestMapping(value = "course", method = RequestMethod.GET)
    public String course(Model model, @RequestParam int id){
        Course cor = courseDao.findOne(id);
        List<Recipe> recipes = cor.getRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("title", cor.getCourseName() + " recipes");
        for (Recipe recipe : recipes){
            List<RateComment> ratings = recipe.getRateCommentList();
            double total = 0;
            double average = 0;
            if (ratings.size() > 0){
                for (RateComment rating : ratings){
                    total = total + rating.getRating();
                }
                average = total / ratings.size();
                model.addAttribute("average", df2.format(average));
            }else {
                model.addAttribute("average", 0);
            }
        }
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

    //Edit a recipe
    @RequestMapping(value="edit/{recipeId}", method = RequestMethod.GET)
    public String displayEditRecipeForm(Model model, @PathVariable int recipeId){
        model.addAttribute(recipeDao.findOne(recipeId));
        model.addAttribute("title", "Edit " + recipeDao.findOne(recipeId).getRecipeName());
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseDao.findAll()){
            courses.add(course);
        }
        courses.sort(courseComparator);

        ArrayList<Category> categories = new ArrayList<>();
        for (Category category : categoryDao.findAll()){
            categories.add(category);
        }
        categories.sort(categoryComparator);

        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
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
        return "recipe/view";
    }

    @RequestMapping(value = "add-rating/{recipeId}", method = RequestMethod.GET)
    public String displayAddRatingForm(@PathVariable int recipeId, Model model){
        int rating = 0;
        String comment = "";
        Recipe recipe = recipeDao.findOne(recipeId);
        AddRateCommentToRecipeForm rateForm = new AddRateCommentToRecipeForm(recipe, rating, comment);
        model.addAttribute("title", "Rate and Comment "+recipe.getRecipeName());
        model.addAttribute("form", rateForm);
        return "recipe/add-rating";
    }

    @RequestMapping(value = "add-rating", method = RequestMethod.POST)
    public String processAddRatingForm(Model model, @ModelAttribute @Valid AddRateCommentToRecipeForm rateForm,
                                       Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("form", rateForm);
            return "recipe/add-rating";
        }
        Recipe recipe = recipeDao.findOne(rateForm.getRecipeId());
        RateComment rateComment = new RateComment(recipe, rateForm.getRating(), rateForm.getComment());
        recipe.setRateCommentList(rateComment);
        rateCommentDao.save(rateComment);
        recipeDao.save(recipe);
        return "redirect:view-ratingWithMsg/"+ recipe.getId();
    }

    @RequestMapping(value="view-ratingWithMsg/{id}", method = RequestMethod.GET)
    public String viewRatingWithSuccessMsg(@PathVariable int id, Model model){
        Recipe recipe = recipeDao.findOne(id);
        model.addAttribute("title", recipe.getRecipeName());
        model.addAttribute("recipe", recipe);
        model.addAttribute("message", "Added successfully!");
        return "recipe/view-ratingWithMsg";
    }

    @RequestMapping(value="view-rating/{id}", method = RequestMethod.GET)
    public String viewRating(@PathVariable int id, Model model){
        Recipe recipe = recipeDao.findOne(id);
        model.addAttribute("title", recipe.getRecipeName());
        model.addAttribute("recipe", recipe);
        return "recipe/view-rating";
    }

    //Edit the ingredients and quantities of a recipe
    @RequestMapping(value = "edit-ingredients/{recipeId}", method = RequestMethod.GET)
    public String displayEditIngredientsAndQuantity(Model model, @PathVariable int recipeId){
        Recipe recipe = recipeDao.findOne(recipeId);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient i : ingredientDao.findAll()){
            ingredients.add(i);
        }
        ingredients.sort(ingredientComparator);

        model.addAttribute("recipe", recipe);
        model.addAttribute("form", new IngredientAndQuantity());
        model.addAttribute("title", "Edit " + recipe.getRecipeName() + " ingredients");
        model.addAttribute("ingredients", ingredients);
        return "recipe/edit-ingredients";
    }

//    // can't edit yet
//    @RequestMapping(value = "edit-ingredients/{recipeId}", method = RequestMethod.POST)
//    public String processEditIngredientsAndQuantity(Model model, @PathVariable int recipeId){
//
//        Recipe editedList = recipeDao.findOne(recipeId);
//
//        recipeDao.save(editedList);
//
//
//        model.addAttribute("message", "Successfully edited!");
//        model.addAttribute("recipe", editedList);
//        model.addAttribute("title", "Ingredients needed for " + editedList.getRecipeName());
//
//        //return "redirect:view/"+ editedList.getId();
//        return "recipe/view";
//    }
}

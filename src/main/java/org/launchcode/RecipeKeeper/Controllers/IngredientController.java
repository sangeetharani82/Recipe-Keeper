package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparators.IngredientComparator;
import org.launchcode.RecipeKeeper.models.Ingredient;
import org.launchcode.RecipeKeeper.models.data.IngredientDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("ingredient")
public class IngredientController {

    @Autowired
    private IngredientDao ingredientDao;

    @Autowired
    private RecipeDao recipeDao;

    IngredientComparator ingredientComparator = new IngredientComparator();
    @RequestMapping(value="")
    public String index(Model model){
        ArrayList<Ingredient> lists = new ArrayList<>();
        for (Ingredient ingredient: ingredientDao.findAll()){
            lists.add(ingredient);
        }
        lists.sort(ingredientComparator);
        model.addAttribute("title", "Ingredients");
        model.addAttribute("ingredients", lists);
        return "ingredient/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddForm(Model model){
        model.addAttribute("title", "Add an ingredient");
        model.addAttribute("ingredient", new Ingredient());
        return "ingredient/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddForm(Model model, @ModelAttribute @Valid Ingredient ingredient, Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add an ingredient");
            return "ingredient/add";
        }
        ingredientDao.save(ingredient);
        model.addAttribute("message", "Successfully added!");
        return "ingredient/message";
        //return "redirect:";
    }

    @RequestMapping(value="edit/{ingredientId}", method = RequestMethod.GET)
    public String displayEditIngredientForm(Model model, @PathVariable int ingredientId){
        model.addAttribute(ingredientDao.findOne(ingredientId));
        model.addAttribute("title", "Edit");
        model.addAttribute("ingredientName", ingredientDao.findOne(ingredientId).getIngredientName());
        return "ingredient/edit";
    }

    @RequestMapping(value = "edit/{ingredientId}", method = RequestMethod.POST)
    public String processEditForm(@PathVariable int ingredientId, @RequestParam String ingredientName, Model model){
        Ingredient edited = ingredientDao.findOne(ingredientId);
        edited.setIngredientName(ingredientName);
        ingredientDao.save(edited);
        model.addAttribute("message", "Successfully edited and saved!");
        return "ingredient/message";
        //return "redirect:/ingredient";
    }

    @RequestMapping(value = "delete/{ingredientId}")
    public String delete(@PathVariable int ingredientId, Model model){
        ingredientDao.delete(ingredientId);
        model.addAttribute("message", "Successfully deleted!");
        return "ingredient/message";
        //return "redirect:/ingredient";
    }
}

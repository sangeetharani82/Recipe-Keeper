package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.models.RateComment;
import org.launchcode.RecipeKeeper.models.Recipe;
import org.launchcode.RecipeKeeper.models.data.RateCommentDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("rate")
public class RateCommentController {

    @Autowired
    private RateCommentDao rateCommentDao;

    @Autowired
    private RecipeDao recipeDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Ratings and comments");
        model.addAttribute("recipes", recipeDao.findAll());
        model.addAttribute("lists", rateCommentDao.findAll());
        return "rate/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayRatingForm(Model model){

        model.addAttribute("recipes", recipeDao.findAll());
        model.addAttribute("title", "Rate & Comment");
        model.addAttribute("rate", new RateComment());
        return "rate/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processRatingForm(@RequestParam int recipeId, Model model, @ModelAttribute @Valid RateComment newRate,
                                    Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title", "Select a Recipe to rate and comment");
            model.addAttribute("recipes", recipeDao.findAll());
            return "rate/add";
        }
        Recipe recipe = recipeDao.findOne(recipeId);
        newRate.setRecipe(recipe);
        rateCommentDao.save(newRate);
        return "redirect:";
    }

    @RequestMapping(value = "recipe", method = RequestMethod.GET)
    public String Recipe(Model model, @RequestParam int id){
        Recipe recipe = recipeDao.findOne(id);
        List<RateComment> rateComments = recipe.getRateCommentList();
        model.addAttribute("ratings", rateComments);
        model.addAttribute("title", "Rating and comments for " + recipe.getRecipeName());
        return "rate/view";
    }
}

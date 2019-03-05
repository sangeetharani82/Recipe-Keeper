package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparator.CategoryComparator;
import org.launchcode.RecipeKeeper.models.Category;
import org.launchcode.RecipeKeeper.models.data.CategoryDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RecipeDao recipeDao;

    CategoryComparator categoryComparator = new CategoryComparator();

    @RequestMapping(value="")
    public String index(Model model){
        ArrayList<Category> lists = new ArrayList<>();
        for (Category category : categoryDao.findAll()){
            lists.add(category);
        }
        lists.sort(categoryComparator);
        model.addAttribute("categories", lists);
        model.addAttribute("title", "Categories");
        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddForm(Model model){
        model.addAttribute("title", "Add Category");
        model.addAttribute("category", new Category());
        return "category/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddForm(Model model,
                                 @ModelAttribute @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add Category");
            return "category/add";
        }
        categoryDao.save(category);

        model.addAttribute("message", "Successfully added!");
        return "category/message";
        //return "redirect:";
    }

    // delete each Category
    @RequestMapping(value = "delete/{categoryId}")
    public String delete(@PathVariable int categoryId, Model model){
        categoryDao.delete(categoryId);
        model.addAttribute("message", "Successfully deleted!");
        return "category/message";
        //return "redirect:/category";
    }
}

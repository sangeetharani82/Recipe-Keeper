package org.launchcode.RecipeKeeper.Controllers;

import org.hibernate.annotations.Cache;
import org.launchcode.RecipeKeeper.models.Category;
import org.launchcode.RecipeKeeper.models.Course;
import org.launchcode.RecipeKeeper.models.Recipe;
import org.launchcode.RecipeKeeper.models.data.CategoryDao;
import org.launchcode.RecipeKeeper.models.data.CourseDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "course")
public class CourseController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private RecipeDao recipeDao;

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("title", "Courses");
        model.addAttribute("courses", courseDao.findAll());
        return "course/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddForm(Model model){
        model.addAttribute("title", "Add Course");
        model.addAttribute("course", new Course());
        return "course/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddForm(Model model, @ModelAttribute @Valid Course course, Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add Course");
            return "course/add";
        }
        courseDao.save(course);

        model.addAttribute("message", "Successfully added!");
        return "message";
        //return "redirect:";
    }

    // delete each course
    @RequestMapping(value = "delete/{courseId}")
    public String delete(@PathVariable int courseId, Model model){
        courseDao.delete(courseId);
        model.addAttribute("message", "Successfully deleted!");
        return "message";
        //return "redirect:/course";
    }

}

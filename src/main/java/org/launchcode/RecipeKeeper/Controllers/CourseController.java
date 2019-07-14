package org.launchcode.RecipeKeeper.Controllers;

import org.launchcode.RecipeKeeper.Comparators.CourseComparator;
import org.launchcode.RecipeKeeper.models.Course;
import org.launchcode.RecipeKeeper.models.data.CourseDao;
import org.launchcode.RecipeKeeper.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "recipe")
//@RequestMapping(value = "course")
public class CourseController {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private RecipeDao recipeDao;

    CourseComparator courseComparator = new CourseComparator();

    @RequestMapping(value="")
    public String index(Model model){
        ArrayList<Course> lists = new ArrayList<>();
        for (Course course : courseDao.findAll()){
            lists.add(course);
        }
        lists.sort(courseComparator);
        model.addAttribute("title", "Courses");
        model.addAttribute("courses", lists);
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
        return "course/message";
        //return "redirect:";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String displayCourseLists(Model model){
        ArrayList<Course> lists = new ArrayList<>();
        for (Course course : courseDao.findAll()){
            lists.add(course);
        }
        lists.sort(courseComparator);
        model.addAttribute("title", "Edit a course");
        model.addAttribute("courses", lists);
        return "course/list";
    }

    @RequestMapping(value="edit/{courseId}", method = RequestMethod.GET)
    public String displayEditCourseForm(Model model, @PathVariable int courseId){
        model.addAttribute(courseDao.findOne(courseId));
        model.addAttribute("title", "Edit");
        model.addAttribute("courseName", courseDao.findOne(courseId).getCourseName());
        return "course/edit";
    }

    @RequestMapping(value = "edit/{courseId}", method = RequestMethod.POST)
    public String processEditForm(@PathVariable int courseId, @RequestParam String courseName, Model model){
        Course edited = courseDao.findOne(courseId);
        edited.setCourseName(courseName);
        courseDao.save(edited);
        model.addAttribute("message", "Successfully edited and saved!");
        return "course/message";
        //return "redirect:/ingredient";
    }

    // delete each course
    @RequestMapping(value = "delete/{courseId}")
    public String delete(@PathVariable int courseId, Model model){
        courseDao.delete(courseId);
        model.addAttribute("message", "Successfully deleted!");
        return "course/message";
        //return "redirect:/course";
    }

}

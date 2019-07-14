package org.launchcode.RecipeKeeper.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="option")
public class OptionController {

    @RequestMapping(value = "")
    public String index (Model model){
        model.addAttribute("title", "What do you want to do?");
        return "option/index";
    }
}

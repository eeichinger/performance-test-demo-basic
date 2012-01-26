package org.eeichinger.performancetestdemo.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
@RequestMapping("/welcome")
public class WelcomeController {
	@RequestMapping(method = RequestMethod.GET)
	public String get(ModelMap model) {
		model.addAttribute("message", "Spring Security Hello World");
		return "welcome";
	}
}

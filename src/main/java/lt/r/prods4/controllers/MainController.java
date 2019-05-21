package lt.r.prods4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("**")
public class MainController {

	@GetMapping("/k")
	public String go() {
		return "index2.html";
	}
}

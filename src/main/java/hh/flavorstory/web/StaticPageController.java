package hh.flavorstory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaticPageController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("message") String message,
                                Model model) {
        // Simple server-side handling: log to console for now.
        System.out.println("Contact form submitted: " + name + " <" + email + "> - " + message);
        // redirect with success flag
        return "redirect:/contact?success";
    }
}

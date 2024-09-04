package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.Messagetype;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String name(Model model) {
        System.out.println("Home pagae handler");
        // sending data to view
        model.addAttribute("name", "Ajay kumar");
        model.addAttribute("youtubechannel", "learn code with durgesh");
        model.addAttribute("website", "https://www.google.com/");

        return "home";
    }

    // about rout
    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("about page loading");
        return "about";
    }

    // services rout
    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("Services page loading");
        return "services";
    }

    // contact page
    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    // this is showing loging page
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    // registration page
    @GetMapping("/register")
    public String resister(Model model) {
        UserForm userForm = new UserForm();

        model.addAttribute("userForm", userForm);
        // we can insert default data
        return "register";
    }

    // processing register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println(userForm);

        // validate
        if (rBindingResult.hasErrors()) {
            return "register";
        }

        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://static.wikia.nocookie.net/p__/images/5/5f/TotK_Link_Artwork_2.png/revision/latest?cb=20230418021839&path-prefix=protagonist")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic(
                "https://static.wikia.nocookie.net/p__/images/5/5f/TotK_Link_Artwork_2.png/revision/latest?cb=20230418021839&path-prefix=protagonist");

        User saveUser = userService.saveUser(user);

        System.out.println("User saved");

        // message " registrayion successfull"

        Message message = Message.builder().content("registration Successfull").type(Messagetype.blue).build();

        session.setAttribute("message", message);

        // redirect login page
        return "redirect:/register";

        // fetch form data
    }

}

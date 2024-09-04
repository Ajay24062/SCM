package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.Messagetype;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactControl {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactControl.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService  imageService;
    @Autowired
    private UserService userService;

    // add contact page : handler
    @RequestMapping("/add")
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        // contactForm.setFavorite(true);

        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String SaveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession httpSession) {

        // process form data

        // validate
        if (result.hasErrors()) {
            result.getAllErrors().forEach(errors -> logger.info(errors.toString()));
            httpSession.setAttribute("message",
                    Message.builder().content("Please correct the following errors").type(Messagetype.red).build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedinUser(authentication);
        // form-->contact
        User user = userService.getUserByEmail(username);
        // image process

//upload process

String filename=UUID.randomUUID().toString();

String fileURL=imageService.uploadimage(contactForm.getContactImage(),filename);

        logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setPicture(fileURL);
contact.setCloudinaryImagePublicId(filename);

         contactService.save(contact);

        System.out.println(contactForm);

        httpSession.setAttribute("message",
                Message.builder().content("You have successfully added a new contact").type(Messagetype.green).build());

        return "redirect:/user/contacts/add";

    }

}

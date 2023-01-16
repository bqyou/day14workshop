package day14.workshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import day14.workshop.model.Contact;
import day14.workshop.service.ContactsRedis;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AddressBookController {

    @Autowired
    private ContactsRedis ctcRedisSvc;

    @GetMapping(path = "/")
    public String contactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @PostMapping(path = "/contact")
    public String processForm(@Valid Contact contact, BindingResult bResult,
            Model model, HttpServletResponse response) {
        if (bResult.hasErrors()) {
            return "contact";
        }
        ctcRedisSvc.save(contact);
        model.addAttribute("contact", contact);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "showContact";
    }

    @GetMapping(path = "/contact")
    public String getAllContact(Model model,
            @RequestParam(name = "startIndex") Integer startIndex) {
        List<Contact> allContacts = ctcRedisSvc.findAll(startIndex);
        model.addAttribute("contactList", allContacts);
        return "listContact";
    }

    @GetMapping(path = "/contact/{contactId}")
    public String getContactById(Model model, @PathVariable(value = "contactId") String id) {
        Contact contact = ctcRedisSvc.findById(id);
        model.addAttribute("contact", contact);
        return "showContact";
    }
}

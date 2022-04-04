package ru.kholodkova.address_book.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kholodkova.address_book.dao.ContactsDao;
import ru.kholodkova.address_book.models.Contact;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactsController {
    private ContactsDao contactsDao;

    @Autowired
    public ContactsController(ContactsDao contactsDao) {
        this.contactsDao = contactsDao;
    }

    @GetMapping
    public String getAllContacts(Model model) {
//        char[] alfa = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'G', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        model.addAttribute("contacts", contactsDao.getAllContacts());
//        model.addAttribute("alfa", alfa);
        return "contacts/all_contacts";
    }

    @GetMapping("/{id}")
    public String getContact(@PathVariable("id") int id, Model model) {
        model.addAttribute("contact", contactsDao.getContact(id));
        return "contacts/contact";
    }

    @GetMapping("/new")
    public String newContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "contacts/new";
    }

    @PostMapping()
    public String addContact(@ModelAttribute("contact") @Valid Contact contact, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "contacts/new";
        }
        contactsDao.addContact(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}/edit")
    public String editContact(@PathVariable("id") int id, Model model) {
        Contact contact = contactsDao.getContact(id);
        model.addAttribute("contact", contact);
        return "contacts/edit";
    }

    @PatchMapping("/{id}")
    public String updateContact(@PathVariable("id") int id, @ModelAttribute("contact") @Valid Contact contact,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "contacts/edit";
        }
        contactsDao.updateContact(id, contact);
        return "redirect:/contacts";
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable("id") int id) {
        contactsDao.deleteContact(id);
        return "redirect:/contacts";
    }

    @GetMapping("/result")
    public String searchContacts(@RequestParam("name") String name, Model model) {
        List<Contact> result = contactsDao.findContact(name);
        model.addAttribute("result", result);
        return "contacts/result";
    }

    @GetMapping("/recent")
    public String getRecentContacts(Model model) {
        List<Contact> recentContacts = contactsDao.getRecentContacts();
        model.addAttribute("contacts", recentContacts);
        return "contacts/recent";
    }

    @GetMapping("/popular")
    public String getPopularContacts(Model model) {
        List<Contact> popContacts = contactsDao.getPopularContacts();
        model.addAttribute("contacts", popContacts);
        return "contacts/popular";
    }

    @PostMapping("/{id}")
    public String addToFav(@PathVariable("id") int id) {
        contactsDao.addToFav(id);
        return "redirect:/contacts";
    }

    @GetMapping("/favourite")
    public String getFavouriteContacts(Model model) {
        List<Contact> favContacts = contactsDao.getFavoriteContacts();
        model.addAttribute("contacts", favContacts);
        return "contacts/favourite";
    }
}

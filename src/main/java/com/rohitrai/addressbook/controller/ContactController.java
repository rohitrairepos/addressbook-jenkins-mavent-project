package com.rohitrai.addressbook.controller;

import com.rohitrai.addressbook.model.Contact;
import com.rohitrai.addressbook.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getAll(@RequestParam(required = false) String search) {
        return search == null ? contactService.findAll() : contactService.search(search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getById(@PathVariable Long id) {
        Contact contact = contactService.findById(id);
        return contact == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(contact));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> update(@PathVariable Long id, @Valid @RequestBody Contact contact) {
        Contact updated = contactService.update(id, contact);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return contactService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

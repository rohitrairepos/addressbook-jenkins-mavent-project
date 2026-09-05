package com.rohitrai.addressbook;

import com.rohitrai.addressbook.model.Contact;
import com.rohitrai.addressbook.service.ContactService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    @Test
    void shouldReturnSeedContacts() {
        ContactService service = new ContactService();

        List<Contact> contacts = service.findAll();

        assertEquals(3, contacts.size());
        assertTrue(contacts.stream().anyMatch(c -> "Rohit".equals(c.getFirstName())));
    }

    @Test
    void shouldCreateAndFindContact() {
        ContactService service = new ContactService();
        Contact created = service.create(new Contact(null, "Alice", "Brown", "alice@example.com", "123", "Acme"));

        assertNotNull(created.getId());
        assertEquals("Alice", service.findById(created.getId()).getFirstName());
    }

    @Test
    void shouldSearchContacts() {
        ContactService service = new ContactService();

        List<Contact> results = service.search("example.com");

        assertEquals(3, results.size());
    }

    @Test
    void shouldDeleteContact() {
        ContactService service = new ContactService();

        assertTrue(service.delete(1L));
        assertNull(service.findById(1L));
    }
}

package com.rohitrai.addressbook.service;

import com.rohitrai.addressbook.model.Contact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContactService {

    private final AtomicLong sequence = new AtomicLong(3);
    private final List<Contact> contacts = new ArrayList<>();

    public ContactService() {
        contacts.add(new Contact(1L, "Rohit", "Rai", "rohit@example.com", "+91-9000000001", "Example Corp"));
        contacts.add(new Contact(2L, "Jane", "Doe", "jane@example.com", "+1-555-0100", "Acme Inc"));
        contacts.add(new Contact(3L, "John", "Smith", "john@example.com", "+1-555-0101", "Globex"));
    }

    public synchronized List<Contact> findAll() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public synchronized Contact findById(Long id) {
        return contacts.stream()
                .filter(contact -> contact.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized Contact create(Contact contact) {
        contact.setId(sequence.incrementAndGet());
        contacts.add(contact);
        return contact;
    }

    public synchronized Contact update(Long id, Contact updated) {
        Contact existing = findById(id);
        if (existing == null) {
            return null;
        }
        updated.setId(id);
        int index = contacts.indexOf(existing);
        contacts.set(index, updated);
        return updated;
    }

    public synchronized boolean delete(Long id) {
        return contacts.removeIf(contact -> contact.getId().equals(id));
    }

    public synchronized List<Contact> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }

        String value = query.trim().toLowerCase();
        return contacts.stream()
                .filter(contact -> contains(contact.getFirstName(), value)
                        || contains(contact.getLastName(), value)
                        || contains(contact.getEmail(), value)
                        || contains(contact.getCompany(), value)
                        || contains(contact.getPhone(), value))
                .sorted(Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private boolean contains(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }
}

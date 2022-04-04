package ru.kholodkova.address_book.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kholodkova.address_book.models.Contact;

import java.util.*;

@Component
public class ContactsDao {
    private JdbcTemplate jdbcTemplate;
    private static Map<Character, List<Contact>> alphabetList;

    static{
        alphabetList = new HashMap<>();
        for(int i = 65; i <= 122; i++) {
            if(i==91) {
                i=97;
            }
            alphabetList.put((char)i, new ArrayList<>());
        }
    }

    @Autowired
    public ContactsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ContactsDao(){}

    public Map<Character, List<Contact>> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts = jdbcTemplate.query("SELECT * FROM contacts", new BeanPropertyRowMapper<>(Contact.class));
        Collections.sort(contacts);
//        for(Map.Entry entry : alphabetList.entrySet()) {
//            for(Contact contact : contacts) {
//                List<Contact> sortedContacts = new ArrayList<>();
//                if(contact.getName().startsWith((String) entry.getKey())) {
//                   sortedContacts.add(contact);
//                }
//            }
//        }
        Iterator<Map.Entry<Character, List<Contact>>> iterator = alphabetList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, List<Contact>> entry = iterator.next();
            entry.setValue(new ArrayList<Contact>());
            for(Contact contact : contacts) {
                if(contact.getName().startsWith(String.valueOf(entry.getKey()))) {
                    entry.getValue().add(contact);
                }
            }
        }
        for(Map.Entry entry : alphabetList.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(Arrays.deepToString(new Object[]{entry.getValue()}));
        }

        return alphabetList;
    }

    public List<Contact> getRecentContacts() {
        List<Contact> recentContacts = new ArrayList<>();
        recentContacts = jdbcTemplate.query("SELECT * FROM contacts ORDER BY creation_time DESC LIMIT 5", new BeanPropertyRowMapper<>(Contact.class));
        return recentContacts;
    }

    public Contact getContact(int id) {
        Contact contact = jdbcTemplate.query("SELECT * FROM contacts WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Contact.class))
                .stream().findAny().orElse(null);
        int usage = contact.getUsage();
        usage = usage + 1;
        jdbcTemplate.update("UPDATE contacts SET usage=? WHERE id=?", usage, id);
        return contact;
    }

    public List<Contact> getPopularContacts() {
        List<Contact> popularContacts = jdbcTemplate.query("SELECT * FROM contacts ORDER BY usage DESC LIMIT 5", new BeanPropertyRowMapper<>(Contact.class));
        return popularContacts;
    }

    public List<Contact> getFavoriteContacts() {
        List<Contact> favoriteContacts = jdbcTemplate.query("SELECT * FROM contacts WHERE isFav = true", new BeanPropertyRowMapper<>(Contact.class));
        for (Contact contact : favoriteContacts) {
            System.out.println(contact);
        }
        return favoriteContacts;
    }

    public void addToFav(int id) {
        boolean fav = true;
        jdbcTemplate.update("UPDATE contacts SET isFav=? WHERE id=?", fav, id);
    }

    public void addContact(Contact contact) {
        jdbcTemplate.update("INSERT INTO contacts (name, phone, creation_time) VALUES (?, ?, ?)", contact.getName(), contact.getPhone(), contact.getCreationTime());
    }

    public void updateContact(int id, Contact contact) {
        jdbcTemplate.update("UPDATE contacts SET name=?, phone=? WHERE id=?", contact.getName(), contact.getPhone(), id);
    }

    public void deleteContact(int id) {
        jdbcTemplate.update("DELETE FROM contacts WHERE id=?", id);
    }

    public List<Contact> findContact(String sampling){
        String sqlSampling = "%"+sampling+"%";
        List<Contact> contacts = new ArrayList<>();
        contacts = jdbcTemplate.query("SELECT * FROM contacts WHERE name LIKE ?",
                new Object[]{sqlSampling}, new BeanPropertyRowMapper<>(Contact.class));
        return contacts;
    }

    public static void main(String[] args) {
//        new ContactsDao().getAllContacts();
//        for(Map.Entry entry : alphabetList.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(Arrays.deepToString(new Object[]{entry.getValue()}));
//        }
    }
}

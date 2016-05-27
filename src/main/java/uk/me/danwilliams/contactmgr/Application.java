package uk.me.danwilliams.contactmgr;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import uk.me.danwilliams.contactmgr.model.Contact;
import uk.me.danwilliams.contactmgr.model.Contact.ContactBuilder;

import java.util.List;


/**
 * Created by Dan on 26/05/2016.
 */
public class Application {
    //hold a reusable reference to a sessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        //Create a standard service registry object
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Dan", "Williams")
                .withEmail("Dan@gmail.com")
                .withPhone(1234567890L)
                .build();

        int id = save(contact);

        //Display list of contacts before the update
        System.out.printf("%n%n Before update %n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //get the persisted contact
        Contact c = findContactById(id);

        //Update the contact
        c.setFirstName("Ashley");

        //persist the changes
        System.out.printf("%n%n Updating... %n%n");
        update(c);
        System.out.printf("%n%n Update  complete! %n%n");

        //Display a list of contacts after the update
        System.out.printf("%n%n After update %n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //delete contact that was jsut created
        System.out.printf("%n%n Deleting... %n%n");
        delete(c);
        System.out.printf("%n%n Delete Complete! %n%n");


        System.out.printf("%n%n After Delete %n%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id){
        //open a session
        Session session = sessionFactory.openSession();

        //retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        //close the session
        session.close();

        //return the object
        return contact;
    }

    public static void update(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to update the contact
        session.update(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    public static void delete(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to update the contact
        session.delete(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){
        //open a session
        Session session = sessionFactory.openSession();

        //Create criteria
        Criteria criteria = session.createCriteria(Contact.class);

        //Get a list of contact objects according to the criteria object
        List<Contact> contacts = criteria.list();

        //close the session
        session.close();

        return contacts;
    }

    private static int save(Contact contact){
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //use the session to save the contact
        int id = (int)session.save(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();

        return id;
    }


}

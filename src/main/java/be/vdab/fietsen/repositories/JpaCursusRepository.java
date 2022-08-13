package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Cursus;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public class JpaCursusRepository implements CursusRepository{
    // EntityManager is een interface uit JPA. Je doet veel handelingen met een EntityManager:
    //  • Entities als nieuwe records toevoegen aan de database.
    //  • Records lezen als entities.
    //  • Records wijzigen die bij entities horen.
    //  • Records verwijderen die bij entities horen.
    //  • Transacties te beheren.
    //Spring Boot maakt een EntityManager als een bean, zodra je project de JPA dependency bevat.
    private final EntityManager manager;

    public JpaCursusRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Cursus> findById(UUID id) {
        // De method find zoekt een entity op de primary key.
        // De 1° parameter is het type van de entity.
        // De 2° parameter is de primary key waarde.
        // find geeft de entity terug als hij die vindt in de database.
        // find geeft null terug als hij de entity niet vindt.
        // Een method die een waarde niet vindt, geeft best een Optional.
        // JPA werd echter gemaakt voor Optional bestond in Java.
        // Je maakt je eigen method (findById) wel “modern”. Je geeft een Optional terug.
        return Optional.ofNullable(manager.find(Cursus.class, id));
    }

    @Override
    public void create(Cursus cursus) {
        // De parameter cursus bevat een GroepsCursus object of een IndividueleCursus object.
        // Bij een GroepsCursus object vult JPA in het nieuwe record de kolom soort met een G.
        // JPA doet dit op basis van de regel @DiscriminatorValue("G") in de class GroepsCursus.
        // Bij een IndividueleCursus object vult JPA de kolom soort met een I.

        // Je geeft aan persist de toe te voegen entity mee als parameter.
        // De method stuurt intern een SQL-insert statement naar de database.
        manager.persist(cursus);
    }
}

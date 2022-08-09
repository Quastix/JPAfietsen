package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Docent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
class JpaDocentRepository implements DocentRepository {
    private final EntityManager manager;

    // Je injecteert de EntityManager bean die Spring voor je maakt.
    JpaDocentRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Docent> findById(long id) {
        // De method find zoekt een entity op de primary key.
        // De 1° parameter is het type van de entity.
        // De 2° parameter is de primary key waarde.
        // find geeft de entity terug als hij die vindt in de database.
        // find geeft null terug als hij de entity niet vindt.
        // Een method die een waarde niet vindt, geeft best een Optional.
        // JPA werd echter gemaakt voor Optional bestond in Java.
        // Je maakt je eigen method (findById) wel “modern”. Je geeft een Optional terug.
        return Optional.ofNullable(manager.find(Docent.class, id));
    }

    @Override
    public void create(Docent docent) {
        manager.persist(docent);
    }
}

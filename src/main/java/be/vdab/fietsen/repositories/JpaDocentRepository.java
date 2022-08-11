package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.projections.AantalDocentenPerWedde;
import be.vdab.fietsen.projections.IdEnEmailAdres;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
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
    @Override
    public void delete(long id) {
        //Je verwijdert een entity in twee stappen: je leest eerst de te verwijderen entity.
        findById(id)
                // Je voert de method remove uit. Je geeft de entity mee als parameter.
                .ifPresent(docent -> manager.remove(docent));
    }
    @Override
    public List<Docent> findAll(){
        return manager.createQuery("select d from Docent d order by d.wedde",
                Docent.class).getResultList();
    }
    @Override
    public List<Docent> findByWeddeBetween(BigDecimal van, BigDecimal tot){
        return manager.createNamedQuery("Docent.findByWeddeBetween", Docent.class)
                .setParameter("van",
                        // Je duidt hier de waarde aan die je in die parameter invult.
                        // Je gebruikt de waarde die je binnenkrijgt in de method parameter van.
                        van)
                .setParameter("tot", tot)
                .getResultList();
        /*Vervangen door Named Query zie class Docent
        return manager.createQuery(
                "select d from Docent d where d.wedde between :van and :tot", Docent.class)
                // Je vult een parameter in de JPQL query met een waarde.
                // Je duidt hier aan welke parameter in de JPQL query je wil invullen: van (je typt : niet).
                .setParameter("van",
                        // Je duidt hier de waarde aan die je in die parameter invult.
                        // Je gebruikt de waarde die je binnenkrijgt in de method parameter van.
                        van)
                .setParameter("tot", tot)
                .getResultList();*/
    }
    @Override
    public List<IdEnEmailAdres> findIdsEnEmailAdressen(){
        return manager.createQuery(
                "select new be.vdab.fietsen.projections.IdEnEmailAdres(d.id, d.emailAdres)" +
                        "from Docent d", IdEnEmailAdres.class).getResultList();
    }
    @Override
    public BigDecimal findGrootsteWedde(){
        return manager.createQuery("select max(d.wedde) from Docent d", BigDecimal.class)
                .getSingleResult();
    }
    @Override
    public List<AantalDocentenPerWedde> findAantalDocentenPerWedde(){
        return manager.createQuery(
                "select new be.vdab.fietsen.projections.AantalDocentenPerWedde(" +
                        "d.wedde, count(d)) from Docent d group by d.wedde",
                AantalDocentenPerWedde.class)
                .getResultList();
    }
}

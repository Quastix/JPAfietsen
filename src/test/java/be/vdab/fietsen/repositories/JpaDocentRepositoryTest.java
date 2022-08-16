package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Adres;
import be.vdab.fietsen.domain.Campus;
import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.domain.Geslacht;
import be.vdab.fietsen.projections.AantalDocentenPerWedde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertCampus.sql", "/insertDocent.sql"})
@Import(JpaDocentRepository.class)
class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String DOCENTEN = "docenten";
    private static final String DOCENTEN_BIJNAMEN = "docentenbijnamen";
    private Docent docent;
    private Campus campus;
    private final JpaDocentRepository repository;
    private final EntityManager manager;


    public JpaDocentRepositoryTest(JpaDocentRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void beforeEach() {
        campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus);
    }

    private long idVanTestMan() {
        return jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'", Long.class);
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestMan())).hasValueSatisfying(docent -> assertThat(docent.getVoornaam()).isEqualTo("testM"));
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    private long idVanTestVrouw() {
        return jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testV'", Long.class);
    }

    @Test
    void man() {
        assertThat(repository.findById(idVanTestMan())).hasValueSatisfying(docent -> assertThat(docent.getGeslacht()).isEqualTo(Geslacht.MAN));
    }

    @Test
    void vrouw() {
        assertThat(repository.findById(idVanTestVrouw())).hasValueSatisfying(docent -> assertThat(docent.getGeslacht()).isEqualTo(Geslacht.VROUW));
    }

    @Test
    void create() {
        manager.persist(campus);
        repository.create(docent);
        // JPA vult de variabele id met het getal in de kolom id in het nieuwe record.
        assertThat(docent.getId()).isPositive();
        assertThat(countRowsInTableWhere(DOCENTEN,
                "id = " + docent.getId() + " and campusId = " + campus.getId()));
    }

    @Test
    void delete() {
        var id = idVanTestMan();
        repository.delete(id);
        // Je verwijderde de docent op de vorige regel.
        // De EntityManager doet verwijderingen niet direct.
        // Hij spaart ze op tot juist voor de commit van de transactie.
        // Hij stuurt dan alle opgespaarde verwijderingen in één keer naar de database,
        // via JDBC batch updates. Dit verhoogt de performantie.
        // JPA moet in deze test de docent direct verwijderen.
        // Enkel zo kan je de correcte werking van je delete method testen.
        // Je voert flush uit. JPA voert de verwijderingen dan direct uit.
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "id = " + id)).isZero();
    }

    @Test
    void findAll() {
        assertThat(repository.findAll())
                .hasSize(countRowsInTable(DOCENTEN))
                .extracting(Docent::getWedde)
                .isSorted();
    }

    @Test
    void findByWeddeBetween() {
        var duizend = BigDecimal.valueOf(1_000);
        var tweeduizend = BigDecimal.valueOf(2_000);
        var docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        assertThat(docenten).hasSize(countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000"))
                .allSatisfy(
                        docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend));
    }

    @Test
    void findIdsEnEmailAdressen() {
        assertThat(repository.findIdsEnEmailAdressen())
                .hasSize(countRowsInTable(DOCENTEN));
    }

    @Test
    void findGrootsteWedde() {
        assertThat(repository.findGrootsteWedde()).isEqualByComparingTo(
                jdbcTemplate.queryForObject("select max(wedde) from docenten", BigDecimal.class));
    }

    @Test
    void findAantalDocentenPerWedde() {
        var duizend = BigDecimal.valueOf(1_000);
        assertThat(repository.findAantalDocentenPerWedde())
                .hasSize(jdbcTemplate.queryForObject(
                        // Het resultaat moet evenveel items bevatten als het aantal unieke weddes.
                        "select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(
                        // Je houdt de items over met de wedde 1000.
                        aantalPerWedde -> aantalPerWedde.wedde().compareTo(duizend) == 0)
                // singleElement controleert dat er maar één item is en geeft je dat item.
                .singleElement()
                // Je haalt uit dit item het aantal docenten.
                .extracting(AantalDocentenPerWedde::aantal)
                // Dit moet gelijk zijn aan het aantal docenten met een wedde 1000.
                .isEqualTo((long) super.countRowsInTableWhere(DOCENTEN, "wedde = 1000"));
    }

    @Test
    void algemeneOpslag() {
        assertThat(repository.algemeneOpslag(BigDecimal.TEN))
                // Er moeten evenveel docenten opslag gekregen hebben als er docenten zijn.
                .isEqualTo(countRowsInTable(DOCENTEN));
        // De docent met de voornaam testM had een wedde van 1000. Dit moet nu 1100 zijn.
        assertThat(countRowsInTableWhere(DOCENTEN,
                "wedde = 1100 and id = " + idVanTestMan())).isOne();
    }

    @Test
    void bijnamenLezen() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(docent ->
                        assertThat(docent.getBijnamen()).containsOnly("test"));
    }

    @Test
    void bijnaamToevoegen() {
        manager.persist(campus);
        repository.create(docent);
        docent.addBijnaam("test");
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN_BIJNAMEN,
                "bijnaam = 'test' and docentId = " + docent.getId())).isOne();
    }
    @Test void campusLazyLoaded() {
        // JPA leest enkel een record uit de table docenten.
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        // Je spreekt de campus van de docent aan.
                        // JPA leest nu pas het record uit de table campussen.
                        docent -> assertThat(docent.getCampus().getNaam()).isEqualTo("test"));
    }
}

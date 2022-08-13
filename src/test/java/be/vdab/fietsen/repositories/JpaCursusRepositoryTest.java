package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.GroepsCursus;
import be.vdab.fietsen.domain.IndividueleCursus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import(JpaCursusRepository.class)
@Sql("/insertCursus.sql")
public class JpaCursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String GROEPS_CURSUSSEN = "groepscursussen";
    private static final String INDIVIDUELE_CURSUSSEN = "individuelecursussen";
    private final EntityManager manager;
    private static final LocalDate EEN_DATUM = LocalDate.of(2019, 1, 1);
    private final JpaCursusRepository repository;

    public JpaCursusRepositoryTest(EntityManager manager, JpaCursusRepository repository) {
        this.manager = manager;
        this.repository = repository;
    }

    private UUID idVanTestGroepsCursus() {
        return jdbcTemplate.queryForObject("select bin_to_uuid(id) from groepscursussen where naam = 'testGroep'", UUID.class);
    }

    private UUID idVanTestIndividueleCursus() {
        return jdbcTemplate.queryForObject("select bin_to_uuid(id) from individuelecursussen where naam='testIndividueel'", UUID.class);
    }

    @Test
    void findGroepCursusById() {
        assertThat(repository.findById(idVanTestGroepsCursus()))
                .containsInstanceOf(GroepsCursus.class)
                .hasValueSatisfying(
                        cursus -> assertThat(cursus.getNaam()).isEqualTo("testGroep"));
    }

    @Test
    void findIndividueleCursusById() {
        assertThat(repository.findById(idVanTestIndividueleCursus()))
                .containsInstanceOf(IndividueleCursus.class)
                .hasValueSatisfying(
                        cursus -> assertThat(cursus.getNaam()).isEqualTo("testIndividueel"));
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void createGroepsCursus() {
        var cursus = new GroepsCursus("testGroep2", EEN_DATUM, EEN_DATUM);
        repository.create(cursus);
        // Je voegde op de vorige regel een cursus met een UUID primary key toe.
        // De EntityManager voert de toevoegingen niet direct uit.
        // Hij spaart ze op tot juist voor de commit van de transactie.
        // Hij stuurt dan alle opgespaarde toevoegingen in één keer naar de database,
        // via JDBC batch updates. Dit verhoogt de performantie.
        // In deze test is het wel nodig dat JPA de cursus onmiddellijk toevoegt.
        // Enkel zo kan je de correcte werking van onze create method testen.
        // e voert flush uit. Die voert de gevraagde toevoegingen direct uitvoert.
        // Als je een autonumber primary key hebt, kan JPA geen JDBC batch updates gebruiken,
        // omdat je daarbij de gegenereerde primary key waarden niet kan ophalen.
        // JPA voegt dan een record toe zodra je de persist method uitvoert op de EntityManager.
        manager.flush();
        assertThat(countRowsInTableWhere(GROEPS_CURSUSSEN, "id = uuid_to_bin('" + cursus.getId() + "')")).isOne();
    }

    @Test
    void createIndividueleCursus() {
        var cursus = new IndividueleCursus("testIndividueel2", 7);
        repository.create(cursus);
        manager.flush();
        assertThat(countRowsInTableWhere(INDIVIDUELE_CURSUSSEN, "id = uuid_to_bin('" + cursus.getId() + "')")).isOne();
    }
}
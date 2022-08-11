package be.vdab.fietsen.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

// Je maakt een integration test. Je test daarbij DefaultDocentService en zijn samenwerking met JpaDocentRepository.
// Je kan zo testen of na de opslag de docent n de database gewijzigd is.
// Gezien de integration test de database aanspreekt is hij trager dan de unit test.
@DataJpaTest(showSql = false)
@Import(DefaultDocentService.class)
// Je wil DefaultDocentService testen. Die heeft een JpaDocentRepository bean nodig.
// Je kan die bean niet laden met @Import: JpaDocentRepository heeft package visibility.
// Hij is niet zichtbaar in de huidige package.
// Je laadt de bean daarom met @ComponentScan.
// Value bevat de package van de class van de bean.
// ResourcePattern bevat de naam van de class van de bean.
@ComponentScan(value = "be.vdab.fietsen.repositories",
        resourcePattern = "JpaDocentRepository.class")
@Sql("/insertDocent.sql")
class DefaultDocentServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String DOCENTEN = "docenten";
    private final DefaultDocentService service;
    private final EntityManager manager;

    public DefaultDocentServiceIntegrationTest(DefaultDocentService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }

    private long idVanTestMan() {
        return jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'", Long.class);
    }

    @Test
    void opslag() {
        var id = idVanTestMan();
        service.opslag(id, BigDecimal.TEN);
        // Je wijzigde de docent op de vorige regel.
        // De EntityManager doet wijzigingen niet direct.
        // Hij spaart ze op tot juist voor de commit van de transactie.
        // Hij stuurt dan alle opgespaarde wijzigingen in één keer naar de database, via JDBC-batch updates.
        // Dit verhoogt de performantie. In deze test is het wel nodig dat JPA de docent direct wijzigt.
        // Je kan enkel zo de correcte werking van je opslag method testen. Je voert flush uit.
        // JPA doet de wijziging dan direct.
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "wedde = 1100 and id = " + id)).isOne();
    }
}

package be.vdab.fietsen.services;

import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.domain.Geslacht;
import be.vdab.fietsen.exceptions.DocentNietGevondenException;
import be.vdab.fietsen.repositories.DocentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;


// Je maakt een unit test voor DefaultDocentService.
//Je wil in een unit test enkel de code in DefaultDocentService testen.
// Deze class heeft echter een dependency op DocentRepository, geïmplementeerd door JpaDocentRepository.
// Methods van DocentRepository roepen methods van JpaDocentRepository op.
// Je wil tijdens de DefaultDocentService test de code in JpaDocentRepository niet uitvoeren:
//      • Misschien is JpaDocentRepository nog niet geschreven.
//      • Misschien bevat JpaDocentRepository nog fouten.
//Je maakt daarom in de test een mock die DocentRepository implementeert.
// Je injecteert deze mock in het DefaultDocentService object dat je test.
// Als je in je tests methods van DefaultDocentService oproept, roepen die zelf de mock op.
//Extra voordeel: de mock spreekt geen database aan. JUnit voert de test dus zeer snel uit.

@ExtendWith(MockitoExtension.class)
class DefaultDocentServiceTest {
    private DefaultDocentService service;
    @Mock
    // Mockito maakt een mock. Die implementeert de interface DocentRepository.
    private DocentRepository repository;
    private Docent docent;

    @BeforeEach
    void beforeEach() {
        // Je injecteert de mock in je DefaultDocentService.
        service = new DefaultDocentService(repository);
        docent = new Docent("test", "test", BigDecimal.valueOf(100), "test@test.be", Geslacht.MAN);
    }

    @Test
    void opslag() {
        // Je traint de mock: bij een oproep van findById(1) geeft die het object in docent.
        when(repository.findById(1)).thenReturn(Optional.of(docent));
        // Je roept de method opslag op. Die roept zelf de method findById(1) op van de mock.
        service.opslag(1, BigDecimal.TEN);
        assertThat(docent.getWedde()).isEqualByComparingTo("110");
        // Je controleert of DefaultDocentService op de mock findById(1) heeft opgeroepen.
        verify(repository).findById(1);
    }

    @Test
    void opslagVoorOnbestaandeDocent() {
        assertThatExceptionOfType(DocentNietGevondenException.class).isThrownBy(()
                -> service.opslag(-1, BigDecimal.TEN));
        // Je probeert een onbestaande docent (met id -1) opslag te geven.
        // Je trainde de mock niet. findById geeft dan een “lege” Optional terug.
        // DefaultDocentService is correct als die een DocentNietGevondenException werpt.
        verify(repository).findById(-1);
    }
}

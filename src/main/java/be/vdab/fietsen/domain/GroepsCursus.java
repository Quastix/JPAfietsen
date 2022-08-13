package be.vdab.fietsen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

// Het is een Entity class.
// dwz: Heeft een private variabele die de entity uniek identificeert. Bij Klant is dit de variabele id.
@Entity
// Duidt de bijbehorende database table aan
// Je mag dit weglaten als de tabelnaam gelijk is aan de class naam.
@Table(name ="groepscursussen")
public class GroepsCursus extends Cursus{
    private LocalDate van;
    private LocalDate tot;

    public GroepsCursus(String naam, LocalDate van, LocalDate tot) {
        super(naam);
        this.van = van;
        this.tot = tot;
    }
    // JPA heeft een default constructor nodig voor zijn interne werking.
    // Voor JPA volstaat het dat de default constructor de zichtbaarheid protected heeft.
    // Door deze constructor niet public te maken is hij niet zichtbaar in de rest van je code.
    // Je kan hem niet “per ongeluk” oproepen om een docent te maken waarbij je verkeerdelijk
    // geen enkele informatie (voornaam, familienaam, …) meegeeft.
    protected  GroepsCursus(){}

    public LocalDate getVan() {
        return van;
    }

    public LocalDate getTot() {
        return tot;
    }
}

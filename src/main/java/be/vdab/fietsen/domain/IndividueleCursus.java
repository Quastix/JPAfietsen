package be.vdab.fietsen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

// Het is een Entity class.
// dwz: Heeft een private variabele die de entity uniek identificeert. Bij Klant is dit de variabele id.
@Entity
// Duidt de bijbehorende database table aan
// Je mag dit weglaten als de tabelnaam gelijk is aan de class naam.
@Table(name ="individuelecursussen")
public class IndividueleCursus extends Cursus{
    private int duurtijd;

    public IndividueleCursus(String naam, int duurtijd) {
        super(naam);
        this.duurtijd = duurtijd;
    }

    protected IndividueleCursus(){}

    public int getDuurtijd() {
        return duurtijd;
    }
}

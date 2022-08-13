package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.util.UUID;

// Het is een Entity class.
// dwz: Heeft een private variabele die de entity uniek identificeert. Bij Klant is dit de variabele id.
@Entity
//Je duidt “table per concrete class” aan met TABLE_PER_CLASS.
@Inheritance(strategy =  InheritanceType.TABLE_PER_CLASS)
// Duidt de bijbehorende database table aan
// Je mag dit weglaten als de tabelnaam gelijk is aan de class naam.
@Table(name = "cursussen")
public abstract class Cursus {
    // Staat voor de private variabele die hoort bij de primary key kolom.
    @Id
    @Column(columnDefinition = "binary(16)")
    private UUID id;
    private String naam;

    // Je maakt geen parameter voor de private variabele id.
    // Deze parameter is niet nodig. Je moet geen id meegeven als je een nieuw Docent object maakt.
    // De database bepaalt de id bij het toevoegen van het record. JPA vult hiermee de private variabele id.
    public Cursus(String naam) {
        id = UUID.randomUUID();
        this.naam = naam;
    }
    // JPA heeft een default constructor nodig voor zijn interne werking.
    // Voor JPA volstaat het dat de default constructor de zichtbaarheid protected heeft.
    // Door deze constructor niet public te maken is hij niet zichtbaar in de rest van je code.
    // Je kan hem niet “per ongeluk” oproepen om een docent te maken waarbij je verkeerdelijk
    // geen enkele informatie (voornaam, familienaam, …) meegeeft.
    protected Cursus() {
    }
    public UUID getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }
}

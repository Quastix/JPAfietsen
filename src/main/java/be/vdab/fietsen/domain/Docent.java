package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Het is een entity class
// dwz: Heeft een private variabele die de entity uniek identificeert. Bij Klant is dit de variabele id.
@Entity
// Duidt de bijbehorende database table aan
// Je mag dit weglaten als de tabelnaam gelijk is aan de class naam.
@Table(name = "docenten")
public class Docent {

    // Staat voor de private variabele die hoort bij de primary key kolom.
    @Id
    // Je plaatst de parameter strategy op IDENTITY bij een kolom met autonummering.
    // JPA vult, na het toevoegen van een record, de variabele id met het getal in de kolom id.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

    // Je maakt geen parameter voor de private variabele id.
    // Deze parameter is niet nodig. Je moet geen id meegeven als je een nieuw Docent object maakt.
    // De database bepaalt de id bij het toevoegen van het record. JPA vult hiermee de private variabele id.
    public Docent(String voornaam, String familienaam, BigDecimal wedde,
                  String emailAdres, Geslacht geslacht) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
    }
    // JPA heeft een default constructor nodig voor zijn interne werking.
    // Voor JPA volstaat het dat de default constructor de zichtbaarheid protected heeft.
    // Door deze constructor niet public te maken is hij niet zichtbaar in de rest van je code.
    // Je kan hem niet “per ongeluk” oproepen om een docent te maken waarbij je verkeerdelijk
    // geen enkele informatie (voornaam, familienaam, …) meegeeft.
    protected Docent() {
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public BigDecimal getWedde() {
        return wedde;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public void opslag(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        var factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
        wedde = wedde.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}

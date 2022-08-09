package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "docenten")
public class Docent {
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

    // Voor JPA volstaat het dat de default constructor de zichtbaarheid protected heeft.
    // Door deze constructor niet public te maken is hij niet zichtbaar in de rest van je code.
    // Je kan hem niet “per ongeluk” oproepen om een docent te maken waarbij je verkeerdelijk
    // geen enkele informatie (voornaam, familienaam, …) meegeeft.
    protected Docent() {}

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
}

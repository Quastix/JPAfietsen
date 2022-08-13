package be.vdab.fietsen.domain;

import javax.persistence.*;

@Entity
@Table(name = "campussen")
public class Campus {
    // Staat voor de private variabele die hoort bij de primary key kolom.
    @Id
    // Je plaatst de parameter strategy op IDENTITY bij een kolom met autonummering.
    // JPA vult, na het toevoegen van een record, de variabele id met het getal in de kolom id.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    // @Embedded staat voor een variabele met als type een value object class.
    @Embedded
    private Adres adres;

    public Campus(String naam, Adres adres) {
        this.naam = naam;
        this.adres = adres;
    }

    protected Campus(){}

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Adres getAdres() {
        return adres;
    }
}

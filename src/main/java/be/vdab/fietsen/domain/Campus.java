package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

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
    @ElementCollection
    @CollectionTable(name = "campussentelefoonnrs", joinColumns = @JoinColumn(name = "campusId"))
    // @OrderBy definieert de volgorde waarmee JPA de value objects leest uit de database.
    // Je vermeldt de naam van één of meerdere private variabelen (gescheiden door komma)
    // waarop je wil sorteren. Je kan omgekeerd sorteren met desc na een private variabele.
    @OrderBy("fax")
    private Set<TelefoonNr> telefoonNrs;

    public Campus(String naam, Adres adres) {
        this.naam = naam;
        this.adres = adres;
        this.telefoonNrs = new LinkedHashSet<>();
    }

    protected Campus() {
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Adres getAdres() {
        return adres;
    }
    public Set<TelefoonNr> getTelefoonNrs() { return Collections.unmodifiableSet(telefoonNrs); }
}

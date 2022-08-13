package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

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

    // @ElementCollection staat voor een verzameling value objects
    // private Set<String> bijnamen;)
    @ElementCollection
    // @CollectionTable duidt de table aan die de value objects bevat.
    @CollectionTable(name = "docentenbijnamen",
            // @JoinColumn duidt een kolom in de table aan.
            // Het is de foreign key kolom die verwijst naar primary kolom in de table (docenten)
            // die hoort bij de huidige entity class (Docent).
            // Je vult met @JoinColumn de parameter joinColumns van @CollectionTable.
            joinColumns = @JoinColumn(name = "docentId"))
    // @Column duidt de kolom naam aan die hoort bij de value objects in de verzameling.
    @Column(name = "bijnaam")
    // JPA ondersteunt List, Set en Map.
    private Set<String> bijnamen;

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
        this.bijnamen = new LinkedHashSet<>();
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

    public boolean addBijnaam(String bijnaam) {
        if (bijnaam.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return bijnamen.add(bijnaam);
    }

    public boolean removeBijnaam(String bijnaam) {
        return bijnamen.remove(bijnaam);
    }

    public Set<String> getBijnamen() {
        // JPA stelt zelf geen vereisten aan een getter voor de verzameling.
        // Los van JPA raden we aan de getter van een verzameling niet met (return bijnamen;) te maken.
        // Je leest met getBijnamen de bijnamen van een Docent.
        // Als je (return bijnamen;) typt, kan je met getBijnamen per ongeluk een bijnaam
        // toevoegen aan de docent: docent.getBijnamen().add("Polle pap");
        // Je kan ook per ongeluk een bijnaam verwijderen:
        // docent.getBijnamen().remove("Polle pap");

        // Je verhindert dit met de static Collections method unmodifiableSet.
        // Je geeft een Set mee. Je krijgt een Set terug met dezelfde elementen.
        // Als je op die Set add of remove uitvoert, krijg je een UnsupportedOperationException.
        // De getter geeft zo een read-only voorstelling van de Set.
        return Collections.unmodifiableSet(bijnamen);
    }
}

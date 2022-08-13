package be.vdab.fietsen.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

//@Embeddable staat voor een value object class.
@Embeddable
// Je moet bij een value object aangeven of je JPA annotations typt bij de private variabelen
// of bij de getters. De meeste programmeurs typen de annotations bij de private variabelen.
// Je geeft dit hier aan. In deze class zijn er geen annotations bij de variabelen.
// Je typt toch de huidige regel. IntelliJ toont anders fouten bij de getters.
@Access(AccessType.FIELD)
public class Adres {
    private String straat;
    private String huisNr;
    private String postcode;
    private String gemeente;

    public Adres(String straat, String huisNr, String postcode, String gemeente) {
        this.straat = straat;
        this.huisNr = huisNr;
        this.postcode = postcode;
        this.gemeente = gemeente;
    }
    // Een value object class moet bij JPA een default constructor hebben voor zijn interne werking.
    // Voor JPA volstaat het de constructor protected te maken.
    protected Adres(){}

    public String getStraat() {
        return straat;
    }

    public String getHuisNr() {
        return huisNr;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getGemeente() {
        return gemeente;
    }
}

package be.vdab.fietsen.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class TelefoonNr {
    private String nummer;
    private boolean fax;
    private String opmerking;


    // TelefoonNr bevat geen variabele die verwijst naar het bijbehorende Campus object.
    // Op die manier is TelefoonNr herbruikbaar in andere entity classes: Klant, …
    public TelefoonNr(String nummer, boolean fax, String opmerking) {
        this.nummer = nummer;
        this.fax = fax;
        this.opmerking = opmerking;
    }

    protected TelefoonNr() {
    }

    public String getNummer() {
        return nummer;
    }

    public boolean isFax() {
        return fax;
    }

    public String getOpmerking() {
        return opmerking;
    }

    @Override
    // Campus bevat straks een Set<TelefoonNr>.
    // Die laat geen TelefoonNr objecten met hetzelfde nummer toe.
    // Je baseert daartoe de equals method op het nummer.
    public boolean equals(Object object) {
        return object instanceof TelefoonNr telefoonNr &&
                // Je maakt bij het vergelijken geen onderscheid tussen hoofd- en kleine letters.
                nummer.equalsIgnoreCase(telefoonNr.nummer);
    }

    @Override
    // Je baseert ook hashCode op het nummer.
    public int hashCode() {
        // Je maakt ook hier geen onderscheid tussen hoofd- en kleine letters.
        return nummer.toUpperCase().hashCode();
    }
}

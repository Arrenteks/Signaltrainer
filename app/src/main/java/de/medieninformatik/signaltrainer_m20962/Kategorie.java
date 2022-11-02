package de.medieninformatik.signaltrainer_m20962;

import java.io.Serializable;
/**
 * Boilerplate Klasse für die Kategorie der Signale
 * implementiert Serializable für Nutzung in der intent hashmap
 */
public class Kategorie implements Serializable {

    public static final String  KATEGORIE = "KATEGORIE";  // serialize

    public long id = -1;
    public String bez = "-";

    public Kategorie(long id, String bez){
        this.id = id;
        this.bez = bez;
    }

    public Kategorie(){}

    public String toString(){
        return bez;
    }
}

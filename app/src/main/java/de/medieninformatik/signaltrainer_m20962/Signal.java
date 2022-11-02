package de.medieninformatik.signaltrainer_m20962;

import java.io.Serializable;

/**
 * Boilerplateklasse für die Informationen aus der Datenbank
 * implementiert Serializable für die intent hashmap
 */
public class Signal implements Serializable {


    public static final String  SIGNAL = "SIGNAL";  // serialize

    public long id =-1;
    public String kategorie="-";
    public String name = "-";
    public String bedeutung = "-";
    public String beschreibung = "-";
    public String ds_dv = "-";
    public String tag_nacht = "-";
    public String url = "-";

    public Signal(long id, String kategorie, String name, String bedeutung, String beschreibung, String ds_dv, String tag_nacht, String url) {
        this.id = id;
        this.kategorie = kategorie;
        this.name = name;
        this.bedeutung = bedeutung;
        this.beschreibung = beschreibung;
        this.ds_dv = ds_dv;
        this.tag_nacht = tag_nacht;
        this.url = url;
    }

    //Default Constructor
    public Signal(){}

}

package de.medieninformatik.signaltrainer_m20962;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBSQueries {


    private static final String DATABASE_NAME = "SIGNALE.db";
    private static final int DATABASE_VERSION = 1;

    public static int aktuelleDBVersion=1;
    private final static int MAXUpdate = 1;


    public static final String DBSIGNALE = "SIGNAL";
    public static final String ID = "_id";
    public static final String KATEGORIE = "KATEGORIE";
    public static final String NAME = "NAME";
    public static final String BEDEUTUNG = "BEDEUTUNG";
    public static final String BESCHREIBUNG = "BESCHREIBUNG";
    public static final String DS_DV = "DS_DV";
    public static final String NACHT_TAG = "TAG_NACHT";
    public static final String URL = "URL";
    public static HashMap hashSignalliste = null;

    public static final String DBKATEGORIE = "KATEGORIE";
    public static final String KATEGORIEBEZ = "BEZ";




    private static final String DBVERSION = "DBVERSION";
    public static final String VERSION = "VERSION";

    public static String getDatabasename() {
        return DATABASE_NAME;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    /**
     * Methode zur Erstellung der Datenbank
     * @return String
     */
    public static String getCreateTables() {
        String s = "CREATE TABLE  if not exists "+DBSIGNALE+" ( "+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KATEGORIE+" VARCHAR(30) DEFAULT '', "+
                DS_DV + " VARCHAR (30) DEFAULT '', " +
                NACHT_TAG + " VARCHAR (30) DEFAULT '', " +
                NAME + "  VARCHAR(30) DEFAULT '', "+
                BEDEUTUNG + "  VARCHAR(30) DEFAULT '', "+
                BESCHREIBUNG + " VARCHAR(200) DEFAULT '', "+
                URL + " VARCHAR(200) DEFAULT '' "+
                " );\n"+

                " CREATE TABLE  if not exists "+ DBKATEGORIE +" ( "+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KATEGORIEBEZ +" VARCHAR(40) DEFAULT '' unique "+
                " );"+

                "CREATE TABLE  if not exists "+DBVERSION+" ( "+
                VERSION+ "  INTEGER DEFAULT 0 "+
                " );\n"+
                "INSERT INTO " +DBVERSION+ " ( "+VERSION+" ) "+
                " VALUES (1);";
        Log.i(MainActivity.TAG, "**********\n"+s);
        return s;
    } // getCreateTables"

    /**
     * Methode zum Updaten eines bestehenden Signals
     * @param signal signalupdate
     * @return DBError
     */
    public static DBError updateSignal(Signal signal) {
        DBSOpenHandler dbhandler = DBSOpenHandler.getInstance();
        ContentValues values = new ContentValues();
        values.put(KATEGORIE, signal.kategorie);
        values.put(NAME, signal.name);
        values.put(BEDEUTUNG, signal.bedeutung);
        values.put(BESCHREIBUNG, signal.beschreibung);
        values.put(DS_DV, signal.ds_dv);
        values.put(NACHT_TAG, signal.tag_nacht);

        return dbhandler.updateSQL(values,DBSIGNALE,ID,signal.id);
    }

    /**
     * Methode zum Läschen eines Signals ein einer bestimmten ID
     * @param id Id des Signals
     * @return DBError
     */
    public static DBError deleteSignal(long id) {
        DBSOpenHandler dbhandler = DBSOpenHandler.getInstance();
        return dbhandler.deleteSQL(DBSIGNALE,ID, id);
    }

    /**
     * Methode zum Einfügen eines Signals
     * @param signal einzufügendes Signal
     * @return DBError
     */
    public static DBError insertSignal(Signal signal) {
        DBSOpenHandler dbhandler = DBSOpenHandler.getInstance();
        ContentValues values = new ContentValues();
        values.put(KATEGORIE, signal.kategorie);
        values.put(NAME, signal.name);
        values.put(BEDEUTUNG, signal.bedeutung);
        values.put(BESCHREIBUNG, signal.beschreibung);
        values.put(DS_DV, signal.ds_dv);
        values.put(NACHT_TAG, signal.tag_nacht);
        values.put(URL, signal.url);
        return dbhandler.insertSQL(values, DBSIGNALE);
    }

    /**
     * Methode zum Hinzufügen einer Kategorie
     * @param kat hinzuzufügende Kategorie
     * @return DBError
     */
    public static DBError insertKategorie(Kategorie kat){
        DBSOpenHandler dbHandler = DBSOpenHandler.getInstance();
        ContentValues values = new ContentValues();
        values.put(KATEGORIEBEZ, kat.bez);
        return dbHandler.insertSQL(values, DBKATEGORIE);
    }

    /**
     * Methode zum holen der aktuellen Datenbankversion
     * @return int
     */
    public static int getDBVersion() {
        String[] tablesCoplumns= {VERSION};
        DBSOpenHandler dbshandler = DBSOpenHandler.getInstance();
        Cursor cursor = dbshandler.getQuery(DBVERSION,tablesCoplumns,null,null);
        int version=-1;
        if (cursor==null) {
            Log.i(MainActivity.TAG,"Der Cursor für die DBVersion ist null");
        }
        else {
            int anzColumn = cursor.getColumnCount();
            int anzRows = cursor.getCount();
            Log.i(MainActivity.TAG,"************************ Anzahl columns: "+anzColumn);
            Log.i(MainActivity.TAG,"************************ Anzahl rows: "+anzRows);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                version = cursor.getInt(0);
                Log.i(MainActivity.TAG, "Version: "+version);
                cursor.moveToNext();
            } // while
            cursor.close();;
        }
        return version;

    }

    /**
     * methode zum erstellen eines Cursors, welcher Informationen über Signale enthält
     * @param where where-Bedingung
     * @param orderby orderby- Anweisung
     * @return Cursor
     */
    public static Cursor  getSignalCursor(String where, String orderby) {
        DBSQueries.hashSignalliste = null;  // nun muss sie neu geladen werden
        String[] tablesColumns = {ID, KATEGORIE, NAME, BEDEUTUNG, BESCHREIBUNG, DS_DV, NACHT_TAG, URL};
        DBSOpenHandler dbshandler = DBSOpenHandler.getInstance();
        Cursor cursor = dbshandler.getQuery(DBSIGNALE,tablesColumns,where, orderby);
        if (cursor==null) {
            Log.i(MainActivity.TAG, "Der Cursor in getSignalCursor ist null");
        }
        else {
            int anzColumn = cursor.getColumnCount();
            int anzRows = cursor.getCount();
            Log.i(MainActivity.TAG,"************************ Anzahl Std columns: "+anzColumn);
            Log.i(MainActivity.TAG,"************************ Anzahl Std rows: "+anzRows);
        }
        return cursor;
    }

    /**
     * Methode zum erstellen eines Cursors, welcher Informationen über Kategorien enthält
     * @param orderby order by Anweisung
     * @return Cursor
     */
    public static Cursor getKategorieCursor(String orderby){
        DBSQueries.hashSignalliste = null;
        String[] tablesColumns = {ID, KATEGORIEBEZ};
        DBSOpenHandler dbshandler = DBSOpenHandler.getInstance();
        Cursor cursor = dbshandler.getQuery(DBKATEGORIE, tablesColumns, null, orderby);
        if (cursor==null) {
            Log.i(MainActivity.TAG, "Der Cursor in getKategorieCursor ist null");
        }
        else {
            int anzColumn = cursor.getColumnCount();
            int anzRows = cursor.getCount();
            Log.i(MainActivity.TAG,"************************ Anzahl Std columns: "+anzColumn);
            Log.i(MainActivity.TAG,"************************ Anzahl Std rows: "+anzRows);
        }
        return cursor;
    }

    /**
     * Methode zum erstellen einer ArrayList mit Informationen über Signale
     * @return liste
     */
    public static ArrayList<Signal> getInitStignalListe() {
        ArrayList<Signal> liste = new ArrayList<>();
        Log.i(MainActivity.TAG, "########################################################  in initListe ");
        liste.clear();
        liste.add( new Signal(1, "Hp-Signal", "Hp0", "Halt.", "Ein Signalflügel - bei zweiflügligen Signalen der obere Flügel - zeigt waagerecht nach rechts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp0formsignal.png"));
        liste.add( new Signal(2, "Hp-Signal", "Hp0", "Halt.", "Ein rotes Licht oder zwei rote Lichter nebeneinander.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp0lichtsignal.png"));
        liste.add( new Signal(3, "Hp-Signal", "Hp1", "Fahrt.", "Ein Signalflügel - bei zweiflügligen Signalen der obere Flügel - zeigt schräg nach rechts aufwärts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp1formtag.png"));
        liste.add( new Signal(4, "Hp-Signal", "Hp1", "Fahrt.", "Ein grünes Licht.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp1licht.png"));
        liste.add( new Signal(5, "Hp-Signal", "Hp2", "Langsamfahrt.", "Zwei Signalflügel zeigen schräg nach rechts aufwärts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp2formtag.png"));
        liste.add( new Signal(6, "Hp-Signal", "Hp2", "Langsamfahrt.", "Ein grünes und senkrecht darunter ein gelbes Licht.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hp2licht.png"));

        liste.add( new Signal(7, "Ks-Signal", "Ks1", "Fahrt.", "Ein grünes Licht bzw. ein grünes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ks1.gif"));
        liste.add( new Signal(8, "Ks-Signal", "Ks2", "Halt erwarten.", "Ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ks2.png"));

        liste.add( new Signal(9, "Hl-Signal", "Hl1", "Fahrt mit Höchstgeschwindigkeit.", "Ein grünes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl1.png"));
        liste.add( new Signal(10, "Hl-Signal", "Hl2", "Fahrt mit 100 km/h, dann Höchstgeschwindigkeit.", "Ein gelbes Licht mit einem grünen Lichtstreifen, darüber ein grünes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl2.png"));
        liste.add( new Signal(11, "Hl-Signal", "Hl3a", "Fahrt mit 40 km/h, dann mit Höchstgeschwindigkeit.", "Ein gelbes Licht, darüber ein grünes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl3a.png"));
        liste.add( new Signal(12, "Hl-Signal", "H3b", "Fahrt mit 60 km/h, dann mit Höchstgeschwindigkeit.", "Ein gelbes Licht mit einem gelben Lichtstreifen, darüber ein grünes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl3b.png"));
        liste.add( new Signal(13, "Hl-Signal", "Hl4", "Höchstgeschwindigkeit auf 100 km/h ermäßigen.", "Ein grünes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl4.gif"));
        liste.add( new Signal(14, "Hl-Signal", "Hl5", "Fahrt mit 100 km/h.", "Ein gelbes Licht mit einem grünen Lichtstreifen, darüber ein grünes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl5.gif"));
        liste.add( new Signal(15, "Hl-Signal", "Hl6a", "Fahrt mit 40 km/h, dann mit 100 km/h.", "Ein gelbes Licht, darüber ein grünes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl6a.gif"));
        liste.add( new Signal(16, "Hl-Signal", "Hl6b", "Fahrt mit 60 km/h, dann mit 100 km/h.", "Ein gelbes Licht mit einem gelben Lichtstreifen, darüber ein grünes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl6b.gif"));
        liste.add( new Signal(17, "Hl-Signal", "Hl7", "Höchstgeschwindigkeit auf 40 km/h (60 km/h) ermäßigen.", "Ein gelbes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl7.gif"));
        liste.add( new Signal(18, "Hl-Signal", "Hl8", "Geschwindigkeit 100 km/h auf 40 km/h (60 km/h) ermäßigen.", "Ein gelbes Licht mit einem grünen Lichtstreifen, darüber ein gelbes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl8.gif"));
        liste.add( new Signal(19, "Hl-Signal", "Hl9a", "Fahrt mit 40 km/h, dann mit 40 km/h (60 km/h).", "Ein gelbes Licht, darüber ein gelbes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl9a.gif"));
        liste.add( new Signal(20, "Hl-Signal", "Hl9b", "Fahrt mit 60 km/h, dann mit 40 km/h (60 km/h).", "Ein gelbes Licht mit einem gelben Lichtstreifen, darüber ein gelbes Blinklicht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl9b.gif"));
        liste.add( new Signal(21, "Hl-Signal", "Hl10", "\"Halt\" erwarten.", "Ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl10.png"));
        liste.add( new Signal(22, "Hl-Signal", "Hl11", "Geschwindigkeit 100 km/h ermäßigen, \"Halt\" erwarten.", "Ein gelbes Licht mit einem grünen Lichtstreifen, darüber ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl11.png"));
        liste.add( new Signal(23, "Hl-Signal", "Hl12a", "Geschwindigkeit 40 km/h ermäßigen, \"Halt\" erwarten.", "Zwei gelbe Lichter übereinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl12a.png"));
        liste.add( new Signal(24, "Hl-Signal", "Hl12b", "Geschwindigkeit 60 km/h ermäßigen, \"Halt\" erwarten.", "Ein gelbes Licht mit einem gelben Lichtstreifen, darüber ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/hl12b.png"));

        liste.add( new Signal(25, "Sv-Signal", "Sv0", "Zughalt! Weiterfahrt auf Sicht.", "Zwei gelbe Lichter waagerecht nebeneinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv0.png"));
        liste.add( new Signal(26, "Sv-Signal", "Sv1", "Fahrt! Fahrt erwarten.", "Zwei grüne Lichter waagerecht nebeneinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv1.png"));
        liste.add( new Signal(27, "Sv-Signal", "Sv2", "Fahrt! Halt erwarten.", "Ein grünes, rechts daneben in gleicher Höhe ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv2.png"));
        liste.add( new Signal(28, "Sv-Signal", "Sv3", "Fahrt! Langsamfahrt erwarten.", "Links ein grünes Licht; rechts in gleicher Höhe ein grünes und senkrecht darunter ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv3.png"));
        liste.add( new Signal(29, "Sv-Signal", "Sv4", "Langsamfahrt! Fahrt erwarten.", "Links ein grünes und senkrecht darunter ein gelbes Licht; rechts in Höhe des oberen linken Lichtes ein grünes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv4.png"));
        liste.add( new Signal(30, "Sv-Signal", "Sv5", "Langsamfahrt! Langsamfahrt erwarten.", "Links ein grünes und senkrecht darunter ein gelbes Licht; rechts daneben in gleicher Höhe die gleichen Lichter.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv5.png"));
        liste.add( new Signal(31, "Sv-Signal", "Sv6", "Langsamfahrt! Halt erwarten.", "Links ein grünes, senkrecht darunter ein gelbes Licht; rechts in Höhe des oberen linken Lichtes ein gelbes Licht.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sv6.png"));

        liste.add( new Signal(32, "Vr-Signal", "Vr0", "Halt erwarten.", "Die runde Scheibe steht senkrecht. Wo ein Flügel vorhanden ist, zeigt er senkrecht nach unten.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr0formtag.png"));
        liste.add( new Signal(33, "Vr-Signal", "Vr0", "Halt erwarten.", "Zwei gelbe Lichter nach rechts steigend.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr0nachtform.png"));
        liste.add( new Signal(34, "Vr-Signal", "Vr0", "Halt erwarten.", "Zwei gelbe Lichter nach rechts steigend. An Vorsignalen im Geltungsbereich der DV 301, die nicht an Hauptsignalen stehen, kann bis auf Weiteres nur ein gelbes Licht gezeigt werden.", "DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vrlichtvr0.png"));
        liste.add( new Signal(35, "Vr-Signal", "Vr1", "Fahrt erwarten.", "Die runde Scheibe liegt waagerecht. Wo ein Flügel vorhanden ist, zeigt er senkrecht nach unten.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr1.png"));
        liste.add( new Signal(36, "Vr-Signal", "Vr1", "Fahrt erwarten.", "Zwei grüne Lichter nach rechts steigend.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr1nacht.png"));
        liste.add( new Signal(37, "Vr-Signal", "Vr1", "Fahrt erwarten.", "Zwei grüne Lichter nach rechts steigend. An Vorsignalen im Geltungsbereich der DV 301, die nicht an Hauptsignalen stehen, kann bis auf Weiteres nur ein grünes Licht gezeigt werden.", "DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vrlichtvr1.png"));
        liste.add( new Signal(38, "Vr-Signal", "Vr2", "Langsamfahrt erwarten.", "Die runde Scheibe steht senkrecht. Der Flügel zeigt schräg rechts abwärts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr2formtag.png"));
        liste.add( new Signal(39, "Vr-Signal", "Vr2", "Langsamfahrt erwarten.", "Ein gelbes Licht und nach rechts steigend ein grünes Licht.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr2formnacht.png"));
        liste.add( new Signal(40, "Vr-Signal", "Vr2", "Langsamfahrt erwarten.", "Ein gelbes Licht und nach rechts steigend ein grünes Licht. Das Signal kann im Geltungsbereich der DV 301 auch ein grünes Licht und nach rechts steigend ein gelbes Licht zeigen.", "DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vrlichtvr2.png"));
        liste.add( new Signal(41, "Vr-Signal", "Vr1/2", "Fahrt oder Langsamfahrt erwarten.", "Die runde Scheibe liegt waagerecht.", "DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr12.png"));
        liste.add( new Signal(42, "Vr-Signal", "Vr1/2", "Fahrt oder Langsamfahrt erwarten.", "Zwei grüne Lichter nach rechts steigend.", "DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/vr12nacht.png"));

        liste.add( new Signal(43, "Zs-Signal", "Zs1", "Am Signal Hp 0 oder am gestörten Lichthauptsignal ohne schriftlichen Befehl vorbeifahren.", "Drei weiße Lichter in Form eines A oder ein weißes Blinklicht.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs1blink.gif"));
        liste.add( new Signal(44, "Zs-Signal", "Zs2", "Die Fahrstraße führt in die angezeigte Richtung.", "Ein weiß leuchtender Buchstabe.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs2.png"));
        liste.add( new Signal(45, "Zs-Signal", "Zs2v", "Richtungsanzeiger (Zs 2) erwarten.", "Ein gelbleuchtender Buchstabe.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs2v.png"));
        liste.add( new Signal(46, "Zs-Signal", "Zs3", "Die durch die Kennziffer angezeigte Geschwindigkeit darf vom Signal ab im anschließenden Weichenbereich nicht überschritten werden.", "Eine weiße kennziffer auf dreieckiger schwarzer Tafel mit weißem Rand.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs3form.png"));
        liste.add( new Signal(47, "Zs-Signal", "Zs3v", "Geschwindigkeitsanzeiger (Zs 3) erwarten.", "Eine gelbleuchtende Kennziffer.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs3v.png"));
        liste.add( new Signal(48, "Zs-Signal", "Zs6", "Der Fahrweg führt in das Streckengleis entgegen der gewöhnlichen Fahrtrichtung.", "Ein weiß leuchtender schräger Lichtstreifen, dessen Enden in der Regel senkrecht nach oben und unten abgebogen sind.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs6licht.png"));
        liste.add( new Signal(49, "Zs-Signal", "Zs7", "Am Signal Hp 0 oder am gestörten Lichthauptsignal ohne schriftlichen Befehl vorbeifahren! Weiterfahrt auf Sicht.", "Drei gelbe Lichter in Form eines V", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs7.png"));
        liste.add( new Signal(50, "Zs-Signal", "Zs8", "Am Halt zeigenden oder gestörten Hauptsignal vorbeifahren, der Fahrweg führt in das Streckengleis entgegen der gewöhnlichen Fahrtrichtung.", "Drei blinkende Lichter in Form eines A", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs8.gif"));
        liste.add( new Signal(51, "Zs-Signal", "Zs9", "Nach dem zulässigen Vorbeifahren an dem Halt zeigenden oder gestörten Lichthauptsignal Halt vor dem Bahnübergang! Weiterfahrt nach Sicherung.", "Eine dreieckige, weiße Tafel mit rotem Rand und schwarzem Gatter.", "DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs9.png"));
        liste.add( new Signal(52, "Zs-Signal", "Zs10", "Ende der Geschwindigkeitsbeschränkung.", "Ein weißer Pfeil mit der Spitze nach oben auf pfeilförmiger, schwarzer Tafel.", "DS", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs10form.png"));
        liste.add( new Signal(53, "Zs-Signal", "Zs12", "Am Halt zeigenden oder gestörten Hauptsignal auf mündlichen oder fernmündlichen Auftrag vorbeifahren.", "Eine weiße Tafel mit rotem Rand und rotem \"M\" in Schreibschrift.", "DS/DV", "Tagzeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs12.png"));
        liste.add( new Signal(54, "Zs-Signal", "Zs13", "Fahrt in ein Stumpfgleis oder in ein Gleis mit verkürztem Einfahrweg.", "Ein um 90° nach links umgelegtes gelbes rückstrahlendes \"T\" auf einer rechteckigen schwarzen Tafel.", "DS/DV", "Tagzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs13form.png"));
        liste.add( new Signal(55, "Zs-Signal", "Zs13", "Fahrt in ein Stumpfgleis oder in ein Gleis mit verkürztem Einfahrweg.", "Ein um 90° nach links umgelegtes gelbleuchtendes \"T\"", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs13licht.png"));
        liste.add( new Signal(56, "Zs-Signal", "Zs103", "Das Halt zeigende Hauptsignal gilt nicht für Rangierabteilungen.", "Eine rechteckige schwarze Tafel mit weißen Rauten.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zs103.png"));

        liste.add( new Signal(57, "Ts-Signal", "Ts1", "Nachschieben einstellen.", "Um 90° nach rechts umgelegtes weißes \"T\" auf schwarzer Rechteckscheibe.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ts1.png"));
        liste.add( new Signal(58, "Ts-Signal", "Ts2", "Halt für zurückkehrende Schiebelokomotiven und Sperrfahrten.", "Quadratische, auf der Spitze stehende weiße Scheibe mit schwarzem Rand.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ts2.png"));
        liste.add( new Signal(59, "Ts-Signal", "Ts3", "Weiterfahrt für zurückkehrende Schiebelokomotiven und Sperrfahrten.", "Auf Signal Ts 2 ein schwarzer nach rechts steigender Streifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ts3.png"));

        liste.add( new Signal(60, "Lf-Signal", "Lf1", "Es folgt eine vorübergehende Langsamfahrstelle, auf der die angezeigte Geschwindigkeit nicht überschritten werden darf.", "Eine auf der Spitze stehende dreieckige gelbe Scheibe mit weißem Rand zeigt eine schwarze Kennziffer. Bei beschränktem Raum kann die Dreieckspitze nach oben zeigen.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf1.png"));
        liste.add( new Signal(61, "Lf-Signal", "Lf1", "Es folgt eine vorübergehende Langsamfahrstelle, auf der die angezeigte Geschwindigkeit nicht überschritten werden darf.", "Unter dem beleuchteten Tageszeichen zwei schräg nach links steigende Lichter. Bei beschränktem Raum befinden sich die Lichter vor dem Tageszeichen.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf1nacht.png"));
        liste.add( new Signal(62, "Lf-Signal", "Lf1/2", "Auf dem am Signal beginnenden, in der Regel durch eine Endscheibe begrenzten Gleisabschnitt darf die angezeigte Geschwindigkeit nicht überschritten werden.", "Eine rechteckige, gelbe Scheibe mit weißem Rand zeigt eine schwarze Ziffer.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf12.png"));
        liste.add( new Signal(63, "Lf-Signal", "Lf2", "Anfang der vorübergehenden Langsamfahrstelle.", "Eine rechteckige, auf der Schmalseite stehende oder quadratische gelbe Scheibe mit schwarzem A.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf2.png"));
        liste.add( new Signal(64, "Lf-Signal", "Lf3", "Ende der vorübergehenden Langsamfahrstelle.", "Eine rechteckige, auf der Schmalseite stehende oder quadratische weiße Scheibe mit schwarzem E.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf3.png"));
        liste.add( new Signal(65, "Lf-Signal", "Lf4", "Es folgt eine ständige Langsamfahrstelle, auf der die angezeigte Geschwindigkeit nicht überschritten werden darf.", "Eine auf der Spitze stehende dreieckige weiße Tafel mit schwarzem Rand zeigt eine schwarze Kennziffer. Bei beschränktem Raum kann die Dreieckspitze nach oben zeigen.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf4.png"));
        liste.add( new Signal(66, "Lf-Signal", "Lf4", "Die angezeigte Geschwindigkeit darf nicht überschritten werden.", "Eine auf der Spitze stehende dreieckige, weiße Tafel mit schwarzem Rand zeigt eine schwarze Geschwindigkeitszahl. Bei beschränktem Raum kann die Dreieckspitze nach oben zeigen.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf4dv.png"));
        liste.add( new Signal(67, "Lf-Signal", "Lf5", "Die auf der Geschwindigkeitstafel (Lf 4) angezeigte Geschwindigkeitsbeschränkung muss durchgeführt sein.", "Eine rechteckige, auf der Schmalseite stehende weiße Tafel mit schwarzem A.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf5.png"));
        liste.add( new Signal(68, "Lf-Signal", "Lf5", "Die durch das Signal Lf 4 angezeigte Geschwindigkeitsbeschränkung muss durchgeführt sein.", "Eine rechteckige, weiße Tafel mit schwarzen Ecken.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf5dv.png"));
        liste.add( new Signal(69, "Lf-Signal", "Lf6", "Ein Geschwindigkeitssignal (Lf 7) ist zu erwarten.", "Eine auf der Spitze stehende, schwarz- und weißumrandete dreieckige gelbe Tafel zeigt eine schwarze Kennziffer. Die gezeigte Kennziffer bedeutet, dass der 10fache Wert in km/h als Fahrgeschwindigkeit vom Signal Lf 7 ab zugelassen ist. Bei beschränktem Raum kann die Dreieckspitze nach oben zeigen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf6.png"));
        liste.add( new Signal(70, "Lf-Signal", "Lf7", "Die angezeigte Geschwindigkeit darf vom Signal ab nicht überschritten werden.", "Eine rechteckige, auf der Schmalseite stehende oder quadratische weiße Tafel mit schwarzem Rand zeigt eine schwarze Kennziffer. Die gezeigte Kennziffer bedeutet, dass der 10fache Wert in km/h als Fahrgeschwindigkeit zugelassen ist.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/lf7.png"));

        liste.add( new Signal(71, "Sh-Signal", "Sh0", "Halt! Fahrverbot.", "Ein waagerechter schwarzer Streifen in runder weißer Scheibe auf schwarzem Grund.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh0.png"));
        liste.add( new Signal(72, "Sh-Signal", "Sh1", "Fahrverbot aufgehoben.", "Ein nach rechts steigender schwarzer Streifen auf runder weißer Scheibe.", "DS", "Tageszeichen","https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh1.png"));
        liste.add( new Signal(73, "Sh-Signal", "Sh1", "Fahrverbot aufgehoben.", "Zwei weiße Lichter nach rechts steigend.", "DS", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh1zwei.png"));
        liste.add( new Signal(74, "Sh-Signal", "Sh2", "Schutzhalt.", "Eine rechteckige rote Scheibe mit weißem Rand.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh2eins.png"));
        liste.add( new Signal(75, "Sh-Signal", "Sh2", "Schutzhalt.", "Ein rotes Licht am Tageszeichen oder am Ausleger des Wasserkrans.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh2zwei.png"));
        liste.add( new Signal(76, "Sh-Signal", "Sh3", "Sofort halten.", "Eine rot-weiße Signalfahne, irgendein Gegenstand oder der Arm wird im Kreis geschwungen.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh3.png"));
        liste.add( new Signal(77, "Sh-Signal", "Sh3", "Sofort halten.", "Laterne, möglichst rot abgeblendet oder ein leuchtender Gegenstand wird im Kreis geschwungen.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh3zwei.png"));
        liste.add( new Signal(78, "Sh-Signal", "Sh5", "Sofort halten.", "Mehrmals drei kurze Töne nacheinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/sh5.png"));

        liste.add( new Signal(77, "Ra-Signal", "Ra1", "Wegfahren.", "Ein langer Ton.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra1ton.png"));
        liste.add( new Signal(78, "Ra-Signal", "Ra1", "Wegfahren.", "Senkrechte Bewegung des Armes von oben nach unten.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra1hand.png"));
        liste.add( new Signal(79, "Ra-Signal", "Ra1", "Wegfahren.", "Eine senkrechte Bewegung der Laterne von oben nach unten.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra1handnacht.png"));
        liste.add( new Signal(80, "Ra-Signal", "Ra2", "Herkommen.", "Zwei mäßig lange Töne.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra2ton.png"));
        liste.add( new Signal(81, "Ra-Signal", "Ra2", "Herkommen.", "Langsame waagerechte Bewegung des Armes hin und her.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra2hand.png"));
        liste.add( new Signal(82, "Ra-Signal", "Ra2", "Herkommen.", "Eine senkrechte Bewegung der Laterne von oben nach unten.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra2handnacht.png"));
        liste.add( new Signal(83, "Ra-Signal", "Ra3", "Aufdrücken.", "Zwei kurze Töne schnell nacheinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra3ton.png"));
        liste.add( new Signal(84, "Ra-Signal", "Ra3", "Aufdrücken.", "Beide Arme in Schulterhöhe nach vorn heben und die flach ausgestreckten Hände wiederholt einander nähern.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra3hand.png"));
        liste.add( new Signal(85, "Ra-Signal", "Ra3", "Aufdrücken.", "Wie am Tage, in der Hand eine Laterne.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra3handnacht.png"));
        liste.add( new Signal(86, "Ra-Signal", "Ra4", "Abstoßen.", "Zwei lange Töne und ein kurzer Ton.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra4ton.png"));
        liste.add( new Signal(87, "Ra-Signal", "Ra4", "Abstoßen.", "Zweimal eine waagerechte Bewegung des Armes vom Körper nach außen und eine schnelle senkrechte Bewegung nach unten.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra4hand.png"));
        liste.add( new Signal(88, "Ra-Signal", "Ra4", "Abstoßen.", "Zweimal eine waagerechte Bewegung der Laterne vom Körper nach außen und eine schnelle senkrechte Bewegung nach unten.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra4handnacht.png"));
        liste.add( new Signal(89, "Ra-Signal", "Ra5", "Rangierhalt.", "Drei kurze Töne schnell nacheinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra5ton.png"));
        liste.add( new Signal(90, "Ra-Signal", "Ra5", "Rangierhalt.", "Kreisförmige Bewegung des Arms.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra5hand.png"));
        liste.add( new Signal(91, "Ra-Signal", "Ra5", "Rangierhalt.", "Kreisförmige Bewegung der Handlaterne.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra5handnacht.png"));
        liste.add( new Signal(92, "Ra-Signal", "Ra6", "Halt! Abdrücken verboten.", "Ein waagerechter weißer Balken mit schwarzem Rand.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra6.png"));
        liste.add( new Signal(93, "Ra-Signal", "Ra6", "Halt! Abdrücken verboten.", "Ein waagerechter weißer Lichtstreifen.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra6.png"));
        liste.add( new Signal(94, "Ra-Signal", "Ra7", "Langsam abdrücken.", "Ein weißer Balken mit schwarzem Rand schräg nach rechts aufwärts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra7.png"));
        liste.add( new Signal(95, "Ra-Signal", "Ra7", "Langsam abdrücken.", "Ein weißer Lichtstreifen schräg nach rechts aufwärts.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra7.png"));
        liste.add( new Signal(96, "Ra-Signal", "Ra8", "Mäßig schnell abdrücken.", "Ein senkrechter weißer Balken mit schwarzem Rand.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra8.png"));
        liste.add( new Signal(97, "Ra-Signal", "Ra8", "Mäßig schnell abdrücken.", "Ein senkrechter weißer Lichtstreifen.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra8.png"));
        liste.add( new Signal(98, "Ra-Signal", "Ra9", "Zurückziehen.", "Ein senkrechter Lichtstreifen, vom oberen Ende nach rechts abzweigend ein waagerechte Lichtstreifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra9licht.png"));
        liste.add( new Signal(99, "Ra-Signal", "Ra10", "Über die Tafel hinaus darf nicht rangiert werden.", "Eine oben halbkreisförmige abgerundete weiße Tafel mit schwarzer Aufschrift \"Halt für Rangierfahrten\".", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra10.png"));
        liste.add( new Signal(100, "Ra-Signal", "Ra11", "Auftrag des Wärters zur Rangierfahrt abwarten.", "Ein gelbes W mit schwarzem Rand.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra11.png"));
        liste.add( new Signal(101, "Ra-Signal", "Ra11a", "Auftrag des Wärters zur Rangierfahrt abwarten.", "Ein gelbes W mit schwarzem Rand.", "DV", "Tagezeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra11.png"));
        liste.add( new Signal(102, "Ra-Signal", "Ra11b", "Auftrag des Wärters zur Rangierfahrt abwarten.", "Ein weißes W mit schwarzem Rand.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra11b.png"));
        liste.add( new Signal(103, "Ra-Signal", "Ra12", "Grenze, bis zu der bei zusammenlaufenden Gleisen das Gleis besetzt werden darf.", "Ein rot-weißes Zeichen.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra12.png"));
        liste.add( new Signal(104, "Ra-Signal", "Ra13", "Kennzeichnung der Grenze einer Gleisisolierung.", "Auf weißem Grund ein blauer Pfeil.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ra13.png"));

        liste.add( new Signal(105, "Wn-Signal", "Wn1", "Gerader Zweig.", "Von der Weichenspitze oder vom Herzstück aus gesehen. Ein auf der schmalseite stehendes weißes rechteck auf schwarzem Grund.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn1form.png"));
        liste.add( new Signal(106, "Wn-Signal", "Wn1", "Gerader Zweig.", "Von der Weichenspitze oder vom Herzstück aus gesehen. Zwei übereinander stehende weiße Lichter.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn1licht.png"));
        liste.add( new Signal(107, "Wn-Signal", "Wn2", "Gebogener Zweig.", "Von der Weichenspitze oder vom Herzstück aus gesehen. Ein weißer Pfeil oder Streifen auf schwarzem Grund zeigt entsprechend der Ablenkung schräg nach links oder rechts aufwärts.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn2form.png"));
        liste.add( new Signal(108, "Wn-Signal", "Wn2", "Gebogener Zweig.", "Von der Weichenspitze oder vom Herzstück aus gesehen. Zwei nebeneinander stehende weiße Lichter.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn2licht.png"));
        liste.add( new Signal(109, "Wn-Signal", "Wn3", "Gerade von links nach rechts.", "Die Pfeile oder Streifen bilden eine von links nach rechts steigende Linie.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn3.png"));
        liste.add( new Signal(110, "Wn-Signal", "Wn3", "Gerade von links nach rechts.", "Die Lichter bilden eine von links nach rechts steigende Linie.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn3.png"));
        liste.add( new Signal(111, "Wn-Signal", "Wn4", "Gerade von rechts nach links.", "Die Pfeile oder Streifen bilden eine von rechts nach links steigende Linie.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn4.png"));
        liste.add( new Signal(112, "Wn-Signal", "Wn4", "Gerade von rechts nach links.", "Die Lichter bilden eine von rechts nach links steigende Linie.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn4.png"));
        liste.add( new Signal(113, "Wn-Signal", "Wn5", "Die Pfeile oder Streifen bilden einen nach links geöffneten Winkel.", "Die Lichter bilden eine von rechts nach links steigende Linie.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn5.png"));
        liste.add( new Signal(114, "Wn-Signal", "Wn5", "Die Lichter bilden einen nach links geöffneten Winkel.", "Die Lichter bilden eine von rechts nach links steigende Linie.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn5.png"));
        liste.add( new Signal(115, "Wn-Signal", "Wn6", "Bogen von rechts nach rechts.", "Die Pfeile oder Streifen bilden einen nach rechts geöffneten Winkel.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn6.png"));
        liste.add( new Signal(116, "Wn-Signal", "Wn6", "Bogen von rechts nach rechts.", "Die Lichter bilden einen nach rechts geöffneten Winkel.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn6.png"));
        liste.add( new Signal(117, "Wn-Signal", "Wn7", "Die Gleissperre ist abgelegt.", "Ein senkrechter, schwarzer Streifen in einer runden, weißen Scheibe auf schwarzem Grund.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/wn7.png"));

        liste.add( new Signal(118, "Zp-Signal", "Zp1", "Achtung.", "Ein mäßig langer Ton.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp1.png"));
        liste.add( new Signal(119, "Zp-Signal", "Zp2", "Handbremsen mäßig anziehen.", "Ein kurzer Ton.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp2.png"));
        liste.add( new Signal(120, "Zp-Signal", "Zp3", "Handbremse stark anziehen.", "Drei kurze Töne schnell nacheinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp3.png"));
        liste.add( new Signal(121, "Zp-Signal", "Zp4", "Handbremsen lösen.", "Ein kurzer Ton.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp4.png"));
        liste.add( new Signal(122, "Zp-Signal", "Zp5", "Beim Zug ist etwas außergewöhnliches eingetreten - Bremsen und Hilfe leisten.", "Mehrmals drei kurze Töne nacheinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp5.png"));
        liste.add( new Signal(123, "Zp-Signal", "Zp6", "Bremse anlegen.", "Beide Hände werden über dem Kopf zusammengeschlagen.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp6tag.png"));
        liste.add( new Signal(124, "Zp-Signal", "Zp6", "Bremse anlegen.", "Die weißlechtende Handlaterne wird mehrmals mit der rechten Hand in einem Halbkreis gehoben und senkrecht schnell abgesenkt.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp6nacht.png"));
        liste.add( new Signal(125, "Zp-Signal", "Zp7", "Bremse lösen.", "Eine Hand wird über dem Kopf mehrmals im Halbkreis hin- und hergeschwungen.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp7tag.png"));
        liste.add( new Signal(126, "Zp-Signal", "Zp7", "Bremse lösen.", "Die weißleuchtende Handlaterne wird über dem Kopf mehrmals im Halbkreis hin- und hergeschwungen.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp7nacht.png"));
        liste.add( new Signal(127, "Zp-Signal", "Zp8", "Bremse in Ordnung.", "Beide Arme werden gestreckt senkrecht hochgehalten.", "DS/DV", "Tageszeichen", null));
        liste.add( new Signal(128, "Zp-Signal", "Zp8", "Bremse in Ordnung.", "Die weißleuchtende Handlaterne wird mehrmals in Form einer liegenden Acht bewegt.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp8nacht.png"));
        liste.add( new Signal(129, "Zp-Signal", "Zp9", "Abfahren.", "Eine runde weiße Scheibe mit grünem Rand.", "DS/DV", "Tageszeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp9kelle.png"));
        liste.add( new Signal(130, "Zp-Signal", "Zp9", "Abfahren.", "Ein grünes Licht.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp9lampe.png"));
        liste.add( new Signal(131, "Zp-Signal", "Zp11", "Kommen.", "Ein langer, ein kurzer und ein langer Ton oder ein langes, ein kurzes und ein langes Lichtzeichen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zp11.png"));

        liste.add( new Signal(132, "El-Signal", "El1v", "Signal El 1 erwarten.", "", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el1v.png"));
        liste.add( new Signal(133, "El-Signal", "El1", "Ausschalten.", "Ein zerlegtes weißes U.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el1.png"));
        liste.add( new Signal(134, "El-Signal", "El2", "Einschalten erlaubt.", "Ein geschlossenes weißes U.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el2.png"));
        liste.add( new Signal(135, "El-Signal", "El3", "Signal \"Bügel ab\" erwarten.", "Ein geschlossenes weißes U.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el3.png"));
        liste.add( new Signal(136, "El-Signal", "El4", "Einschalten erlaubt.", "Zwei in der Höhe gegeneinander versetzte weiße Streifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el4.png"));
        liste.add( new Signal(137, "El-Signal", "El5", "Bügel ab.", "Ein waagerecht weißer Streifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el5.png"));
        liste.add( new Signal(138, "El-Signal", "El6", "Halt für Fahrzeuge mit gehobenen Stromabnehmer.", "Ein auf der Spitze stehender weißer quadratischer weißer Rahmen mit innenliegendem weißen Quadrat.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/el6.png"));

        liste.add( new Signal(139, "Zg-Signal", "Zg1", "Kennzeichnung der Zugspitze.", "Vorn am ersten Fahrzeug, wenn dieses ein Triebfahrzeug oder Steuerwagen ist, drei weiße Lichter in Form eines A (Dreilicht-Spitzensignal).", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zg1.png"));
        liste.add( new Signal(140, "Zg-Signal", "Zg1", "Kennzeichnung der Zugspitze.", "Vorn am Ersten Fahrzeug, wenn dieses nicht ein Triebfahrzeug oder Steuerwagen ist, zwei weiße Lichter in gleicher Höhe.", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zg1wagen.png"));
        liste.add( new Signal(141, "Zg-Signal", "Zg2", "Kennzeichnung des Zugschlusses.", "Am letzten Fahrzeug zwei rote Lichter oder zwei rechteckige reflektierende Schilder mit weißen Dreiecken seitlich und je einem roten Dreieck oben und unten, die sich mit ihren Spitzen in der Mitte des Schildes berühren. Die roten Lichter dürfen blinken. ", "DS/DV", "Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/zg2schilder.png"));

        liste.add( new Signal(142, "Ro-Signal", "Ro1", "Vorsicht! Im Nachbargleis nähern sich Fahrzeuge.", "Mit dem Horn ein langer Ton als Mischklang aus zwei verschieden hohen Tönen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ro1.png"));
        liste.add( new Signal(143, "Ro-Signal", "Ro2", "Arbeitsgleise räumen.", "Mit dem Horn zwei lange Töne nacheinander in verschiedener Tonlage.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ro2.png"));
        liste.add( new Signal(144, "Ro-Signal", "Ro3", "Arbeitsgleise schnellstens räumen.", "Mit dem Horn mindestens fünfmal je zwei kurze Töne nacheinander in verschiedener Tonlage.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ro3.png"));
        liste.add( new Signal(145, "Ro-Signal", "Ro4", "Kennzeichnung der Gleisseite, nach der beim Ertönen der Rottenwarnsignale Ro 2 und Ro 3 die Arbeitsgleise zu räumen sind.", "Ein weißes Fahnenschild mit schwarzem Rand.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ro4.png"));

        liste.add( new Signal(146, "Ne-Signal", "Ne1", "Kennzeichnung der Stelle, wo bestimmte Züge vor einer Betriebsstelle zu halten haben.", "Eine weiße Trapeztafel mit schwarzem Rand an schwarz und weiß schräg gestreiftem Pfahl.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne1.png"));
        liste.add( new Signal(147, "Ne-Signal", "Ne2", "Kennzeichnung des Standorts eines Vorsignals.", "Eine schwarzgeränderte weiße Tafel mit zwei übereinander stehenden schwarzen Winkeln, die sich mit der Spitze berühren.", "DS/DV", "Tageszeichen/Nachtzeichen","https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne2.png"));
        liste.add( new Signal(148, "Ne-Signal", "Ne3", "Ein Vorsignal ist zu erwarten.", "Mehrere aufeinanderfolgende viereckige weiße Tafeln mit einem oder mehreren nach rechts steigenden schwarzen Streifen, deren Anzahl in Fahrtrichtung abnimmt.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne3.png"));
        liste.add( new Signal(149, "Ne-Signal", "Ne4", "Das Hauptsignal steht - abweichend von der Regel - an einem anderen Standort.", "Eine viereckige, schachbrettartig schwarz und weiß gemusterte Tafel.", "DS/DV", "Tageszeichen/Nachtzeichen","https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne4.png"));
        liste.add( new Signal(150, "Ne-Signal", "Ne5", "Kennzeichnung des Halteplatzes der Zugspitze bei planmäßig haltenden Zügen.", "Eine hochstehende weiße Rechteckscheibe mit schwarzem Rand und schwarzem H oder eine hochstehende schwarze Rechteckscheibe mit weißem H.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne5.png"));
        liste.add( new Signal(151, "Ne-Signal", "Ne6", "Ein Haltepunkt ist zu erwarten.", "Eine schräg zum Gleis gestellte waagerechte weiße Tafel mit drei schwarzen Schrägstreifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne6.png"));
        liste.add( new Signal(152, "Ne-Signal", "Ne7", "Pflugscharr heben.", "Eine weiße Pfeilspitze mit schwarzem Rand zeigt nach oben oder eine gelbe Pfeilspitze mit schwarzem Rand zeigt nach oben.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne7a.png"));
        liste.add( new Signal(153, "Ne-Signal", "Ne12", "Überwachungssignal einer Rückfallweiche beachten.", "Eine rechteckige, orangefarbene Tafel mit zwei waagerechten weißen Streifen.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne12.png"));
        liste.add( new Signal(154, "Ne-Signal", "Ne13a", "Die Rückfallweiche ist gegen die Spitze befahrbar.", "Ein weißes Licht über einem orngefarbenen waagerechten Streifen und einem orange-weiß schräg gestreiften Mastschild.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne13a.png"));
        liste.add( new Signal(155, "Ne-Signal", "Ne13b", "Die Rückfallweiche ist gegen die Spitze nicht befahrbar, vor der Weiche halten", "Ein orangefarbener waagerechter Streifen über einem orange-weiß schräg gestreiften Mastschild.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne13b.png"));
        liste.add( new Signal(156, "Ne-Signal", "Ne14", "Halt für Züge mit ETCS-Betriebsart SR", "Ein gelber Pfeil mit weißem Rand auf einer blauen quadratischen Tafel.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/ne14.png"));

        liste.add( new Signal(157, "So-Signal", "So1", "Fahren auf Sicht beenden.", "Eine viereckige rote Tafel mit liegendem weißen Kreuz.", "DV", "Tageszeichen/Nachtzeichen", "http://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/so1.png"));
        liste.add( new Signal(158, "So-Signal", "So19", "Ein Hauptsignal ist zu erwarten.", "Drei aufeinander folgende viereckige orangefarbene Tafeln mit einer, zwei oder drei weißen Kreisflächen, deren Anzahl in Fahrtrichtung abnimmt; die Kreisflächen können rückstrahlend sein.", "DV", "Tageszeichen/Nachtzeichen", "http://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/so19.png"));
        liste.add( new Signal(159, "So-Signal", "So106", "Bei fehlendem Vorsignal wird angezeigt, dass ein Hauptsignal zu erwarten ist.", "Eine weiße Sechseckscheibe mit liegendem schwarzen Kreuz an einem schwarz und weiß schräg gestreiftem Pfahl.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/so106.png"));

        liste.add( new Signal(160, "Bü-Signal", "Bü0", "Halt vor dem Bahnübergang! Weiterfahrt nach Sicherung.", "Eine runde gelbe Scheibe in einer gelben Umrahmung über einem schwarz-weiß schräg gestreiften Mastschild. Scheibe, Umrahmung und Mastschild sind rückstrahlend.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue0.png"));
        liste.add( new Signal(161, "Bü-Signal", "Bü1", "Der Bahnübergang darf befahren werden.", "Ein blinkendes weißes Licht über einer runden gelben Scheibe in einer gelben Umrahmung über einem schwarz-weiß schräg gestreiften Mastschild. Scheibe, Umrahmung und Mastschild sind rückstrahlend.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue1.gif"));
        liste.add( new Signal(162, "Bü-Signal", "Bü2", "Ein Überwachungssignal ist zu erwarten.", "Eine rechteckige schwarze Tafel mit vier auf den Spitzen übereinander stehenden rückstrahlenden weißen Rauten.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue2.png"));
        liste.add( new Signal(163, "Bü-Signal", "Bü3", "Kennzeichnung des Einschaltpunktes von Blinklichtern oder Lichtzeichen mit Fernüberwachung.", "Eine schwarz-weiß waagerecht gestreifte rückstrahlende Tafel.", "DS", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue3.png"));
        liste.add( new Signal(164, "Bü-Signal", "Bü4", "Etwa 3 Sekunden lang pfeifen!", "Eine rechteckige weiße Tafel mit schwarzem P oder eine rechteckige schwarze Tafel mit weißem Rand und weißem P.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue4.png"));
        liste.add( new Signal(165, "Bü-Signal", "Bü5", "Es ist zu läuten", "Eine rechteckige weiße Tafel mit schwarzem L.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/bue5.png"));
        liste.add( new Signal(166, "Bü-Signal", "Pf2", "Zweimal pfeifen!", "Zwei weiße Tafeln mit schwarzem P senkrecht übereinander.", "DS/DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/pf2.png"));
        liste.add( new Signal(167, "Bü-Signal", "So14", "Kennzeichnung des Einschaltpunktes von Blinklichtern.", "Ein schwarz-weiß waagerecht gestreifter Pfahl.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/so14.png"));
        liste.add( new Signal(168, "Bü-Signal", "So15", "Überwachungssignal beachten.", "Eine rechteckige, weiße rückstrahlende Tafel mit drei waagerechten, schwarzen Streifen.", "DV", "Tageszeichen/Nachtzeichen", "https://www.tf-ausbildung.de/SignalbuchOnline/Grafiken/so15.png"));


        Log.i(MainActivity.TAG,"******************************** Modul Signalliste,anzahl Signale: "+liste.size());

        return liste;
    }

    /**
     * Methode zum Erstellen einer Kategorienliste
     * @return liste
     */
    public static ArrayList<Kategorie> getInitKategorienListe(){
        ArrayList<Kategorie> liste = new ArrayList<>();
        liste.clear();
        liste.add(new Kategorie(0, "Hp-Signal"));
        liste.add(new Kategorie(0, "Ks-Signal"));
        liste.add(new Kategorie(0, "Hl-Signal"));
        liste.add(new Kategorie(0, "Sv-Signal"));
        liste.add(new Kategorie(0, "Vr-Signal"));
        liste.add(new Kategorie(0, "Zs-Signal"));
        liste.add(new Kategorie(0, "Ts-Signal"));
        liste.add(new Kategorie(0, "Lf-Signal"));
        liste.add(new Kategorie(0, "Sh-Signal"));
        liste.add(new Kategorie(0, "Ra-Signal"));
        liste.add(new Kategorie(0, "Wn-Signal"));
        liste.add(new Kategorie(0, "Zp-Signal"));
        liste.add(new Kategorie(0, "El-Signal"));
        liste.add(new Kategorie(0, "Zg-Signal"));
        liste.add(new Kategorie(0, "Ro-Signal"));
        liste.add(new Kategorie(0, "Ne-Signal"));
        liste.add(new Kategorie(0, "So-Signal"));
        liste.add(new Kategorie(0, "Bü-Signal"));
        return liste;
    }

    /**
     * Methode zum holen eines Signlas in einem Cursor
     * @param cursor zu durchsuchender Cursor
     * @return signal
     */
    public static Signal getSignal(Cursor cursor) {
        Signal signal = new Signal();
        // Extract properties from cursor
        if (DBSQueries.hashSignalliste == null) {
            Log.i(MainActivity.TAG,"************* in hashtable init");

            DBSQueries.hashSignalliste = new HashMap(100,0.7f);
            ArrayList<Kategorie> kategorieliste = DBSQueries.getKategorieListe();
            for(Kategorie kat: kategorieliste){
                hashSignalliste.put(kat.id, kat);
                Log.i(MainActivity.TAG, "********************" + kat.id + " " + kat.bez);
            }
        }
        signal.id = cursor.getInt(cursor.getColumnIndexOrThrow(DBSQueries.ID));
        signal.kategorie = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.KATEGORIE));
        signal.name = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.NAME));
        signal.bedeutung = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.BEDEUTUNG));
        signal.beschreibung = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.BESCHREIBUNG));
        signal.ds_dv = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.DS_DV));
        signal.tag_nacht = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.NACHT_TAG));
        signal.url = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.URL));

        return signal;
    }

    /**
     * Methode zum extrahieren einer Kategorie aus einem übergebenen Cursor
     * @param cursor Cursor
     * @return kat
     */
    public static Kategorie getKategorie(Cursor cursor){
        Kategorie kat = new Kategorie();
        kat.id = cursor.getInt(cursor.getColumnIndexOrThrow(DBSQueries.ID));
        kat.bez = cursor.getString(cursor.getColumnIndexOrThrow(DBSQueries.KATEGORIEBEZ));

        return kat;
    }

    /**
     * Methode zum erstellen einer ArrayListe bestehend aus Kategorien
     * @return lsite
     */
    public static ArrayList<Kategorie> getKategorieListe(){
        ArrayList<Kategorie> liste = new ArrayList<>();
        String sql = "SELECT "+ID+", "+KATEGORIEBEZ+"  FROM "+DBKATEGORIE+" ORDER BY BEZ";
        DBSOpenHandler dbshandler = DBSOpenHandler.getInstance();
        Cursor cursor = dbshandler.getQueryPur(sql);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kategorie prf = new Kategorie();
            prf.id = cursor.getInt(0);
            prf.bez = cursor.getString(1);
            Log.i(MainActivity.TAG, "id: "+prf.id+"   bez: "+prf.bez);
            liste.add(prf);
            cursor.moveToNext();
        } // while
        cursor.close();

        return liste;
    }



}

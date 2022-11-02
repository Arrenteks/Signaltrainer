package de.medieninformatik.signaltrainer_m20962;


public class DBError {
    long n=0;
    boolean isError=false;
    String errortext ="";

    public DBError(long n, boolean isError, String errortext) {
        this.n=n; // Anzahl der durchgeführten Änderungen
        this.isError=isError;
        this.errortext = errortext;
    }

}


package de.medieninformatik.signaltrainer_m20962;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView auswertung;
    private TextView quote;

    /**
     * setzt die UI Elemente
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        auswertung = findViewById(R.id.auswertung);
        quote = findViewById(R.id.quote);

        setErgebnisse();
    }

    /**
     * Schreibt die Auswertung für die übergebenen Ergebnisse
     */
    private void setErgebnisse(){
        Intent intent = getIntent();
        int richtig = (int) intent.getExtras().get("RICHTIG");
        auswertung.setText("Sie haben " + richtig + " von 30 Signalen korrekt erkannt");
        quote.setText("Das entspricht " + 100*richtig/30 + " Prozent");
    }
}
package de.medieninformatik.signaltrainer_m20962;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton rb_0;
    private RadioButton rb_1;
    private RadioButton rb_2;
    private RadioButton[] rbArray;

    private HashMap<Integer, Integer> fragenhash;
    private HashMap<Integer, Integer> antworthash;

    private ImageView quizImgView;
    private Button button_next;
    private TextView errortext;

    private Random rnd;
    private ArrayList<Signal> signallist;

    private final int FRAGENZAHL_MAX = 30;
    private int aktuelleFrage = 1;
    private int richtig = 0;
    private int falsch = 0;

    private int sendeNr = -1;

    /**
     * setzen der einzelnen UI Elemente und Zurodnung eines Tag auf die jeweiligen RadioButon
     * @param savedInstanceState gespeicherte Instanzstatus
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        radioGroup = findViewById(R.id.radioGroup);
        button_next = findViewById(R.id.button_next);
        quizImgView = findViewById(R.id.quizImgView);
        rb_0 = findViewById(R.id.radBut_0);
        rb_1 = findViewById(R.id.radBut_1);
        rb_2 = findViewById(R.id.radBut_2);
        errortext = findViewById(R.id.errortext_quiz);

        rbArray = new RadioButton[3];
        rbArray[0] = rb_0;
        rbArray[1] = rb_1;
        rbArray[2] = rb_2;
        fragenhash = new HashMap<>();
        antworthash = new HashMap<>();

        for (int i = 0; i < rbArray.length; i++) {
            rbArray[i].setTag(i);
        }

        signallist = new ArrayList<>();
        signallist = DBSQueries.getInitStignalListe();
        rnd = new Random();
        initQuestion();
    }

    /**
     * onClickBefehl f??r den Button. Checkt ob eine Antwort gesetzt wurde. Wenn ja geht es weiter,
     * wenn nein bleibt er bei der aktuellen Frage und setzt den errortext sichtbar.
     * @param view aktueller view
     */
    public void nextQuestionClick(View view) {
        if(radioGroup.getCheckedRadioButtonId() != -1){
            if(errortext.getVisibility() == View.VISIBLE) errortext.setVisibility(View.INVISIBLE);
            putAntwort();
            aktuelleFrage++;
            checkQuizFinished();
        }else{
            errortext.setVisibility(View.VISIBLE);
        }
    }

    /**
     * checkt ob das Quiz vorbei ist. Wenn ja erfolgt die Auswertung, wenn nein wird die n??chste Frage
     * geladen
     */
    public void checkQuizFinished(){
        if(aktuelleFrage == FRAGENZAHL_MAX-1)button_next.setText("Quiz beenden");//wenn Frage 29 erreicht, mache erkenntlich, dass der n??chste Klick das Quiz beendet

        if(aktuelleFrage == FRAGENZAHL_MAX){
            for (int i = 1; i <= FRAGENZAHL_MAX; i++) {
                if(fragenhash.get(i) == antworthash.get(i)){
                    richtig++;
                }else{
                    falsch++;
                }
            }
            Log.i(MainActivity.TAG, "******* richtig: " + richtig + " falsch: " + falsch);
            button_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAuswertung();
                }
            });
        }else{
            initQuestion();
        }
    }

    /**
     * sucht sich zuf??llig ein neues Signal aus der Datenbank und legt es in den fragenhash.
     * Anschlie??end wird der Rest der UI Elemente gesetzt.
     */
    private void initQuestion(){
       radioGroup.clearCheck();
       Signal signal = signallist.get(rnd.nextInt(signallist.size()));
       Integer antwort= rnd.nextInt(rbArray.length);
       fragenhash.put(aktuelleFrage, antwort);

       new DownloadImageTask(quizImgView).execute(signal.url);

        for (RadioButton rbs: rbArray) {
            rbs.setText(signallist.get(rnd.nextInt(signallist.size())).name);//setze den Text eines der RadioButtons anhand eines zuf??llig gew??hlten Signals
        }
        rbArray[antwort].setText(signal.name);//setze die Antwort zuf??llig
    }

    /**
     * abh??ngig von der vom Nutzer gew??hlten Antwort wird eine Zahl in den antworthash eingetragen.
     */
    public void putAntwort(){
        switch (radioGroup.getCheckedRadioButtonId()){

            case R.id.radBut_0:
                antworthash.put(aktuelleFrage, 0);
                break;
            case R.id.radBut_1:
                antworthash.put(aktuelleFrage, 1);
                break;
            case R.id.radBut_2:
                antworthash.put(aktuelleFrage, 2);
                break;
            default:
                antworthash.put(aktuelleFrage, -1);//keine Antwort wurde gegeben
                break;

        }
    }

    /**
     * verpackt die Anzahl der richtigen und der falschen Antworten in eine HashMap und startet die
     * Aktivit??t f??r den benutzten Intent
     */
    public void startAuswertung(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("RICHTIG", richtig);
        intent.putExtra("FALSCH", falsch);
        startActivityForResult(intent, sendeNr);
    }
}
package de.medieninformatik.signaltrainer_m20962;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SignalListActivity extends AppCompatActivity {

    private Cursor anfangscursor;
    private int sendeNr = -1;
    private ListView listView = null;
    private MyCursorAdapter adapter;

    /**
     * setzen der UI Element und laden des Cursors zum setzen der Daten in den ListView
     * @param savedInstanceState gespeicherte Instanzinfos
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signallist);

        setContentView(R.layout.activity_signallist);
        listView = findViewById(R.id.signallistview);
        DBSOpenHandler.createInstance(this, DBSQueries.getDatabasename(), DBSQueries.getDatabaseVersion());
        Intent intent = getIntent();

        if(intent == null){
            Basis.alertDialogOk(this, "Fehler", "Intent für ausgewählte Kategorie ist null");
        }else{
            Kategorie kategorie = (Kategorie) intent.getSerializableExtra(Kategorie.KATEGORIE);
            anfangscursor = DBSQueries.getSignalCursor("KATEGORIE='" + kategorie.bez+"'", null);

            adapter = new MyCursorAdapter(this, anfangscursor);
            listView.setAdapter(adapter);

            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View rowView, int i, long l) {
                    Signal sig = (Signal)  rowView.getTag();
                    showSignalInfos(sig);
                }
            });
        }
    }

    /**
     * übermittlung des angeklicketen Signals an die Intent Hashmap und aufrufen der SignalInfos Klasse
     * für besagten Intent
     * @param signal Signalinfos
     */
    public void showSignalInfos(Signal signal){
        Intent intent = new Intent(this, SignalInfos.class);
        intent.putExtra(Signal.SIGNAL,signal);
        startActivityForResult(intent, sendeNr);
    }
}
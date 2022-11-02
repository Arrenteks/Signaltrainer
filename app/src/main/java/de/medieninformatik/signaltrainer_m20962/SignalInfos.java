package de.medieninformatik.signaltrainer_m20962;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

public class SignalInfos extends AppCompatActivity {
    ImageView imgView = null;
    TextView signalname = null;
    TextView beschreibung = null;
    TextView kategorie = null;
    TextView bedeutung = null;
    TextView geltungsbereich = null;
    TextView zeichenart = null;


    /**
     * setzen der UI Elemente und extrahierung der in intent übergebenen Signaldaten
     * @param savedInstanceState gespeicherte Instanzinfos
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_infos);
        signalname = findViewById(R.id.signalname);
        imgView = findViewById(R.id.imgView);
        beschreibung = findViewById(R.id.beschreibung);
        kategorie = findViewById(R.id.kategorie);
        bedeutung = findViewById(R.id.bedeutung);
        geltungsbereich = findViewById(R.id.geltungsbereich);
        zeichenart = findViewById(R.id.zeichenart);

        Intent intent = getIntent();
        if(intent == null){
            Basis.alertDialogOk(this, "Fehler", "Intent für ausgewähltes Signal ist null");
        }else{
            Signal signal = (Signal) intent.getSerializableExtra(Signal.SIGNAL);
            insertDatas(signal);
        }

    }

    /**
     * setzen der Signalinfos
     * @param signal Signal
     */
    public void insertDatas(Signal signal){
        signalname.setText(signal.name);
        bedeutung.setText("Bedeutung: " + signal.bedeutung);
        beschreibung.setText(signal.beschreibung);
        kategorie.setText("Kategorie: " + signal.kategorie);
        geltungsbereich.setText("Geltungsbereich: " + signal.ds_dv);
        zeichenart.setText("Zeichenart: " + signal.tag_nacht);
        new DownloadImageTask(imgView).execute(signal.url);
    }
}

/**
 * Innere Klasse für das asynchrone Laden einer Bitmap aus einer übergebenen URL
 */
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage){
        this.bmImage = bmImage;
    }

    /**
     * anweisung was im Hintergrund geladen werden soll
     * @param urls woher kommt die Bitmap
     * @return Bitmap
     */
    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try{
            InputStream in = new URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        }catch(Exception e){
            Log.e("Fehler beim Decodieren des Bildes", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    /**
     * wenn das Laden abgeschlossen ist, soll die geladene Bitmap in den übergebenen ImageView eingetragen werden
     * @param bitmap übergebenes Bild
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
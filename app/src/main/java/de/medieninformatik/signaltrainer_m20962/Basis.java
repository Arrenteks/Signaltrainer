package de.medieninformatik.signaltrainer_m20962;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;


public class Basis {

    public interface IResultYes{
        public void resultYes();
    }
    public interface IResultNo{
        public void resultNo();
    }


    public static void showToast(Context context, int stringid) {
        Toast.makeText(context, context.getString(stringid), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }



    // import android.app.AlertDialog;
    public static void alertDialogOk(Context context, String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(content);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // hier wurde ok gedrÃ¼ckt
                // callokClick();
            }
        });
        alert.show();
    }  // alertDialogOk


    // import android.app.AlertDialog;
    public static  void alertDialogNoYes(Context context, IResultYes parentyes, IResultNo parentno, String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(content);


        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (parentno!=null) parentno.resultNo();
            }
        });

        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                parentyes.resultYes();
            }
        });
        alert.show();
    }  // alertDialogYesNo

    // import android.app.AlertDialog;
    public  void alertDialogYesNoCancel(Context context, IResultYes parentyes, IResultNo parentno, String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(content);



        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // hier wurde yes gedrückt
                parentyes.resultYes();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // CNo
                if (parentno!=null) parentno.resultNo();
            }
        });

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // CNo

            }
        });

        alert.show();
    }  // alertDialogYesNoCancel


    // import android.app.AlertDialog;
    public static void alertDialogEdittext(Context context, String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(content);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // hier wurde ok gedrÃ¼ckt
                // callokClick();
            }
        });
        alert.show();
    }  // alertDialogOk


    public static  boolean FileExists(Context context, String filename) {
        try {
            FileInputStream fin = context.openFileInput(filename);
            fin.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }



    // wandelt einen String in eine double zahl um
    // bei Fehler wird double.NaN zurÃƒÂ¼ckgegeben
    public static double getDoubleNumber(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
/*			Abfrage:
		if (Double.isNaN(tzahl.getText().toString())) {
			// hier ist ergebnis NaN !!
			tergebnis.setText("-");
		}


		String dummy = tzahl1.getText().toString();  // nur ascii-text keine Zahl!!
        double zahl1 = Basis.getDoubleNumber(dummy);
        if (!Double.isFinite(zahl1)) {
            tverror.setText("Fehlerhafte 1. Zahl");
            tergebnis.setText("-");
            return;
        }
*/



    public static Integer getIntegerNumber(String input) {
        try {
            int zahl = Integer.parseInt(input);
            return new Integer(zahl);
        } catch (NumberFormatException e) {
            return null;
        }

    }





}

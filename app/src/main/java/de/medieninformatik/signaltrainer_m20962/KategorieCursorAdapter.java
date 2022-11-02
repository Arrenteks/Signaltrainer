package de.medieninformatik.signaltrainer_m20962;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class KategorieCursorAdapter extends CursorAdapter {

    public KategorieCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate((R.layout.row_layout), parent, false);
    }

    @Override
    public void bindView(View rowView, Context context, Cursor cursor) {
        TextView signalname = rowView.findViewById(R.id.name_sig);
        Kategorie kat = DBSQueries.getKategorie(cursor);

        rowView.setTag(kat);

        signalname.setText(kat.bez);
    }
}

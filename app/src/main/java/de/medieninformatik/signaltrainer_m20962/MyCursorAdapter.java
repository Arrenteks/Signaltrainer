package de.medieninformatik.signaltrainer_m20962;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter{


    public MyCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate((R.layout.row_layout), parent, false);
    }

    @Override
    public void bindView(View rowView, Context context, Cursor cursor) {
        TextView signalname = rowView.findViewById(R.id.name_sig);
        Signal sig = DBSQueries.getSignal(cursor);

        rowView.setTag(sig);

        signalname.setText(sig.name);
    }
}

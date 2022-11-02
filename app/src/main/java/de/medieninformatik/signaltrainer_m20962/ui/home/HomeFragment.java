package de.medieninformatik.signaltrainer_m20962.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import de.medieninformatik.signaltrainer_m20962.DBSOpenHandler;
import de.medieninformatik.signaltrainer_m20962.DBSQueries;
import de.medieninformatik.signaltrainer_m20962.Kategorie;
import de.medieninformatik.signaltrainer_m20962.KategorieCursorAdapter;
import de.medieninformatik.signaltrainer_m20962.MainActivity;
import de.medieninformatik.signaltrainer_m20962.R;
import de.medieninformatik.signaltrainer_m20962.Signal;
import de.medieninformatik.signaltrainer_m20962.SignalListActivity;
import de.medieninformatik.signaltrainer_m20962.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static ListView listView;
    private static Cursor anfangscursor;
    private static KategorieCursorAdapter adapter;
    private View root;

    private final int sendeNr = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();


        DBSOpenHandler.createInstance(root.getContext(), DBSQueries.getDatabasename(), DBSQueries.getDatabaseVersion());
        startListView();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    public void startListView(){
        listView = root.findViewById(R.id.listview);

        anfangscursor = DBSQueries.getKategorieCursor(null);
        if (anfangscursor.getCount() == 0) initDatas();

        adapter = new KategorieCursorAdapter(root.getContext(), anfangscursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View rowView, int position, long id) {
                Kategorie kat = (Kategorie) rowView.getTag();
                showSignalList(kat);
            }
        });
    }

    public void initDatas() {
        List<Signal> signalList = DBSQueries.getInitStignalListe();
        for (Signal sig : signalList) {
            DBSQueries.insertSignal(sig);
        }

        List<Kategorie> kategorieList = DBSQueries.getInitKategorienListe();
        for (Kategorie kat : kategorieList) {
            DBSQueries.insertKategorie(kat);
        }
    }

    public void showSignalList(Kategorie kategorie) {
        Intent intent = new Intent(root.getContext(), SignalListActivity.class);
        intent.putExtra(Kategorie.KATEGORIE, kategorie);
        startActivityForResult(intent, sendeNr);
    }
}
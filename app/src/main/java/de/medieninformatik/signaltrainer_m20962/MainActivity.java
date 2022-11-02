package de.medieninformatik.signaltrainer_m20962;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import de.medieninformatik.signaltrainer_m20962.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "********* Signale";
    private ActivityMainBinding binding;
    private int sendeNr = -1;

    /**
     * aufruf beim erstellen der Activity. Setzen der UI Elemente und Fragmente
     * @param savedInstanceState gespeicherte Instanz
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * onClick Methode für den Startbutton des ersten Quiz. Startet das Namesquiz
     * @param view View
     */
    public void startTestNameSignal(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivityForResult(intent, sendeNr);

    }

    /**
     * OnClick Methode für den StartButton des zweiten Quiz. Startet das Bedeutungsquiz.
     * @param view View
     */
    public void startTestBedeutungSignal(View view) {
        Intent intent = new Intent(this, BedeutungsquizActivity.class);
        startActivityForResult(intent, sendeNr);
    }
}

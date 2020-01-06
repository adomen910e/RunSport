package com.example.domenger.runsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Profil extends AppCompatActivity {

    private int index_profil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Bundle b;
        b = getIntent().getExtras();

        assert b != null;
        index_profil = b.getInt("id_profil");

        DbHandler db = new DbHandler(this);

        ArrayList<HashMap<String, String>> profil = db.GetUserByUserId(index_profil);

        String nom = profil.get(0).get("nom");
        TextView nomview = findViewById(R.id.profil_nom);
        nomview.setText(nom);

        String prenom = profil.get(0).get("prenom");
        TextView prenomview = findViewById(R.id.profil_prenom);
        prenomview.setText(prenom);

        String mdp = profil.get(0).get("mdp");
        TextView mdpview = findViewById(R.id.profil_mdp);
        mdpview.setText(mdp);

        String an = profil.get(0).get("date");
        TextView anview = findViewById(R.id.profil_an);
        anview.setText(an);

        String login = profil.get(0).get("login");
        TextView loginview = findViewById(R.id.profil_user);
        loginview.setText(login);
    }

    @Override
    //Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
    public boolean onCreateOptionsMenu(Menu menu) {

        //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
        MenuInflater inflater = getMenuInflater();
        //Instanciation du menu XML spécifier en un objet Menu
        inflater.inflate(R.menu.menu_icon, menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //On regarde quel item a été cliqué grâce à son id et on déclenche une action
        Intent intent;
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(Profil.this, "Profile", Toast.LENGTH_SHORT).show();
            case R.id.option_course:
                Toast.makeText(Profil.this, "Course", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.demarrer:
                Toast.makeText(Profil.this, "Demarrer", Toast.LENGTH_SHORT).show();
                intent = new Intent(Profil.this, Course.class);
                intent.putExtra("id_profil", this.index_profil);
                startActivity(intent);
                finish();
                return true;
            case R.id.stats:
                intent = new Intent(Profil.this, Statistic.class);
                intent.putExtra("id_profil", this.index_profil);
                startActivity(intent);
                Toast.makeText(Profil.this, "Stats", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.quitter:
                //Pour fermer l'application il suffit de faire finish()
                finish();
                return true;
        }
        return false;
    }
}
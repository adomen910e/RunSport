package com.example.domenger.runsport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Acceuil extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<HashMap<String, String>> courses;
    private int index_profile;
    private GoogleMap mapa;
    private PolylineOptions polylineOption;
    private SupportMapFragment mapFrag;

    private boolean map_active;
    private boolean is_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        ProgressBar speed_max = findViewById(R.id.circle_vitesse_max);
        ProgressBar speed_moy = findViewById(R.id.circle_vitesse_moy);
        ProgressBar dist = findViewById(R.id.circle_distance);
        ProgressBar time1 = findViewById(R.id.circle_time);

        TextView txt_speed_max = findViewById(R.id.value_vitesse_max);
        TextView txt_speed_moy = findViewById(R.id.value_vitesse_moyenne);
        TextView txt_dist = findViewById(R.id.value_distance);
        TextView txt_time = findViewById(R.id.value_time);
        TextView txt_date = findViewById(R.id.textdate);

        if (mapa == null) {
            mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
            mapFrag.getMapAsync(this);
            mapFrag.getView().setVisibility(View.INVISIBLE);
        }


        map_active = false;


        Button button_map = findViewById(R.id.mapShow);
        button_map.setText("Montrer le parcours");


        new Bundle();
        Bundle b;
        b = getIntent().getExtras();

        assert b != null;
        index_profile = b.getInt("id_profil");


        button_map.setOnClickListener(v -> {
            if (is_course) {
                if (!map_active) {
                    showMap();
                    map_active = true;
                    button_map.setText("Quitter la vue");
                } else {
                    invisibleMap();
                    map_active = false;
                    button_map.setText("Montrer le parcours");

                }
            }else{
                Toast.makeText(Acceuil.this, "Il n'y a pas de parcours enregistré", Toast.LENGTH_SHORT).show();
            }
        });


        DbHandler db = new DbHandler(this);

        courses = db.GetCoursesFromUser(index_profile);

        if (courses.size() == 0){
            is_course =false;
            speed_max.setProgress(0);
            speed_moy.setProgress(0);
            dist.setProgress(0);
            time1.setProgress(0);

            txt_speed_max.setText("0");
            txt_speed_moy.setText("0");
            txt_dist.setText("0");
            txt_time.setText("0");
        }else{
            txt_date.setText(courses.get(courses.size()-1).get("date"));

            is_course = true;
            HashMap<String, String> last_course = courses.get(courses.size()-1);
            float value_speed_max = Float.parseFloat(last_course.get("speedmax"));
            //Vitesse max 50 km/h
            float VITESSE_MAX_CIRCLE = 50;
            speed_max.setProgress((int) (value_speed_max *100/ VITESSE_MAX_CIRCLE));


            float value_speed_moy = Float.parseFloat(last_course.get("speedmoy"));
            //Vitesse moyenne 50km/h
            float VITESSE_MOY_CIRCLE = 50;
            speed_moy.setProgress((int) (value_speed_moy *100/ VITESSE_MOY_CIRCLE));

            float value_dist = Float.parseFloat(last_course.get("distance"));
            //Distance max 50 km
            float DISTANCE_CIRCLE = 50;
            dist.setProgress((int) (value_dist /10/ DISTANCE_CIRCLE));

            float value_time = Float.parseFloat(last_course.get("time"));
            //Temps de base max 1 heure
            float TIME_CIRCLE = 3600;
            time1.setProgress((int) ((value_time * 100) / TIME_CIRCLE));

            DecimalFormat df = new DecimalFormat("#.##");

            txt_speed_max.setText(df.format(value_speed_max));
            txt_speed_moy.setText(df.format(value_speed_moy));
            txt_dist.setText(df.format(value_dist /1000));
            LocalTime timeOfDay = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeOfDay = LocalTime.ofSecondOfDay((long) value_time);
            }
            assert timeOfDay != null;
            String time = timeOfDay.toString();
            txt_time.setText(time);

            String points = last_course.get("points");
            String tab_point[] = points.split(Pattern.quote("|"));

            polylineOption = new PolylineOptions().width(8).color(Color.BLUE);
            for (int i=1; i<tab_point.length; i++){
                String latLong[] = tab_point[i].split(Pattern.quote(";"));
                LatLng latitudeLongitude = new LatLng(Float.parseFloat(latLong[0]), Float.parseFloat(latLong[1]));
                polylineOption.add(latitudeLongitude);

            }

        }

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
                Toast.makeText(Acceuil.this, "Profile", Toast.LENGTH_SHORT).show();
                intent = new Intent(Acceuil.this, Profil.class);
                intent.putExtra("id_profil", this.index_profile);
                intent.putExtra("nb_courses", this.courses.size());
                startActivity(intent);
            case R.id.option_course:
                Toast.makeText(Acceuil.this, "Course", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.demarrer:
                Toast.makeText(Acceuil.this, "Demarrer", Toast.LENGTH_SHORT).show();
                intent = new Intent(Acceuil.this, Course.class);
                intent.putExtra("id_profil", this.index_profile);
                startActivity(intent);
                return true;
            case R.id.stats:
                intent = new Intent(Acceuil.this, Statistic.class);
                intent.putExtra("id_profil", this.index_profile);
                startActivity(intent);
                Toast.makeText(Acceuil.this, "Stats", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.quitter:
                //Pour fermer l'application il suffit de faire finish()
                finish();
                return true;
        }
        return false;
    }

    private void showMap(){
        mapFrag.getView().setVisibility(View.VISIBLE);

        mapa.moveCamera(CameraUpdateFactory.newLatLng(polylineOption.getPoints().get(0)));
        mapa.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        if (mapa != null){
            mapa.addPolyline(polylineOption);
        }

    }

    private void invisibleMap(){
        mapFrag.getView().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

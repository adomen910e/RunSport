package com.example.domenger.runsport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Course extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {

    private int id_user;
    protected DbHandler dbHandler;

    private String date;

    private boolean firs_iteration = true;

    private String data_course;
    private ArrayList<Float> vitesse_moy = new ArrayList<>();
    private ArrayList<Polyline> lineArray = new ArrayList<>();
    private float vitesse_max = 0;
    private float moyenne = 0;
    private float distance = 0;
    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView txtContinueLocation;
    private StringBuilder stringBuilder;
    private Button btnContinueLocation;
    private Button btnEndLocation;
    private boolean clear_marker;

    private boolean isContinue = false;
    private boolean isGPS = false;

    private GoogleMap mMap;
    private MarkerOptions markerOption;
    private PolylineOptions polylineOption;
    private Polyline line;
    private Marker markerOnMap;


    private long startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        Bundle b;
        b = getIntent().getExtras();

        assert b != null;
        id_user = b.getInt("id_profil");

        dbHandler = new DbHandler(Course.this);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());

        this.txtContinueLocation = findViewById(R.id.txtContinueLocation);
        btnContinueLocation = findViewById(R.id.btnContinueLocation);
        btnEndLocation = findViewById(R.id.btnEndLocation);
        clear_marker = false;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(2 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            isGPS = isGPSEnable;
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Location loc1 = new Location("");
                        loc1.setLatitude(wayLatitude);
                        loc1.setLongitude(wayLongitude);

                        float speed = 0;
                        if (!firs_iteration) {
                            distance = distance + loc1.distanceTo(location);
                            if (distance == 0) {
                                vitesse_moy.add((float) 0);
                                speed = 0;
                            } else {
                                vitesse_moy.add(location.getSpeed());
                                speed = location.getSpeed();
                            }

                            if (polylineOption == null){
                                polylineOption = new PolylineOptions()
                                        .add(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .width(8)
                                        .color(Color.BLUE);
                            }
                            line = mMap.addPolyline(polylineOption.add(new LatLng(location.getLatitude(), location.getLongitude())));

                        } else {
                            firs_iteration = false;
                        }

                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();

                        data_course = data_course + "(" + wayLatitude + ";" + wayLongitude + ")";
                        stringBuilder = new StringBuilder();

                        stringBuilder.append("Lat: ");
                        stringBuilder.append(wayLatitude);
                        stringBuilder.append("   ");
                        stringBuilder.append("Lon: ");
                        stringBuilder.append(wayLongitude);
                        stringBuilder.append("\n");
                        stringBuilder.append(" Vitesse: ");
                        stringBuilder.append(speed);
                        stringBuilder.append(" Distance: ");
                        stringBuilder.append(distance);
                        stringBuilder.append("\n");

                        if (!firs_iteration && speed > vitesse_max) {
                            vitesse_max = speed;
                        }


                        txtContinueLocation.setText(stringBuilder.toString());
                        addMarker();

                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        btnContinueLocation.setOnClickListener(v -> {
            if (!isGPS) {
                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                return;
            }
            if (clear_marker) {
                mMap.clear();
                vitesse_moy.clear();
                vitesse_max = 0;
                distance = 0;
            }

            startTime = System.currentTimeMillis();
            this.btnContinueLocation.setBackgroundColor(0xFF48FF00);
            this.btnContinueLocation.setTextColor(0xFF000000);
            this.btnEndLocation.setBackgroundColor(0xFF48FF00);
            this.btnEndLocation.setTextColor(0xFF000000);
            isContinue = true;
            getLocation();
        });

        btnEndLocation.setOnClickListener(v -> {
            if (!isGPS) {
                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                return;
            }

            this.btnContinueLocation.setBackgroundColor(0xFFD50000);
            this.btnContinueLocation.setTextColor(0xFFFFFFFF);
            this.btnContinueLocation.setText("Nouvelle course");
            this.btnEndLocation.setBackgroundColor(0xFFD50000);
            this.btnEndLocation.setTextColor(0xFFFFFFFF);

            clear_marker = true;

            float somme = 0;
            for (int i = 0; i < vitesse_moy.size(); i++) {
                somme = somme + vitesse_moy.get(i);
            }
            moyenne = somme / vitesse_moy.size();

            isContinue = false;
            stringBuilder = new StringBuilder();
            getLocation();

            long difference = System.currentTimeMillis() - startTime;


            //TODO: IL FAUT METTRE LES POINTS DANS LA BDD
            String array_point = parsePointLine();

            dbHandler.insertCoursesDetails(id_user, Float.toString(vitesse_max),
                    Float.toString(moyenne), Float.toString(distance), Long.toString(difference / 1000), date, array_point);

            AlertDialog alertDialog = new AlertDialog.Builder(Course.this).create();
            alertDialog.setTitle("Enregistrement course");
            alertDialog.setMessage("Votre course a bien été enregistrée");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(Course.this, Acceuil.class);
                        intent.putExtra("id_profil", id_user);
                        startActivity(intent);
                        finish();
                    });
            alertDialog.show();
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String parsePointLine() {
        String str_point = "";
        List <LatLng> array_point = line.getPoints();

        for (int i=0; i<array_point.size(); i++){
            str_point = str_point + "|" + array_point.get(i).latitude + ";" + array_point.get(i).longitude;
        }
        return str_point;

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Course.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Course.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Course.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

//    @Override
//    //Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
//        MenuInflater inflater = getMenuInflater();
//        //Instanciation du menu XML spécifier en un objet Menu
//        inflater.inflate(R.menu.menu_acceuil, menu);
//
//        return true;
//    }
//
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //On regarde quel item a été cliqué grâce à son id et on déclenche une action
//        Intent intent;
//        switch (item.getItemId()) {
//            case R.id.profile:
//                Toast.makeText(Course.this, "Profile", Toast.LENGTH_SHORT).show();
//                intent = new Intent(Course.this, Profil.class);
//                startActivity(intent);
//            case R.id.option_course:
//                Toast.makeText(Course.this, "Course", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.demarrer:
//                Toast.makeText(Course.this, "Demarrer", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.stats:
//                Toast.makeText(Course.this, "Stats", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.quitter:
//                //Pour fermer l'application il suffit de faire finish()
//                finish();
//                return true;
//        }
//        return false;
//    }


    public void addMarker() {
        LatLng pos = new LatLng(wayLatitude, wayLongitude);

        if (markerOnMap == null){
            markerOption = new MarkerOptions().position(pos).title("Moi");
            markerOnMap = mMap.addMarker(markerOption);
        }else{
            markerOnMap.setPosition(pos);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        CameraPosition.Builder builder = CameraPosition.builder(mMap.getCameraPosition());
        builder.target(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
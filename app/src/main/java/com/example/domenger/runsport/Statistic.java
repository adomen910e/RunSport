package com.example.domenger.runsport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.HashMap;

public class Statistic extends AppCompatActivity {

    private DbHandler db;
    private int index_profil;
    private ArrayList<HashMap<String, String>> courses;
    private boolean isSelectMax = true;
    private boolean isSelectMoy = true;
    private boolean isSelectDist = true;
    private boolean isSelectTime = true;
    private GraphView graph;
    private BarGraphSeries<DataPoint> maxSeries ;
    private BarGraphSeries<DataPoint> moySeries ;
    private BarGraphSeries<DataPoint> distSeries;
    private BarGraphSeries<DataPoint> timeSeries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Bundle b;
        b = getIntent().getExtras();

        //get the id of the profile
        index_profil = b.getInt("id_profil");

        db = new DbHandler(this);
        courses = db.GetCoursesFromUser(index_profil);

        graph = (GraphView) findViewById(R.id.graph);
        initGraphWithCourses(graph);


        Button btnMax = findViewById(R.id.b_max);
        btnMax.setOnClickListener(v -> {
            if (isSelectMax){
                isSelectMax = false;
                graph.removeSeries(maxSeries);
                btnMax.setBackgroundColor(getResources().getColor(R.color.colorOf));

            }else{
                isSelectMax = true;
                graph.addSeries(maxSeries);
                btnMax.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnMoy = findViewById(R.id.b_moy);
        btnMoy.setOnClickListener(v -> {
            if (isSelectMoy){
                isSelectMoy = false;
                graph.removeSeries(moySeries);
                btnMoy.setBackgroundColor(getResources().getColor(R.color.colorOf));

            }else{
                isSelectMoy = true;
                graph.addSeries(moySeries);
                btnMoy.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnDist = findViewById(R.id.b_dist);
        btnDist.setOnClickListener(v -> {
            if (isSelectDist){
                isSelectDist = false;
                graph.removeSeries(distSeries);
                btnDist.setBackgroundColor(getResources().getColor(R.color.colorOf));

            }else{
                isSelectDist = true;
                graph.addSeries(distSeries);
                btnDist.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnTime = findViewById(R.id.b_time);
        btnTime.setOnClickListener(v -> {
            if (isSelectTime){
                isSelectTime = false;
                graph.removeSeries(timeSeries);
                btnTime.setBackgroundColor(getResources().getColor(R.color.colorOf));

            }else{
                isSelectTime = true;
                graph.addSeries(timeSeries);
                btnTime.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });
    }

    public void initGraphWithCourses(GraphView graph) {

            DataPoint max[] = new DataPoint[courses.size()+3];
            DataPoint moy[] = new DataPoint[courses.size()+3];
            DataPoint dist[] = new DataPoint[courses.size()+3];
            DataPoint time[] = new DataPoint[courses.size()+3];

            max[0] = new DataPoint(-10, 0);
            moy[0] = new DataPoint(-10, 0);
            dist[0] = new DataPoint(-10, 0);
            time[0] = new DataPoint(-10, 0);

            max[1] = new DataPoint(0, 0);
            moy[1] = new DataPoint(0, 0);
            dist[1] = new DataPoint(0, 0);
            time[1] = new DataPoint(0, 0);



            for (int i = 0; i < courses.size(); i++) {
                float value_speed_max = Float.parseFloat(courses.get(i).get("speedmax"));
                //Vitesse max 50 km/h
                float VITESSE_MAX_CIRCLE = 50;
                int Y_max = (int) (value_speed_max * 100 / VITESSE_MAX_CIRCLE);
                max[i+2] = new DataPoint(i+1, Y_max);

                float value_speed_moy = Float.parseFloat(courses.get(i).get("speedmoy"));
                //Vitesse moyenne 50km/h
                float VITESSE_MOY_CIRCLE = 50;
                int Y_moy = (int) (value_speed_moy * 100 / VITESSE_MOY_CIRCLE);
                moy[i+2] = new DataPoint(i+1, Y_moy);

                float value_dist = Float.parseFloat(courses.get(i).get("distance"));
                //Distance max 50 km
                float DISTANCE_CIRCLE = 50;
                int Y_dist = (int) (value_dist / 10 / DISTANCE_CIRCLE);
                dist[i+2] = new DataPoint(i+1, Y_dist);

                float value_time = Float.parseFloat(courses.get(i).get("time"));
                //Temps de base max 1 heure
                float TIME_CIRCLE = 3600;
                int Y_time = (int) ((value_time * 100) / TIME_CIRCLE);
                time[i+2] = new DataPoint(i+1, Y_time);
            }

            max[courses.size()+2] = new DataPoint(100, 0);
            moy[courses.size()+2] = new DataPoint(100, 0);
            dist[courses.size()+2] = new DataPoint(100, 0);
            time[courses.size()+2] = new DataPoint(100, 0);

            maxSeries = new BarGraphSeries<>(max);
            moySeries = new BarGraphSeries<>(moy);
            distSeries = new BarGraphSeries<>(dist);
            timeSeries = new BarGraphSeries<>(time);

            maxSeries.setSpacing(40);
            maxSeries.setAnimated(true);
            maxSeries.setColor(Color.RED);
            graph.addSeries(maxSeries);

            moySeries.setSpacing(40);
            moySeries.setAnimated(true);
            moySeries.setColor(Color.CYAN);
            graph.addSeries(moySeries);

            distSeries.setSpacing(40);
            distSeries.setAnimated(true);
            distSeries.setColor(Color.MAGENTA);
            graph.addSeries(distSeries);

            timeSeries.setSpacing(40);
            timeSeries.setAnimated(true);
            timeSeries.setColor(Color.GREEN);
            graph.addSeries(timeSeries);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(6);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScalable(true);

            maxSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(graph.getContext(), "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

            moySeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(graph.getContext(), "Series2: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

            distSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(graph.getContext(), "Series3: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

            timeSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(graph.getContext(), "Series4: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

            // legend
            maxSeries.setTitle("vitesse max");
            moySeries.setTitle("vitesse moyenne");
            distSeries.setTitle("distance");
            timeSeries.setTitle("temps");

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
//    }
    }

    public int get_percent_max(int value){
        return 0;

    }

    public int get_percent_moy(int value){
        return 0;
    }

    public int get_percent_dist(int value){
        return 0;
    }

    public int get_percent_temps(int value){
        return 0;
    }

    public void initGraph(GraphView graph) {



        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                //LIMITS SCROLLING
                new DataPoint(-10, 0),
                new DataPoint(100, 0),

                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 5),
                new DataPoint(6, 3),
                new DataPoint(7, 2),
                new DataPoint(8, 6)
        });
        series.setSpacing(40);
        series.setAnimated(true);
        graph.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 4),
                new DataPoint(4, 1),
                new DataPoint(5, 5),
                new DataPoint(6, 3),
                new DataPoint(7, 2),
                new DataPoint(8, 6)
        });
        series2.setColor(Color.RED);
        series2.setSpacing(40);
        series2.setAnimated(true);
        graph.addSeries(series2);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 5),
                new DataPoint(6, 3),
                new DataPoint(7, 2),
                new DataPoint(8, 6)
        });
        series3.setColor(Color.GREEN);
        series3.setSpacing(40);
        series3.setAnimated(true);
        graph.addSeries(series3);

        BarGraphSeries<DataPoint> series4 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 5),
                new DataPoint(6, 3),
                new DataPoint(7, 2),
                new DataPoint(8, 6)
        });
        series4.setColor(Color.MAGENTA);
        series4.setSpacing(40);
        series4.setAnimated(true);
        graph.addSeries(series4);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(), "Series2: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        series3.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(), "Series3: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        series4.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(), "Series4: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        // legend
        series.setTitle("vitesse max");
        series2.setTitle("vitesse moyenne");
        series3.setTitle("distance");
        series4.setTitle("temps");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

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
                Toast.makeText(Statistic.this, "Profile", Toast.LENGTH_SHORT).show();
                intent = new Intent(Statistic.this, Profil.class);
                intent.putExtra("id_profil", this.index_profil);
                startActivity(intent);
                finish();
            case R.id.option_course:
                Toast.makeText(Statistic.this, "Course", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.demarrer:
                Toast.makeText(Statistic.this, "Demarrer", Toast.LENGTH_SHORT).show();
                intent = new Intent(Statistic.this, Course.class);
                intent.putExtra("id_profil", this.index_profil);
                startActivity(intent);
                finish();
                return true;
            case R.id.stats:
                Toast.makeText(Statistic.this, "Statistiques", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.quitter:
                //Pour fermer l'application il suffit de faire finish()
                finish();
                return true;
        }
        return false;
    }


}

package com.example.domenger.runsport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class Statistic extends AppCompatActivity {

    private int index_profil;
    private ArrayList<HashMap<String, String>> courses;
    private boolean isSelectMax = true;
    private boolean isSelectMoy = true;
    private boolean isSelectDist = true;
    private boolean isSelectTime = true;
    private boolean isSelectDay = false;
    private boolean isSelectAll = true;
    private GraphView graphAll;

    private BarGraphSeries<DataPoint> maxSeriesAll;
    private BarGraphSeries<DataPoint> moySeriesAll;
    private BarGraphSeries<DataPoint> distSeriesAll;
    private BarGraphSeries<DataPoint> timeSeriesAll;

    private BarGraphSeries<DataPoint> maxSeriesDay;
    private BarGraphSeries<DataPoint> moySeriesDay;
    private BarGraphSeries<DataPoint> distSeriesDay;
    private BarGraphSeries<DataPoint> timeSeriesDay;

    private Button btnDay;
    private Button btnAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Bundle b;
        b = getIntent().getExtras();

        //get the id of the profile
        assert b != null;
        index_profil = b.getInt("id_profil");

        DbHandler db = new DbHandler(this);
        courses = db.GetCoursesFromUser(index_profil);

        graphAll = findViewById(R.id.graph);
        initGraphWithCourses(graphAll);
        createSeriesDay();


        Button btnMax = findViewById(R.id.b_max);
        btnMax.setOnClickListener(v -> {
            if (isSelectMax) {
                isSelectMax = false;
                if (isSelectAll) {
                    graphAll.removeSeries(maxSeriesAll);
                } else {
                    graphAll.removeSeries(maxSeriesDay);
                }
                btnMax.setBackgroundColor(getResources().getColor(R.color.colorOf));

            } else {
                isSelectMax = true;
                if (isSelectAll) {
                    graphAll.addSeries(maxSeriesAll);
                } else {
                    graphAll.addSeries(maxSeriesDay);
                }
                btnMax.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnMoy = findViewById(R.id.b_moy);
        btnMoy.setOnClickListener(v -> {
            if (isSelectMoy) {
                isSelectMoy = false;
                if (isSelectAll) {
                    graphAll.removeSeries(moySeriesAll);
                } else {
                    graphAll.removeSeries(moySeriesDay);
                }
                btnMoy.setBackgroundColor(getResources().getColor(R.color.colorOf));

            } else {
                isSelectMoy = true;
                if (isSelectAll) {
                    graphAll.addSeries(moySeriesAll);
                } else {
                    graphAll.addSeries(moySeriesDay);
                }
                btnMoy.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnDist = findViewById(R.id.b_dist);
        btnDist.setOnClickListener(v -> {
            if (isSelectDist) {
                isSelectDist = false;
                if (isSelectAll) {
                    graphAll.removeSeries(distSeriesAll);
                } else {
                    graphAll.removeSeries(distSeriesDay);
                }
                btnDist.setBackgroundColor(getResources().getColor(R.color.colorOf));

            } else {
                isSelectDist = true;
                if (isSelectAll) {
                    graphAll.addSeries(distSeriesAll);
                } else {
                    graphAll.addSeries(distSeriesDay);
                }
                btnDist.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        Button btnTime = findViewById(R.id.b_time);
        btnTime.setOnClickListener(v -> {
            if (isSelectTime) {
                isSelectTime = false;
                if (isSelectAll) {
                    graphAll.removeSeries(timeSeriesAll);
                } else {
                    graphAll.removeSeries(timeSeriesDay);
                }
                btnTime.setBackgroundColor(getResources().getColor(R.color.colorOf));

            } else {
                isSelectTime = true;
                if (isSelectAll) {
                    graphAll.addSeries(timeSeriesAll);
                } else {
                    graphAll.addSeries(timeSeriesDay);
                }
                btnTime.setBackgroundColor(getResources().getColor(R.color.colorOn));
            }

        });

        btnDay = findViewById(R.id.b_day);
        btnDay.setOnClickListener(v -> {
            if (!isSelectDay) {
                isSelectAll = false;
                isSelectDay = true;
                btnDay.setBackgroundColor(getResources().getColor(R.color.colorOn));
                btnAll.setBackgroundColor(getResources().getColor(R.color.colorOf));

                graphAll.removeSeries(distSeriesAll);
                graphAll.removeSeries(maxSeriesAll);
                graphAll.removeSeries(moySeriesAll);
                graphAll.removeSeries(timeSeriesAll);

                if (isSelectDist) {
                    graphAll.addSeries(distSeriesDay);
                }

                if (isSelectMax) {
                    graphAll.addSeries(maxSeriesDay);
                }

                if (isSelectMoy) {
                    graphAll.addSeries(moySeriesDay);
                }

                if (isSelectTime) {
                    graphAll.addSeries(timeSeriesDay);
                }
            }

        });

        btnAll = findViewById(R.id.b_all);
        btnAll.setOnClickListener(v -> {
            if (!isSelectAll) {
                isSelectDay = false;
                isSelectAll = true;
                btnAll.setBackgroundColor(getResources().getColor(R.color.colorOn));
                btnDay.setBackgroundColor(getResources().getColor(R.color.colorOf));

                graphAll.removeSeries(distSeriesDay);
                graphAll.removeSeries(maxSeriesDay);
                graphAll.removeSeries(moySeriesDay);
                graphAll.removeSeries(timeSeriesDay);

                if (isSelectDist) {
                    graphAll.addSeries(distSeriesAll);
                }

                if (isSelectMax) {
                    graphAll.addSeries(maxSeriesAll);
                }

                if (isSelectMoy) {
                    graphAll.addSeries(moySeriesAll);
                }

                if (isSelectTime) {
                    graphAll.addSeries(timeSeriesAll);
                }
            }

        });
    }

    public void createSeriesDay() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        DataPoint[] maxDay = new DataPoint[courses.size() + 3];
        DataPoint[] moyDay = new DataPoint[courses.size() + 3];
        DataPoint[] distDay = new DataPoint[courses.size() + 3];
        DataPoint[] timeDay = new DataPoint[courses.size() + 3];

        maxDay[0] = new DataPoint(-10, 0);
        moyDay[0] = new DataPoint(-10, 0);
        distDay[0] = new DataPoint(-10, 0);
        timeDay[0] = new DataPoint(-10, 0);

        maxDay[1] = new DataPoint(0, 0);
        moyDay[1] = new DataPoint(0, 0);
        distDay[1] = new DataPoint(0, 0);
        timeDay[1] = new DataPoint(0, 0);

        int comptDay = 2;

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).get("date").equals(date)) {
                float value_speed_max = Float.parseFloat(courses.get(i).get("speedmax"));
                float value_speed_moy = Float.parseFloat(courses.get(i).get("speedmoy"));
                float value_dist = Float.parseFloat(courses.get(i).get("distance"));
                float value_time = Float.parseFloat(courses.get(i).get("time"));

                maxDay[comptDay] = new DataPoint(comptDay - 1, value_speed_max);
                moyDay[comptDay] = new DataPoint(comptDay - 1, value_speed_moy);
                distDay[comptDay] = new DataPoint(comptDay - 1, value_dist / 1000);
                timeDay[comptDay] = new DataPoint(comptDay - 1, value_time / 60);

                comptDay++;
            }
        }

        maxDay[comptDay] = new DataPoint(100, 0);
        moyDay[comptDay] = new DataPoint(100, 0);
        distDay[comptDay] = new DataPoint(100, 0);
        timeDay[comptDay] = new DataPoint(100, 0);

        maxSeriesDay = new BarGraphSeries<>(Arrays.copyOfRange(maxDay, 0, comptDay + 1));
        moySeriesDay = new BarGraphSeries<>(Arrays.copyOfRange(moyDay, 0, comptDay + 1));
        distSeriesDay = new BarGraphSeries<>(Arrays.copyOfRange(distDay, 0, comptDay + 1));
        timeSeriesDay = new BarGraphSeries<>(Arrays.copyOfRange(timeDay, 0, comptDay + 1));

        maxSeriesDay.setSpacing(40);
        maxSeriesDay.setAnimated(true);
        maxSeriesDay.setColor(Color.RED);

        moySeriesDay.setSpacing(40);
        moySeriesDay.setAnimated(true);
        moySeriesDay.setColor(Color.CYAN);

        distSeriesDay.setSpacing(40);
        distSeriesDay.setAnimated(true);
        distSeriesDay.setColor(Color.MAGENTA);

        timeSeriesDay.setSpacing(40);
        timeSeriesDay.setAnimated(true);
        timeSeriesDay.setColor(Color.GREEN);

        maxSeriesDay.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graphAll.getContext(), "Vitesse Maximale: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        moySeriesDay.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graphAll.getContext(), "Vitesse Moyenne: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        distSeriesDay.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graphAll.getContext(), "Distance Parcourue: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        timeSeriesDay.setOnDataPointTapListener((series, dataPoint) -> {
            LocalTime timeOfDay = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeOfDay = LocalTime.ofSecondOfDay((long) (dataPoint.getY() * 60));
            }
            assert timeOfDay != null;
            String time = timeOfDay.toString();

            Toast.makeText(graphAll.getContext(), "Temps Écoulé: " + time, Toast.LENGTH_SHORT).show();
        });

        // Legend
        maxSeriesDay.setTitle("vitesse max");
        moySeriesDay.setTitle("vitesse moyenne");
        distSeriesDay.setTitle("distance");
        timeSeriesDay.setTitle("temps");

    }

    public void initGraphWithCourses(GraphView graph) {

        DataPoint[] maxAll = new DataPoint[courses.size() + 3];
        DataPoint[] moyAll = new DataPoint[courses.size() + 3];
        DataPoint[] distAll = new DataPoint[courses.size() + 3];
        DataPoint[] timeAll = new DataPoint[courses.size() + 3];

        maxAll[0] = new DataPoint(-10, 0);
        moyAll[0] = new DataPoint(-10, 0);
        distAll[0] = new DataPoint(-10, 0);
        timeAll[0] = new DataPoint(-10, 0);

        maxAll[1] = new DataPoint(0, 0);
        moyAll[1] = new DataPoint(0, 0);
        distAll[1] = new DataPoint(0, 0);
        timeAll[1] = new DataPoint(0, 0);


        for (int i = 0; i < courses.size(); i++) {
            float value_speed_max = Float.parseFloat(courses.get(i).get("speedmax"));
            maxAll[i + 2] = new DataPoint(i + 1, value_speed_max);

            float value_speed_moy = Float.parseFloat(courses.get(i).get("speedmoy"));
            moyAll[i + 2] = new DataPoint(i + 1, value_speed_moy);

            float value_dist = Float.parseFloat(courses.get(i).get("distance"));
            distAll[i + 2] = new DataPoint(i + 1, value_dist / 1000);

            float value_time = Float.parseFloat(courses.get(i).get("time"));
            timeAll[i + 2] = new DataPoint(i + 1, value_time / 60);

        }

        maxAll[courses.size() + 2] = new DataPoint(100, 0);
        moyAll[courses.size() + 2] = new DataPoint(100, 0);
        distAll[courses.size() + 2] = new DataPoint(100, 0);
        timeAll[courses.size() + 2] = new DataPoint(100, 0);

        maxSeriesAll = new BarGraphSeries<>(maxAll);
        moySeriesAll = new BarGraphSeries<>(moyAll);
        distSeriesAll = new BarGraphSeries<>(distAll);
        timeSeriesAll = new BarGraphSeries<>(timeAll);

        maxSeriesAll.setSpacing(40);
        maxSeriesAll.setAnimated(true);
        maxSeriesAll.setColor(Color.RED);
        graph.addSeries(maxSeriesAll);

        moySeriesAll.setSpacing(40);
        moySeriesAll.setAnimated(true);
        moySeriesAll.setColor(Color.CYAN);
        graph.addSeries(moySeriesAll);

        distSeriesAll.setSpacing(40);
        distSeriesAll.setAnimated(true);
        distSeriesAll.setColor(Color.MAGENTA);
        graph.addSeries(distSeriesAll);

        timeSeriesAll.setSpacing(40);
        timeSeriesAll.setAnimated(true);
        timeSeriesAll.setColor(Color.GREEN);
        graph.addSeries(timeSeriesAll);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);

        maxSeriesAll.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graph.getContext(), "Vitesse Maximale: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        moySeriesAll.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graph.getContext(), "Vitesse Moyenne: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        distSeriesAll.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(graph.getContext(), "Distance Parcourue: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        timeSeriesAll.setOnDataPointTapListener((series, dataPoint) -> {
            LocalTime timeOfDay = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeOfDay = LocalTime.ofSecondOfDay((long) (dataPoint.getY() * 60));
            }
            assert timeOfDay != null;
            String time = timeOfDay.toString();

            Toast.makeText(graph.getContext(), "Temps Écoulé: " + time, Toast.LENGTH_SHORT).show();
        });

        // legend
        maxSeriesAll.setTitle("vitesse max");
        moySeriesAll.setTitle("vitesse moyenne");
        distSeriesAll.setTitle("distance");
        timeSeriesAll.setTitle("temps");

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

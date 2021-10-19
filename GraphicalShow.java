package com.fitness.tracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;

public class GraphicalShow extends AppCompatActivity {
    DatabaseReference myRef;

    double heart_beat = 0;
    double step = 0.0;
    double bmi = 0;
    double calorie = 0;
    double heart_beat1 = 0;
    double step1 = 0.0;
    double bmi1 = 0;
    double calorie1 = 0;
    SharedPreferences preferences;
    int total = 0;
    GraphView graph;
    BarGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphical_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        graph = findViewById(R.id.graph);

        series = new BarGraphSeries<DataPoint>(new DataPoint[]{

        });
        //  Viewport vp = graph.getViewport();
        //vp.setXAxisBoundsManual(true);
        //vp.setMinX(0);
        //vp.setMaxX(1000);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        myRef = FirebaseDatabase.getInstance().getReference(Constant.DB);
        myRef.child(Constant.user_profile).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
int count=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i("GRAPH", "onDataChange: " + dataSnapshot1.getKey() + "::" + dataSnapshot1.getValue());
                    if (preferences.getString("userid", "").equalsIgnoreCase(dataSnapshot1.getKey())) {
                        heart_beat = dataSnapshot1.child(Constant.heart_rate).getValue(Double.class);
                        step = dataSnapshot1.child(Constant.steps).getValue(Double.class);
                        calorie = dataSnapshot1.child(Constant.calorie).getValue(Double.class);
                        bmi = dataSnapshot1.child(Constant.bmi).getValue(Double.class);
                    } else {
                        total++;
                        heart_beat1 = heart_beat1 + dataSnapshot1.child(Constant.heart_rate).getValue(Double.class);
                        step1 = step1 + dataSnapshot1.child(Constant.steps).getValue(Double.class);
                        calorie1 = calorie1 + dataSnapshot1.child(Constant.calorie).getValue(Double.class);
                        bmi1 = bmi1 + dataSnapshot1.child(Constant.bmi).getValue(Double.class);

                    }
                    count++;
                    Log.i(TAG, "onDataChange: "+count+"::ll"+dataSnapshot.getChildrenCount());
                    if(count==dataSnapshot.getChildrenCount()){
                        Log.i(TAG, "onDataChange: " + heart_beat + "::" + (heart_beat1 / total));
                        DataPoint dataPoint = new DataPoint(1, heart_beat);
                        DataPoint dataPoint1 = new DataPoint(2, heart_beat1 / total);
                        DataPoint dataPoint2 = new DataPoint(3, bmi);
                        DataPoint dataPoint3 = new DataPoint(4, bmi / total);
                        DataPoint dataPoint4 = new DataPoint(5, calorie);
                        DataPoint dataPoint5 = new DataPoint(6, calorie1 / total);
                        DataPoint dataPoint6 = new DataPoint(7, step);
                        DataPoint dataPoint7 = new DataPoint(8, step1 / total);
                        DataPoint dataPoint8 = new DataPoint(9, 0);


                        series.appendData(dataPoint, true, 200);
                        series.appendData(dataPoint1, true, 200);
                        series.appendData(dataPoint2, true, 100);
                        series.appendData(dataPoint3, true, 100);
                        series.appendData(dataPoint4, true, 3000);
                        series.appendData(dataPoint5, true, 3000);
                        series.appendData(dataPoint6, true, 10000);
                        series.appendData(dataPoint7, true, 10000);
                        series.appendData(dataPoint8, true, 200);
                        //   series.setTitle("Heart Rate");

                        // styling
                        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                            @Override
                            public int get(DataPoint data) {
                                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                            }
                        });

                        series.setSpacing(10);
                        series.setAnimated(true);

                        // draw values on top
                        series.setDrawValuesOnTop(true);
                        series.setValuesOnTopColor(Color.RED);
                        //series.setValuesOnTopSize(50);

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                switch ((int) dataPoint.getX()) {
                                    case 1:
                                        Toast.makeText(graph.getContext(), "Your Heart Rate:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;
                                    case 2:
                                        Toast.makeText(graph.getContext(), "Average Heart Rate:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;
                                    case 3:
                                        Toast.makeText(graph.getContext(), "Your BMI:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;

                                    case 4:
                                        Toast.makeText(graph.getContext(), "Average BMI:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;
                                    case 5:
                                        Toast.makeText(graph.getContext(), "Your Calorie:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                                        break;
                                    case 6:
                                        Toast.makeText(graph.getContext(), "Average Calorie:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                                        break;
                                    case 7:
                                        Toast.makeText(graph.getContext(), "Your Steps:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;
                                    case 8:
                                        Toast.makeText(graph.getContext(), "Average  Steps:" + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                                        break;
                                    default:
                                        Toast.makeText(GraphicalShow.this, "Please tap on bar graph" + dataPoint, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        try {
                            graph.addSeries(series);
                        } catch (Exception e) {
                        }
                    }
                    
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//        });


    }

    String TAG = "GRAPH";
}

package com.example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView sensorListTextView;
    Map<Sensor, Boolean> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = new LinkedHashMap<>();
        sensorListTextView = findViewById(R.id.sensorListTextView);
        double start_time = System.currentTimeMillis();
        System.out.println(start_time);

        // Get the list of sensors
        Map<Sensor,Boolean> allSensors = getSensorList();
//        Collections.sort(allSensors, new Comparator<Sensor>() {
//            @Override
//            public int compare(final Sensor a, final Sensor b) {
//                return (a.toString().compareTo(b.toString()));
//            }
//        });

        double last_time = System.currentTimeMillis();
        System.out.println("last_time: " + last_time);

        // Display the list of sensors
        displaySensorList(allSensors);
    }



    private Map<Sensor, Boolean> getSensorList() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> unfiltered = new ArrayList<>(sensorManager.getSensorList(Sensor.TYPE_ALL));


        for(Sensor s1 : unfiltered){

            boolean isThreeAxes = isXYZ(s1);
            addToMap(s1,isThreeAxes);
        }

        return result;
    }

    public void addToMap( Sensor s1,boolean isThreeAxes){

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || !isTriggerSensor(s1)) {
                result.put(s1,isThreeAxes);
            }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isXYZ(Sensor s) {
        switch (s.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_LINEAR_ACCELERATION:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_ROTATION_VECTOR:
                return(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (s.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR
                    || s.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED
                    || s.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
                return(true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (s.getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
                return(true);
            }
        }
        return(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean isTriggerSensor(Sensor sensor) {
        // Add your logic to check if the sensor is a trigger sensor
        // For example, you can check the sensor type or other properties
//        return sensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION;
        return sensor.getName().toLowerCase().contains("trigger");
    }

    private void displaySensorList(Map<Sensor, Boolean> sensors) {
        StringBuilder sensorListText = new StringBuilder();

int i = 1;
            for (Map.Entry<Sensor, Boolean> entry : result.entrySet()) {
                sensorListText.append(i+"  :  ").append(entry.getKey() + ":\n---->" + entry.getValue()).append("\n\n");
                System.out.println(entry.getKey() + ":" + entry.getValue());
                i++;
            }

        sensorListTextView.setText(sensorListText.toString());

    }

}
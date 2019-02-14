package com.bodovix.week5scencorsandlocation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView xTxt;
    TextView yTxt;
    TextView zTxt;

    SensorManager sensorManager;
    Sensor accelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xTxt = findViewById(R.id.dataX);
        yTxt = findViewById(R.id.dataY);
        zTxt = findViewById(R.id.dataZ);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        RegisterAcceleromiter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,accelerometer);
    }

    private void RegisterAcceleromiter() {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null){
            sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                    xTxt.setText("X =" + String.valueOf(event.values[0]));
                    yTxt.setText("Y =" + String.valueOf(event.values[1]));
                    zTxt.setText("Z =" + String.valueOf(event.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

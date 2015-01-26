/*MAGIC 8 BALL - MAIN ACTIVITY (WEARABLE)*/
/*Last updated: January 26, 2015*/
/*SensorListener code modified from http://stackoverflow.com/questions/5271448/how-to-detect-shake-event-with-android*/

package com.example.rachel.magic8ball;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class MainActivityWear extends Activity implements SensorEventListener{
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 600;
    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        long curTime = event.timestamp;

        if (curTime-lastUpdate>200 && mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long deltaTime = curTime-lastUpdate;
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[0];
            float z = event.values[0];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / deltaTime * 10000;

            if (speed > SHAKE_THRESHOLD) {

            }

            last_x = x;
            last_y = y;
            last_z = z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_wear);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

package com.dino.dino;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class DrawView$1 implements SensorEventListener {
    final com.dino.dino.DrawView this$0;

    public DrawView$1(DrawView this$0) {
        this.this$0 = this$0;
    }

    @Override
    public void onSensorChanged(SensorEvent paramSensorEvent) {
        if (paramSensorEvent.sensor.getType() == 1)
        {
            this.this$0.UpdateAcc(paramSensorEvent.values[0], paramSensorEvent.values[1], paramSensorEvent.values[2]);
            this.this$0.Update();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

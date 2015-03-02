package dk.aau.cs.psylog.module_lib;

import android.content.Intent;

public interface ISensor{
    public void startSensor();
    public void stopSensor();
    public void sensorParameters(Intent intent);
}

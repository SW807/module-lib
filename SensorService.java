package dk.aau.cs.psylog.module_lib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class SensorService extends Service{

    protected ISensor sensor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        sensor.stopSensor();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setSensor();
    }

	public abstract void setSensor();

    @Override
    public int onStartCommand(Intent intent, int flag, int startid)
    {
        sensor.sensorParameters(intent);
        sensor.startSensor();
        return Service.START_STICKY;
    }
}

package dk.aau.cs.psylog.module_lib;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public abstract class SensorService extends IntentService {
    protected ISensor sensor;

    PowerManager.WakeLock wakeLock;
    public SensorService() {
        super("Abstract SensorService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        sensor.stopSensor();
        wakeLock.release();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setSensor();

    }

	public abstract void setSensor();

    boolean isCanceled = false;
    @Override
    public int onStartCommand(Intent intent, int flag, int startid)
    {
        if(intent.hasExtra("action"))
        {
            isCanceled = intent.getStringExtra("action").equals("cancel");
        }
        sensor.sensorParameters(intent);
        sensor.startSensor();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        super.onStartCommand(intent, flag, startid);
        return Service.START_STICKY;
    }

    Thread t;
    @Override
    protected void onHandleIntent(Intent intent)
    {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isCanceled)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.run();
    }

}
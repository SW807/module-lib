package dk.aau.cs.psylog.module_lib;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.IBinder;
import android.os.PowerManager;

import dk.aau.cs.psylog.sensor.accelerometer.R;

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
            if(isCanceled)
            {
                stopForeground(true);
            }
        }

        sensor.sensorParameters(intent);
        sensor.startSensor();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        super.onStartCommand(intent, flag, startid);
        if(!isCanceled)
        {
            Notification notification = new Notification(R.drawable.ic_launcher, "the service description",
                    System.currentTimeMillis());
            Intent notificationIntent = new Intent(this, SensorService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.setLatestEventInfo(this, "some info",
                    "some notification message", pendingIntent);
            startForeground(startid+1, notification);
        }

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
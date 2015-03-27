package dk.aau.cs.psylog.module_lib;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class ScheduledService extends IntentService {
    protected IScheduledTask scheduledTask;

    /**
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ScheduledService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        scheduledTask.setParameters(intent);
        scheduledTask.doTask();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setScheduledTask();
    }

    public abstract void setScheduledTask();
}

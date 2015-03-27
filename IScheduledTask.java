package dk.aau.cs.psylog.module_lib;

import android.content.Intent;

public interface IScheduledTask {
    public void doTask();
    public void setParameters(Intent i);
}

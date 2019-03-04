package com.dm.crmdm_app;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.util.Log;

/**
 * Created by dataman on 6/12/2017.
 */

@SuppressLint("NewApi")
public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        /*Log.d(TAG, "Performing long running task in scheduled job");
        return false;*/


        Log.i("FJD.DemoJobService", "onStartJob called");

        PersistableBundle extras = jobParameters.getExtras();
        assert extras != null;

        int result = extras.getInt("return");

       // CentralContainer.getStore(getApplicationContext()).recordResult(jobParameters, result);

        return false; // No more work to do
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}

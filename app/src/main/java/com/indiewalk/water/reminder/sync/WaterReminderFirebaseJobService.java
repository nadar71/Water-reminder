
package com.indiewalk.water.reminder.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class WaterReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    /**
     * ---------------------------------------------------------------------------------------------
     * Job entry point.
     * TODO : check if an IntentService is better (weare on main thread?)
     *
     * @return whether there is more work remaining.
     * ---------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     * ---------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
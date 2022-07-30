package com.zch.network.cookie;

import android.os.AsyncTask;

/**
 * Abstract base for a AsyncTask with result support that should have any RuntimeExceptions it
 * registered if the app is in dev mode.
 */
public abstract class GuardedResultAsyncTask<Result>
        extends AsyncTask<Void, Void, Result> {


    protected GuardedResultAsyncTask() {

    }

    @Override
    protected final Result doInBackground(Void... params) {
        try {
            return doInBackgroundGuarded();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    protected final void onPostExecute(Result result) {
        try {
            onPostExecuteGuarded(result);
        } catch (RuntimeException e) {
        }
    }

    protected abstract Result doInBackgroundGuarded();

    protected abstract void onPostExecuteGuarded(Result result);

}
package com.zch.network.cookie;

import android.os.AsyncTask;

/**
 * Abstract base for a AsyncTask that should have any RuntimeExceptions it throws
 * the app is in dev mode.
 * <p>
 * This class doesn't allow doInBackground to return a results. If you need this
 * use GuardedResultAsyncTask instead.
 */
public abstract class GuardedAsyncTask<Params, Progress>
        extends AsyncTask<Params, Progress, Void> {

    protected GuardedAsyncTask() {

    }

    @Override
    protected final Void doInBackground(Params... params) {
        try {
            doInBackgroundGuarded(params);
        } catch (RuntimeException e) {
        }
        return null;
    }

    protected abstract void doInBackgroundGuarded(Params... params);
}
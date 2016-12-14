package com.cleverm.smartpen.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cleverm.smartpen.util.execption.NullTimerHandlerException;
import com.cleverm.smartpen.util.execption.TriggerTimerException;

import java.util.Calendar;

/**
 * Created by xiong,An android project Engineer,on 22/4/2016.
 * Data:22/4/2016  下午 01:14
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class TimerPlanUtil {

    private static final String TAG = "AndroidTimer";
    private static final int DEFAULT_INTERVAL = 1000; // 1s

    private Handler mTimerHandler = null;
    private int mTimerIntervalInMilliseconds = DEFAULT_INTERVAL;
    private OnTickListener mListener = null;
    private Boolean mIsTimerRunning = false;

    // Must create TimerPlanUtil instance via Builder
    private TimerPlanUtil() {
    }

    private Runnable mTimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (mTimerHandler == null) {
                NullTimerHandlerException.throwException();
                return;
            }

            if (mListener != null) {
                mListener.onTick(Calendar.getInstance().getTimeInMillis());
            }

            mTimerHandler.postDelayed(mTimerRunnable, mTimerIntervalInMilliseconds);
        }
    };

    /**
     * Start the timer
     */
    public void start() {
        synchronized (mIsTimerRunning) {
            if (mIsTimerRunning) {
                TriggerTimerException.throwTriggerStartingTimerException();
                return;
            } else if (mTimerHandler == null){
                NullTimerHandlerException.throwException();
                return;
            } else {
                mIsTimerRunning = true;
                mTimerHandler.post(mTimerRunnable);
                Log.d(TAG, "Timer is started");
            }
        }
    }

    /**
     * Stop the timer
     */
    public void stop() {
        synchronized (mIsTimerRunning) {
            if (!mIsTimerRunning) {
                TriggerTimerException.throwTriggerStoppingTimerException();
                return;
            } else if (mTimerHandler == null){
                NullTimerHandlerException.throwException();
                return;
            } else {
                mIsTimerRunning = false;
                mTimerHandler.removeCallbacks(mTimerRunnable);
                Log.d(TAG, "Timer is stopped");
            }
        }
    }

    public static class Builder {
        private int mTimerIntervalInMilliseconds = DEFAULT_INTERVAL;
        private Looper mLooper = null;
        private OnTickListener mListener;

        /**
         * Builder AndroidTimer instance basing on properties
         */
        public TimerPlanUtil build() {
            TimerPlanUtil timer = new TimerPlanUtil();
            if (mLooper == null) {
                timer.mTimerHandler = new Handler();
            } else {
                timer.mTimerHandler = new Handler(mLooper);
            }
            timer.mListener = mListener;
            timer.mTimerIntervalInMilliseconds = mTimerIntervalInMilliseconds;
            return timer;
        }

        /**
         * Set listener to builder
         * @param listener the listener will observer onTick event
         * @return current building Builder
         */
        public Builder listener(OnTickListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * Set timer interval to builder
         * @param timerIntervalInMilliseconds timer interval in milliseconds
         * @return current building Builder
         */
        public Builder timerIntervalInMilliseconds(int timerIntervalInMilliseconds) {
            mTimerIntervalInMilliseconds = timerIntervalInMilliseconds;
            return this;
        }

        /**
         * Set timer interval to builder
         * @param timerIntevalInSeconds timer interval in seconds
         * @return current building Builder
         */
        public Builder timerIntervalInSeconds(int timerIntevalInSeconds) {
            mTimerIntervalInMilliseconds = timerIntevalInSeconds * 1000;
            return this;
        }

        /**
         * Set the looper to schedule the timer, if it is null, the default looper will be used
         * @param looper a Looper
         * @return current building Builder
         */
        public Builder looper(Looper looper) {
            mLooper = looper;
            return this;
        }
    }

    public interface OnTickListener {
        public void onTick(long timestampInMilliseconds);
    }


}

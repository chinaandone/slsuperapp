package com.cleverm.smartpen.pushtable;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 95 on 2016/2/19.
 */
public class PushTablePreferencesHelper {
    @SuppressWarnings("unused")
    private static final String TAG = PushTablePreferencesHelper.class.getSimpleName();
    public static final int RETURN_VALUE_INVALID = -1;
    private static final String PREF_NAME = "clever_m";

    private static PushTablePreferencesHelper sInstance;
    private static Object mLock = new Object();
    private Context mContext;

    public static PushTablePreferencesHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new PushTablePreferencesHelper(context);
                }
            }
        }
        return sInstance;
    }

    private PushTablePreferencesHelper(Context context) {
        mContext = context;
    }

    public String getString(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getString(key, null);
        }
        return null;
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public void putString(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putString(key, value);
                editor.commit();
            }
        }
    }

    public int getInt(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getInt(key, RETURN_VALUE_INVALID);
        }
        return RETURN_VALUE_INVALID;
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public void putInt(String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putInt(key, value);
                editor.commit();
            }
        }
    }

    public long getLong(String key, long defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public void putLong(String key, long value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putLong(key, value);
                editor.commit();
            }
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getBoolean(key, defValue);
        }
        return defValue;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putBoolean(key, value);
                editor.commit();
            }
        }
    }

    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mContext.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mContext.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS)
                .unregisterOnSharedPreferenceChangeListener(listener);
    }
}

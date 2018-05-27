/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Utility class to store/retrieve values in SharedPreferences.
 *
 * @author sachin.gupta
 */
public class SharedPrefsUtils {

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static boolean getSharedPrefBoolean(Context pContext, String pFileName, String pKey, boolean pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getBoolean(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefBoolean(Context pContext, String pFileName, String pKey, boolean pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putBoolean(pKey, pValue);
        _editor.commit();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static int getSharedPrefInt(Context pContext, String pFileName, String pKey, int pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getInt(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefInt(Context pContext, String pFileName, String pKey, int pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putInt(pKey, pValue);
        _editor.commit();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static String getSharedPrefString(Context pContext, String pFileName, String pKey, String pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getString(pKey, pDefault);
    }


    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefString(Context pContext, String pFileName, String pKey, String pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putString(pKey, pValue);
        _editor.commit();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static long getSharedPrefLong(Context pContext, String pFileName, String pKey, long pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getLong(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefLong(Context pContext, String pFileName, String pKey, long pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putLong(pKey, pValue);
        _editor.commit();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static float getSharedPrefFloat(Context pContext, String pFileName, String pKey, float pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getFloat(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefFloat(Context pContext, String pFileName, String pKey, float pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putFloat(pKey, pValue);
        _editor.commit();
    }

    /***
     *
     * @param pContext
     * @param pFileName
     */
    public static void clear(Context pContext, String pFileName) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.clear();
        _editor.commit();
    }
}

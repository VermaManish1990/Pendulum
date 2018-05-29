package com.pend.util;

import android.util.Log;

import com.pendulum.application.BaseApplication;

/**
 * Class for printing log
 *
 * @author Faisal Khan
 */
public class LoggerUtil {

    /**
     * Send an ERROR log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */

    public static void e(String tag, String msg) {
        if (BaseApplication.IS_LOG_ENABLE)
            Log.e(tag, msg);
    }

    /**
     * Send a INFO log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (BaseApplication.IS_LOG_ENABLE)
            Log.i(tag, msg);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (BaseApplication.IS_LOG_ENABLE)
            Log.d(tag, msg);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (BaseApplication.IS_LOG_ENABLE)
            Log.v(tag, msg);
    }

}

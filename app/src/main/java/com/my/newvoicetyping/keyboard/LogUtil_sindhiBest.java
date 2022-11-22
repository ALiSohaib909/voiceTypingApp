package com.my.newvoicetyping.keyboard;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil_sindhiBest {

    public static void LogError(String className, String customMessage, Exception ex) {
        Log.d("pakplagetypelog_" + className, customMessage + ": " + getStackTrace(ex));
    }

    public static void LogMessage(String className, String customMessage) {
        Log.d("pakplagetypelog_" + className, customMessage);
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}

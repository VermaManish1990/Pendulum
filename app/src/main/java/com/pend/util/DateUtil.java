package com.pend.util;

import com.pend.interfaces.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * This method will return the difference in no. of years
     * String checkIn checkIn date in string
     *
     * @return difference between given dates and current dates.
     */
    public static String getDifferenceFromCurrentDate(String date) {
        String dateDiff;
        Date date1 = parseDate(Constants.SERVER_DATE_FORMAT, date);
        Date date2 = new Date();

        int difInDays = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
        if (difInDays <= 1) {
            int difInDaysInHour = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60));
            dateDiff = difInDaysInHour + " Hours";
//            dateDiff = "1 Day to go";
        } else if (difInDays < 30) {
            dateDiff = difInDays + " Days";
        } else if (difInDays > 365) {
            int diff = difInDays / 365;
            if (diff == 1) {
                dateDiff = diff + " Year";
            } else {
                dateDiff = diff + " Years";
            }
        } else {
            int diff = difInDays / 30;
            if (diff == 1) {
                dateDiff = diff + " Month";
            } else {
                dateDiff = diff + " Months";
            }
        }
        return dateDiff;
    }

    /**
     * Method to convert given date in String and format to date
     *
     * @param inputFormat like dd/MM/YYYY
     * @param date        String date
     * @return Date object
     */
    public static Date parseDate(String inputFormat, String date) {

        final String TAG = DateUtil.class.getSimpleName();
        LoggerUtil.v(TAG, "parseDate");
        Calendar cal = Calendar.getInstance();
        try {
            if (date != null && inputFormat != null) {
                cal.setTime(new SimpleDateFormat(inputFormat, Locale.US).parse(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            LoggerUtil.e(TAG, "parseDate" + e.toString());
        }
        return cal.getTime();
    }
}

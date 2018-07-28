package com.pend.util;

import android.annotation.SuppressLint;

import com.pend.interfaces.Constants;

import java.text.DateFormatSymbols;
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

    /**
     * Method is used to get Month name by Month number.
     *
     * @param month month
     * @return String
     */
    public String getMonthFullName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    /**
     * Method is used to get short name of month.
     *
     * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
     *                    but here we assumed 1 as a <b>January</b> and 12 as a <b>December</b>.
     * @return String
     */
    public static String getMonthShortName(int monthNumber) {
        String monthName = "";

        if (monthNumber >= 1 && monthNumber <= 12)
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber - 1);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return monthName;
    }

    /**
     * Method is used to get current Month number.
     *
     * @return int -- current Month
     */
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * Method is used to get current Year number.
     *
     * @return int -- current Year
     */
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static String getMonthAndYearName(int month, int year) {
        String date = "";
        date = date + getMonthShortName(month);
        date = date + " " + year;
        return date;
    }
}

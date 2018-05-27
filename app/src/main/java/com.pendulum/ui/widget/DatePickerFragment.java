package com.pendulum.ui.widget;///*
// *
// *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
// *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
// *
// */
//
//package com.com.taxi.ui.widget;
//
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.view.View;
//import android.widget.DatePicker;
//
//import com.bluestar.R;
//import com.com.taxi.ui.Events;
//import com.com.taxi.ui.IScreen;
//
//import java.lang.reflect.Field;
//import java.util.Calendar;
//import java.util.Date;
//
///**
// * @author sachin.gupta
// * @note Either {@link DatePickerFragment#EXTRA_OPEN_WITH_TODAY} should be true <br/>
// * OR <br/>
// * Combination of {@link DatePickerFragment#EXTRA_YEAR},
// * {@link DatePickerFragment#EXTRA_DAY_OF_MONTH},
// * {@link DatePickerFragment#EXTRA_DAY_OF_MONTH} should be valid date.
// */
//public class DatePickerFragment extends DialogFragment implements
//        OnDateSetListener {
//
//    public static final String TAG_DATE_PICKER_DIALOG = "tag_date_picker_dialog";
//    public static final String EXTRA_SHOW_ONLY_MM_YYYY = "extra_show_only_mm_yyyy";
//
//    public static final String EXTRA_OPEN_WITH_TODAY = "extra_open_with_today";
//    public static final String EXTRA_YEAR = "extra_year";
//    public static final String EXTRA_MONTH_INDEX = "extra_month_index";
//    public static final String EXTRA_DAY_OF_MONTH = "extra_day_of_month";
//    public static final String EXTRA_MAKE_TODAY_MAXDATE = "extra_today_max";
//    public static final String EXTRA_SET_MIN_DATE = "extra_set_min";
//    public static final String EXTRA_MIN_DAY = "extra_min_day";
//    public static final String EXTRA_MIN_MONTH_INDEX = "extra_min_month";
//    public static final String EXTRA_MIN_YEAR = "extra_min_year";
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_40);
//
//        int year;
//        int month;
//        int day;
//
//        Bundle arguments = getArguments();
//
//        if (arguments.getBoolean(EXTRA_OPEN_WITH_TODAY)) {
//            Calendar calendar = Calendar.getInstance();
//            year = calendar.get(Calendar.YEAR);
//            month = calendar.get(Calendar.MONTH);
//            day = calendar.get(Calendar.DAY_OF_MONTH);
//        } else {
//            year = arguments.getInt(EXTRA_YEAR);
//            month = arguments.getInt(EXTRA_MONTH_INDEX);
//            day = arguments.getInt(EXTRA_DAY_OF_MONTH);
//        }
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                this, year, month, day);
//
//        if (arguments.getBoolean(EXTRA_MAKE_TODAY_MAXDATE)) {
//            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
//        }
//        if (arguments.getBoolean(EXTRA_SET_MIN_DATE)) {
//            Calendar cal = Calendar.getInstance();
//            cal.set(arguments.getInt(EXTRA_MIN_YEAR), arguments.getInt(EXTRA_MIN_MONTH_INDEX), arguments.getInt(EXTRA_MIN_DAY));
//            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
//        }
//
//        if (arguments.getBoolean(EXTRA_SHOW_ONLY_MM_YYYY)) {
//            try {
//                showMonthAndYearOnly(datePickerDialog);
//            } catch (Exception e) {
//            }
//        }
//        return datePickerDialog;
//    }
//
//    /**
//     * This method is used to show month and year only in case of card issue and
//     * expiry date
//     *
//     * @param datePickerDialog
//     */
//    private void showMonthAndYearOnly(DatePickerDialog datePickerDialog)
//            throws Exception {
//        DatePicker datePicker = null;
//        Field[] datePickerDialogFields = datePickerDialog.getClass()
//                .getDeclaredFields();
//        for (Field datePickerDialogField : datePickerDialogFields) {
//            if (datePickerDialogField.getName().equals("mDatePicker")) {
//                datePickerDialogField.setAccessible(true);
//                datePicker = (DatePicker) datePickerDialogField
//                        .get(datePickerDialog);
//            }
//        }
//        if (datePicker == null) {
//            return;
//        }
//        Field datePickerFields[] = datePicker.getClass().getDeclaredFields();
//        for (Field datePickerField : datePickerFields) {
//            if ("mDayPicker".equals(datePickerField.getName())
//                    || "mDaySpinner".equals(datePickerField.getName())) {
//                datePickerField.setAccessible(true);
//                Object dayPicker = new Object();
//                dayPicker = datePickerField.get(datePicker);
//                ((View) dayPicker).setVisibility(View.GONE);
//            }
//        }
//        datePickerDialog.setTitle("");
//    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear,
//                          int dayOfMonth) {
//        if (getActivity() instanceof IScreen) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, monthOfYear);
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            ((IScreen) getActivity()).onEvent(Events.DATE_PICKER_DIALOG,
//                    calendar);
//        }
//    }
//}

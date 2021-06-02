package com.raza.mealshare.CustomDialogs;

import android.view.View;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import androidx.fragment.app.FragmentManager;

public class ShortCutDialogs {
    public static void  PickDate(Calendar openCalendar, FragmentManager fragmentManager, DateResults dateResults){
        Calendar incomming = openCalendar;
        if (openCalendar==null){
            openCalendar=Calendar.getInstance();
            openCalendar.set(Calendar.YEAR,2005);
        }
        Calendar startTimestamp = Calendar.getInstance();
        startTimestamp.set(Calendar.YEAR,1940);
        Calendar endTimestamp =Calendar.getInstance();
        MaterialDatePicker<Long> materialDatePicker=  MaterialDatePicker.Builder.datePicker().setTitleText("Date Of Birth").setCalendarConstraints(new CalendarConstraints.Builder().setEnd(endTimestamp.getTimeInMillis()).setStart(startTimestamp.getTimeInMillis()).setOpenAt(openCalendar.getTimeInMillis()).build()).build();
       materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
           @Override
           public void onPositiveButtonClick(Long selection) {
               Calendar calendar=Calendar.getInstance();
               calendar.setTimeInMillis(selection);
               dateResults.OnPicked(calendar);
           }
       });
       materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dateResults.OnCancelled(incomming);
           }
       });
       materialDatePicker.show(fragmentManager,"Date Picker");
    }
 public interface  DateResults{
        void OnPicked(Calendar calendar);
        void OnCancelled(Calendar calendar);
 }
}

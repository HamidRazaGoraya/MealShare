package com.raza.mealshare.Utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtilities {
    public static void SimpleDateSelector(@NotNull final Calendar cur_calender, Context context,final DateSelected dateSelected){
        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar dateOfBirth=Calendar.getInstance();
                dateOfBirth.set(Calendar.YEAR, year);
                dateOfBirth.set(Calendar.MONTH, monthOfYear);
                dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSelected.Clicked(dateOfBirth);
            }
        },cur_calender.get(Calendar.YEAR),cur_calender.get(Calendar.MONTH),cur_calender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat createMatchDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static DateFormat serverFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat createTeamDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat yesterdayTime(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat todayTime(){
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat BirthDay(){
        return new SimpleDateFormat("yyyy-MM", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat FullBirthDay(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat DateAndTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat MatchTime(){
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
    public static String GetDateFromServer(String time){
        try {
            return new SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(serverFormat().parse(time).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static String GetBirthDateFromServer(String time){
        try {
            return BirthDay().format(serverFormat().parse(time).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    @NotNull
    public static Calendar GetCalenderFromServer(String time){
        Calendar calendar=Calendar.getInstance();
        try {
            calendar.setTime(serverFormat().parse(time));
        }catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }
    @NotNull
    public static String GetTimeNotification(String time){
        Calendar serverTime=GetCalenderFromServer(time);
       return todayTime().format(serverTime.getTime());
    }
    @NotNull
    public static String GetDateNotification(String time){
        Calendar serverTime=GetCalenderFromServer(time);
        Calendar DeviceTime=Calendar.getInstance();
        if (DeviceTime.get(Calendar.DAY_OF_YEAR)==serverTime.get(Calendar.DAY_OF_YEAR)){
            return "今天 ";
        }else if (DeviceTime.get(Calendar.DAY_OF_YEAR)-1==serverTime.get(Calendar.DAY_OF_YEAR)){
            return "昨天 ";
        }else {
            return yesterdayTime().format(serverTime.getTime());
        }
    }
    public static String GetMatch(String time){
        try {
            return new SimpleDateFormat("yyy-MM-dd (EEEE) ", Locale.getDefault()).format(serverFormat().parse(time).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    @NotNull
    public static String GetTimeDiffer(String startTime , String endTime){
        try {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(serverFormat().parse(startTime).getTime())+"-"+new SimpleDateFormat("HH:mm", Locale.getDefault()).format(serverFormat().parse(endTime).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static void MatchDate(Calendar cur_calender, Context context,final DateSelected dateSelected){
        if (cur_calender==null){
            cur_calender=Calendar.getInstance();
        }
        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar dateOfBirth=Calendar.getInstance();
                dateOfBirth.set(Calendar.YEAR, year);
                dateOfBirth.set(Calendar.MONTH, monthOfYear);
                dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSelected.Clicked(dateOfBirth);
            }
        },cur_calender.get(Calendar.YEAR),cur_calender.get(Calendar.MONTH),cur_calender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public static void CreateTeam(@NotNull final Calendar cur_calender, Context context,final DateSelected dateSelected){
        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar dateOfBirth=Calendar.getInstance();
                dateOfBirth.set(Calendar.YEAR, year);
                dateOfBirth.set(Calendar.MONTH, monthOfYear);
                dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSelected.Clicked(dateOfBirth);
            }
        },cur_calender.get(Calendar.YEAR),cur_calender.get(Calendar.MONTH),cur_calender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    @NotNull
    public static String SendMatch(@NotNull Calendar calendar, @NotNull Calendar date){
            calendar.set(Calendar.DAY_OF_YEAR,date.get(Calendar.DAY_OF_YEAR));
            return DateAndTime().format(calendar.getTime());
    }
}

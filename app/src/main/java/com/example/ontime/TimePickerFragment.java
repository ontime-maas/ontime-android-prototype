package com.example.ontime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ontime.model.TimeModel;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        System.out.println("시간 정함!!!");

        // 타임피커에서 약속시간 구하기
        int hour , min;
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            hour = timePicker.getHour();
            min = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            min = timePicker.getCurrentMinute();
        }

        TimeModel timeModel = TimeModel.getInstance();
        timeModel.setPromiseTime_hour(hour);
        timeModel.setPromiseTime_min(min);

        System.out.println("시간  : "+hour);
        System.out.println("분  : "+min);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);


        System.out.println("onCreateDialog 시  : "+hour);
        System.out.println("onCreateDialog 분  : "+min);


        return new TimePickerDialog(
                getContext(), this,hour,min, DateFormat.is24HourFormat(getContext())
        );
    }
}

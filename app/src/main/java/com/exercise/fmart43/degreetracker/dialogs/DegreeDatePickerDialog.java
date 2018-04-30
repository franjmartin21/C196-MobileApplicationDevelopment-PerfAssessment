package com.exercise.fmart43.degreetracker.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;



public class DegreeDatePickerDialog extends DatePickerDialog {

    private Context mContext;

    private DatePickerDialog mDatePickerDialog;

    private TextView mTextView;

    private int mDay, mMonth, mYear;

    public DegreeDatePickerDialog(@NonNull Context context, OnDateSetListener listener, int year, int month, int day) {
        super(context, listener,  year, month, day);
    }

}

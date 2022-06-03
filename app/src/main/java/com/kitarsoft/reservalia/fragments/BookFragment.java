package com.kitarsoft.reservalia.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kitarsoft.reservalia.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button btnFechaReserva, btnHoraReserva;
    private ImageButton btnSumarComensal, btnRestarComensal;
    private TextView txtComensales;

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDatePicker();
        initTimePicker();

        btnFechaReserva = (Button) view.findViewById(R.id.btnFechaReserva);
        btnFechaReserva.setText(getTodaysDate());
        btnFechaReserva.setOnClickListener(v -> datePickerDialog.show());

        btnHoraReserva = (Button) view.findViewById(R.id.btnHoraReserva);
        btnHoraReserva.setText(getTodaysHour());
        btnHoraReserva.setOnClickListener(v -> timePickerDialog.show());

        txtComensales = (TextView) view.findViewById(R.id.txtNumComensales);
        txtComensales.setText("0");

        btnSumarComensal = (ImageButton) view.findViewById(R.id.btnAddComensal);
        btnSumarComensal.setOnClickListener(v -> txtComensales.setText(modificarComensal('+')));

        btnRestarComensal = (ImageButton) view.findViewById(R.id.btnSubComensal);
        btnRestarComensal.setOnClickListener(v -> txtComensales.setText(modificarComensal('-')));
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String getTodaysHour()
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return makeHourString(hour, minute);
    }

    private String modificarComensal(char signo){
        switch (signo){
            case '+':
                return String.valueOf(
                        Integer.parseInt(txtComensales.getText().toString())+1);
            case '-':
                return String.valueOf(
                        Integer.parseInt(txtComensales.getText().toString())-1);
            default:
                return txtComensales.getText().toString();
        }
    }

    private String makeDateString(int day, int month, int year)
    {
        String formatedDay = "", formatedMonth = "";
        if (day < 10){
            formatedDay = "0" + day;
        }else{
            formatedDay = String.valueOf(day);
        }
        if (month < 10){
            formatedMonth = "0" + month;
        }else{
            formatedMonth = String.valueOf(month);
        }
        return formatedDay + "/" + formatedMonth + "/" + year;
    }

    private String makeHourString(int hour, int min)
    {
        String formatedHour = "", formatedMin = "";
        if (hour < 10){
            formatedHour = "0" + hour;
        }else{
            formatedHour = String.valueOf(hour);
        }
        if (min < 10){
            formatedMin = "0" + min;
        }else{
            formatedMin = String.valueOf(min);
        }
        return formatedHour + ":" + formatedMin;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnFechaReserva.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initTimePicker()
    {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String time = makeHourString(hour, min);
                btnHoraReserva.setText(time);
            }
        };

        Calendar cal2 = Calendar.getInstance();
        int hour = cal2.get(Calendar.HOUR_OF_DAY);
        int min = cal2.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getActivity(), timeSetListener, hour, min, true);
    }

}
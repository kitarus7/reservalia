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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.BookingDao;
import com.kitarsoft.reservalia.dao.TableDao;
import com.kitarsoft.reservalia.dao.UserDao;
import com.kitarsoft.reservalia.models.Booking;
import com.kitarsoft.reservalia.models.Table;
import com.kitarsoft.reservalia.models.User;
import com.kitarsoft.reservalia.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button btnFechaReserva, btnHoraReserva, btnReservar;
    private ImageButton btnSumarComensal, btnRestarComensal;
    private EditText txtNombre, txtApellidos, txtTelefono, txtComensales;
    private CheckBox chbTerraza;

    private String userId;
    private static final String USER_ID = "userId";
    private User user;

    private BookingDao bookingDao = new BookingDao();
    private UserDao userDao = new UserDao();
    private TableDao tableDao = new TableDao();

    private List<Booking> listaBookings;
    private List<Table> listaMesas;
    private List<Table>listaMesasPorTamanio;

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
        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
        }
        getUser();

        bookingDao.getBookings(userId, null, null, results -> listaBookings = results);
        tableDao.getTables(userId, null, null, results -> listaMesas = results);
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

        txtNombre = (EditText) view.findViewById(R.id.txtNombreReserva);
        txtApellidos = (EditText) view.findViewById(R.id.txtApellidosReserva);
        txtTelefono = (EditText) view.findViewById(R.id.txtTelefonoReserva);
        txtComensales = (EditText) view.findViewById(R.id.txtNumComensales);
        txtComensales.setText("0");
        chbTerraza = (CheckBox) view.findViewById(R.id.chbTerrazaReserva);

        btnSumarComensal = (ImageButton) view.findViewById(R.id.btnAddComensal);
        btnSumarComensal.setOnClickListener(v -> txtComensales.setText(modificarComensal('+')));

        btnRestarComensal = (ImageButton) view.findViewById(R.id.btnSubComensal);
        btnRestarComensal.setOnClickListener(v -> txtComensales.setText(modificarComensal('-')));

        btnReservar = (Button) view.findViewById(R.id.btnReservar);
        btnReservar.setOnClickListener(v -> createBooking());
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

    private void getUser(){
        userDao.getUser(userId, new UserDao.ReadUser() {
            @Override
            public void onCallback(User result) {
                txtNombre.setText(result.getNombre());
                txtApellidos.setText(result.getApellidos());
                txtTelefono.setText(result.getTelefono());
                user = result;
            }
        });
    }

    private void createBooking(){
        Booking newBooking = new Booking();

        newBooking.setUserId(user.getCorreo());
        newBooking.setNombre(user.getNombre());
        newBooking.setApellidos(user.getApellidos());
        newBooking.setTelefono(user.getTelefono());

        // Redondeamos al siguiente número par
        if(Long.parseLong(txtComensales.getText().toString())%2 != 0){
            newBooking.setComensales(Long.parseLong(txtComensales.getText().toString())+1);
        }else{
            newBooking.setComensales(Long.parseLong(txtComensales.getText().toString()));
        }
        newBooking.setTerraza(chbTerraza.isChecked());

        String fecha[] = btnFechaReserva.getText().toString().split("/");
        String hora[] = btnHoraReserva.getText().toString().split(":");

        newBooking.setFecha_reserva(Utils.getDateTime(
                Integer.valueOf(fecha[2]), Integer.valueOf(fecha[1]), Integer.valueOf(fecha[0]),
                Integer.valueOf(hora[0]), Integer.valueOf(hora[1])));

        //  Autoincremental id
        if(listaBookings.size()>0){
            listaBookings.get(listaBookings.size()-1).getId();
            List<Booking> listaOrdenadaId = listaBookings;
            Collections.sort(listaOrdenadaId, (o1, o2) -> o1.getId().compareTo(o2.getId()));
            Integer nextId = Integer.parseInt(listaOrdenadaId.get(listaOrdenadaId.size()-1).getId())+1;
            newBooking.setId(String.valueOf(nextId));
        }else{
            newBooking.setId(String.valueOf(1));
        }

        String mesaAsignada = validarReserva(newBooking);
        if(!mesaAsignada.equals("ND") && !mesaAsignada.equals("ER")) {
            newBooking.setMesaId(mesaAsignada);
            bookingDao.create(newBooking, userId, newBooking.getId());
            readReservas();
            Toast.makeText(getActivity(), "Reserva realizada con éxito", Toast.LENGTH_LONG).show();
        }
        clearReserva();
        readReservas();
    }

    private void clearReserva(){
        btnFechaReserva.setText(getTodaysDate());
        btnHoraReserva.setText(getTodaysHour());
        txtComensales.setText("0");
        chbTerraza.setChecked(false);
    }

    private void readReservas(){
        bookingDao.getBookings(userId, null, null, new BookingDao.ReadBookings(){
            @Override
            public void onCallback(List<Booking> results) {
                listaBookings = results;
            }
        });
    }

    private String validarReserva(Booking newBooking){
        //  Fecha reserva superior a fecha actual
        Date fechaActual = new Date();
        Date fechaReserva = newBooking.getFecha_reserva().toDate();
        if(fechaActual.before(fechaReserva)){
            //  Comensales mínimo 1
            if(newBooking.getComensales()>0){

                //  Si hay reservas las revisamos sino validamos ok
                List<Booking> listaReservasConflictivas = new ArrayList<>();
                for(Booking reserva : listaBookings){
                    listaReservasConflictivas.add(reserva);
                }

                //  Comprobamos la lista de reservas que estamos comparando
                int vuelta = 0;
                while(vuelta<listaReservasConflictivas.size()){
                    vuelta++;
                    //  Quitamos las reservas cuya mesa sea inferior a los comensales
                    for(Booking reserva : listaBookings){
                        for (Table mesa : listaMesas){
                            //  Busco la mesa de la reserva
                            if(mesa.getId().equals(reserva.getMesaId())){
                                //  Si la mesa de la reserva es igual o mayor a de la nueva reserva
                                if(mesa.getTamaño() < newBooking.getComensales()){
                                    listaReservasConflictivas.remove(reserva);
                                }
                            }
                        }
                        //  Quitamos las reservas anteriores que tengan un margen superior de 59min con la nueva
                        //  reserva < (nueva_reserva-3540s)
                        final int MARGEN = 3540;
                        if(reserva.getFecha_reserva().getSeconds() < newBooking.getFecha_reserva().getSeconds()-MARGEN){
                            listaReservasConflictivas.remove(reserva);
                        }
                        //  Quitamos las reservas posteriores que tengan un margen superior de 59min con la nueva
                        //  reserva > (nueva_reserva+3540s)
                        if(reserva.getFecha_reserva().getSeconds() > newBooking.getFecha_reserva().getSeconds()+MARGEN){
                            listaReservasConflictivas.remove(reserva);
                        }
                    }
                }

                List<String> idMesasOcupadas = new ArrayList<>();
                for (Booking reserva_conflictiva : listaReservasConflictivas){
                    //  Se anotan las ids de las mesas reservadas
                    idMesasOcupadas.add(reserva_conflictiva.getMesaId());
                }

                int tamanio = Integer.valueOf(Long.valueOf(newBooking.getComensales()).intValue());
                String mesaAsignada = getMesaLibreTamanio(idMesasOcupadas, tamanio);
                if(mesaAsignada.equals("ND")){
                    Toast.makeText(getActivity(), "No hay mesas disponibles para esa fecha, pruebe con otra hora o día", Toast.LENGTH_LONG).show();
                }
                return mesaAsignada;
            }else{
                Toast.makeText(getActivity(), "El número de comensales mínimos es 1", Toast.LENGTH_LONG).show();
                return "ER";
            }
        }else{
            Toast.makeText(getActivity(), "La fecha de la reserva debe ser superior a la actual", Toast.LENGTH_LONG).show();
            return "ER";
        }
    }

    /**
     * Devuelve la mesa libre que más se ajusta en tamaño a los comensales
     *
     * @param listaOcupadas lista de ids de mesas ocupadas
     * @param tamanio   tamaño mínimo necesario de la mesa
     * @return id de la mesa libre más ajustada al tamaño
     */
    private String getMesaLibreTamanio(List<String> listaOcupadas, int tamanio){
        List<Table>listaMesasOrdenadaTamanio = listaMesas;
        //  Se ordenan las mesas de menor a mayor
        Collections.sort(listaMesasOrdenadaTamanio, (m1, m2) ->
                String.valueOf(m1.getTamaño()).compareTo(String.valueOf(m2.getTamaño())));
        for (Table mesa : listaMesasOrdenadaTamanio) {
            //  Si la mesa es mayor o igual al tamaño necesario
            if(mesa.getTamaño() >= tamanio){
                //  Recorro la lista de ocupadas y si la mesa no se encuentra ocupada la asigno
                if(Collections.frequency(listaOcupadas, mesa.getId())<=0){
                    return mesa.getId();
                }
            }
        }
        return "ND";
    }
}
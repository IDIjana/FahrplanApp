package ch.hsr.se.mas.fahrplanapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ConnectionSearchFragment extends Fragment {
    private Calendar searchDate;
    private boolean departureSearch;
    private DatePickerDialog connectionDatePickerDialog;
    private TimePickerDialog connectionTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private TextView txtFromStation;
    private TextView txtToStation;

    Button btnSearch;
    Button btnTimeReferenceSelection;
    Button btnDatePicker;
    Button btnTimePicker;

    private ConnectionSearchFragmentInteractionListener mListener;

    public ConnectionSearchFragment() {
        searchDate = Calendar.getInstance();
        departureSearch = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormatter = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
        timeFormatter = new SimpleDateFormat(getString(R.string.time_format), Locale.getDefault());

        connectionDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                searchDate.set(Calendar.YEAR, year);
                searchDate.set(Calendar.MONTH, monthOfYear);
                searchDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                btnDatePicker.setText(dateFormatter.format(searchDate.getTime()));
            }
        },searchDate.get(Calendar.YEAR), searchDate.get(Calendar.MONTH), searchDate.get(Calendar.DAY_OF_MONTH));

        connectionTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                searchDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                searchDate.set(Calendar.MINUTE, minute);
                btnTimePicker.setText(timeFormatter.format(searchDate.getTime()));
            }
        }, searchDate.get(Calendar.HOUR_OF_DAY), searchDate.get(Calendar.MINUTE), true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_search, container, false);

        btnSearch = (Button) view.findViewById(R.id.button_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonPressed();
            }
        });

        btnTimeReferenceSelection = (Button) view.findViewById(R.id.button_time_reference);
        btnTimeReferenceSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeReferenceButtonPressed();
            }
        });

        btnDatePicker = (Button) view.findViewById(R.id.button_date_picker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDatePickerButtonPressed();
            }
        });
        btnDatePicker.setText(dateFormatter.format(searchDate.getTime()));

        btnTimePicker = (Button) view.findViewById(R.id.button_time_picker);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimePickerButtonPressed();
            }
        });
        btnTimePicker.setText(timeFormatter.format(searchDate.getTime()));

        txtFromStation = (TextView) view.findViewById(R.id.text_from);
        txtToStation = (TextView) view.findViewById(R.id.text_to);

        return view;
    }

    public void onTimeReferenceButtonPressed() {
        // Swap reference from departure to arrival and vice versa
        departureSearch = !departureSearch;
        if (departureSearch) {
            btnTimeReferenceSelection.setText(getString(R.string.departure_search));
        } else {
            btnTimeReferenceSelection.setText(getString(R.string.arrival_search));
        }
    }

    public void onDatePickerButtonPressed() {
        connectionDatePickerDialog.show();
    }

    public void onTimePickerButtonPressed() {
        connectionTimePickerDialog.show();
    }

    public void onSearchButtonPressed() {
        if (mListener != null) {
            Search search = new Search();
            search.setFromStation(txtFromStation.getText().toString());
            search.setToStation(txtToStation.getText().toString());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
            search.setDate(dateFormatter.format(searchDate.getTime()));
            search.setTime(btnTimePicker.getText().toString());
            search.setArrivalTime(!departureSearch);

            mListener.onSearchStarted(search);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConnectionSearchFragmentInteractionListener) {
            mListener = (ConnectionSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ConnectionSearchFragmentInteractionListener {
        void onSearchStarted(Search search);
    }
}

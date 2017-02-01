package ch.hsr.se.mas.fahrplanapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ConnectionSearchFragment extends Fragment {
    private Calendar searchDate;
    private boolean isArrivalTime;
    private DatePickerDialog connectionDatePickerDialog;
    private TimePickerDialog connectionTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    Button btnSearch;
    Button btnTimeReferenceSelection;
    Button btnDatePicker;
    Button btnTimePicker;

    AutoCompleteTextView textFrom;
    AutoCompleteTextView textTo;

    private ConnectionSearchFragmentInteractionListener mListener;

    public ConnectionSearchFragment() {
        searchDate = Calendar.getInstance();
        isArrivalTime = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initilize the date- and time-picker to store the selected time and date
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

        // Get all elements for later use so we do not have to search them every time
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
        setReferenceSelectionButtonText();

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

        textFrom = (AutoCompleteTextView) view.findViewById(R.id.text_from);
        textTo = (AutoCompleteTextView) view.findViewById(R.id.text_to);

        View resultView = view.findViewById(R.id.fragment_search);
        new SearchStationsAsyncTask(resultView).execute();

        return view;
    }

    public void onTimeReferenceButtonPressed() {
        // Swap reference from departure to arrival and vice versa
        isArrivalTime = !isArrivalTime;
        setReferenceSelectionButtonText();
    }

    private void setReferenceSelectionButtonText() {
        // Sets the button text depending on the selection of the user
        if (isArrivalTime) {
            btnTimeReferenceSelection.setText(getString(R.string.arrival_search));
        } else {
            btnTimeReferenceSelection.setText(getString(R.string.departure_search));
        }
    }

    public void onDatePickerButtonPressed() {
        connectionDatePickerDialog.show();
    }

    public void onTimePickerButtonPressed() {
        connectionTimePickerDialog.show();
    }

    public void onSearchButtonPressed() {
        SimpleDateFormat searchDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fromLocation = textFrom.getText().toString();
        String toLocation = textTo.getText().toString();
        String time = timeFormatter.format(searchDate.getTime());
        String date = searchDateFormatter.format(searchDate.getTime());

        Log.d("From: ", fromLocation);
        Log.d("To: ", toLocation);
        Log.d("Time: ", time);
        Log.d("Date: ", date);
        Log.d("Is arrival time: ", Boolean.toString(isArrivalTime));

        if (mListener != null) {
            // Notice parent activity that the user wants to search connections
            mListener.onSearchStarted( fromLocation,toLocation,date,time, isArrivalTime);
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
        void onSearchStarted(String from, String to, String date, String time, Boolean isArrivalTime);
    }
}

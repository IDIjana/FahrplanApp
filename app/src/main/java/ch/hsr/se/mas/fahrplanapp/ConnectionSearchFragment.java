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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.Station;

public class ConnectionSearchFragment extends Fragment {
    private Calendar searchDate;
    private boolean isArrivalTime;
    private DatePickerDialog connectionDatePickerDialog;
    private TimePickerDialog connectionTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat searchDateFormatter;
    private DelayAutoCompleteTextView txtFromStation;
    private DelayAutoCompleteTextView txtToStation;
    private static final int THRESHOLD = 2;

    Button btnSearch;
    Button btnTimeReferenceSelection;
    Button btnDatePicker;
    Button btnTimePicker;
    ImageButton btnSwitch;
    ImageButton btnEarlierConnections;
    ImageButton btnLaterConnections;
    ImageButton btnNearestStationFrom;

    private ConnectionSearchFragmentInteractionListener mListener;

    public ConnectionSearchFragment() {
        searchDate = Calendar.getInstance();
        isArrivalTime = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the date- and time-picker to store the selected time and date
        dateFormatter = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
        timeFormatter = new SimpleDateFormat(getString(R.string.time_format), Locale.getDefault());
        searchDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

        btnSwitch = (ImageButton) view.findViewById(R.id.button_switch);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchButtonPressed();
            }
        });

        btnNearestStationFrom = (ImageButton) view.findViewById(R.id.button_nearest_station_from);
        btnNearestStationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNearestStationButtonPressed(v);
            }
        });

        btnEarlierConnections = (ImageButton) view.findViewById(R.id.button_earlier_connections);
        btnEarlierConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEarlierConnectionsButtonPressed();
            }
        });

        btnLaterConnections = (ImageButton) view.findViewById(R.id.button_later_connections);
        btnLaterConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaterConnectionsButtonPressed();
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

        StationAutoCompleteAdapter adapter = new StationAutoCompleteAdapter(getContext());
        txtFromStation = (DelayAutoCompleteTextView) view.findViewById(R.id.text_from);
        txtFromStation.setThreshold(THRESHOLD);
        txtFromStation.setAdapter(adapter);
        txtFromStation.setLoadingIndicator((android.widget.ProgressBar) view.findViewById(R.id.pb_loading_indicator_from));
        txtFromStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Station station = (Station) adapterView.getItemAtPosition(position);
                txtFromStation.setTextWithoutSearch(station.getName());
            }
        });

        txtToStation = (DelayAutoCompleteTextView) view.findViewById(R.id.text_to);
        txtToStation.setThreshold(THRESHOLD);
        txtToStation.setAdapter(adapter);
        txtToStation.setLoadingIndicator((android.widget.ProgressBar) view.findViewById(R.id.pb_loading_indicator_to));
        txtToStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Station station = (Station) adapterView.getItemAtPosition(position);
                txtToStation.setTextWithoutSearch(station.getName());
            }
        });

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
        String fromLocation = txtFromStation.getText().toString();
        String toLocation = txtToStation.getText().toString();
        String time = timeFormatter.format(searchDate.getTime());
        String date = searchDateFormatter.format(searchDate.getTime());

        // Start the search with the parameters the user has set
        startConnectionSearch(fromLocation, toLocation, date, time, isArrivalTime);
    }

    public void onEarlierConnectionsButtonPressed() {
        ListView viewConnections = (ListView) getActivity().findViewById(R.id.fragment_search_results);
        int count = viewConnections.getAdapter().getCount();
        if (count > 0) {
            // Get latest shown connection to have a reference for the later search
            Date arrival = searchDate.getTime();
            for (int i = 0; i < count; i++) {
                Connection connection = (Connection) viewConnections.getAdapter().getItem(i);
                Date tempDate = new Date(Long.parseLong(connection.getTo().getArrivalTimestamp())*1000);
                if (tempDate.before(arrival)) {
                    arrival = tempDate;
                }
            }

            // Create a copy of the original calendar to set time for later connection search
            Calendar earlierDate = Calendar.getInstance(searchDate.getTimeZone());
            earlierDate.setTime(arrival);
            earlierDate.add(Calendar.MINUTE, -1);

            // Prepare and get values for the search
            String fromLocation = txtFromStation.getText().toString();
            String toLocation = txtToStation.getText().toString();
            String time = timeFormatter.format(earlierDate.getTime());
            String date = searchDateFormatter.format(earlierDate.getTime());

            // Start the search with the parameters the user has set
            startConnectionSearch(fromLocation, toLocation, date, time, true);
        }
    }

    public void onLaterConnectionsButtonPressed() {
        ListView viewConnections = (ListView) getActivity().findViewById(R.id.fragment_search_results);
        int count = viewConnections.getAdapter().getCount();
        if (count > 0) {
            // Get latest shown connection to have a reference for the later search
            Date departure = searchDate.getTime();
            for (int i = 0; i < count; i++) {
                Connection connection = (Connection) viewConnections.getAdapter().getItem(i);
                Date tempDate = new Date(connection.getFrom().getDepartureTimestamp().longValue()*1000);
                if (tempDate.after(departure)) {
                    departure = tempDate;
                }
            }

            // Create a copy of the original calendar to set time for later connection search
            Calendar laterDate = Calendar.getInstance(searchDate.getTimeZone());
            laterDate.setTime(departure);
            laterDate.add(Calendar.MINUTE, 1);

            // Prepare and get values for the search
            String fromLocation = txtFromStation.getText().toString();
            String toLocation = txtToStation.getText().toString();
            String time = timeFormatter.format(laterDate.getTime());
            String date = searchDateFormatter.format(laterDate.getTime());

            // Start the search with the parameters the user has set
            startConnectionSearch(fromLocation, toLocation, date, time, false);
        }
    }

    private void startConnectionSearch(String fromLocation, String toLocation, String date,
                                       String time, boolean isArrivalTime) {
        Log.d("From: ", fromLocation);
        Log.d("To: ", toLocation);
        Log.d("Time: ", time);
        Log.d("Date: ", date);
        Log.d("Is arrival time: ", Boolean.toString(isArrivalTime));

        if (mListener != null && !fromLocation.isEmpty() && !toLocation.isEmpty()) {
            Search search = new Search();
            search.setFromStation(fromLocation);
            search.setToStation(toLocation);
            search.setDate(date);
            search.setTime(time);
            search.setArrivalTime(isArrivalTime);

            mListener.onSearchStarted(search);
        }
    }

    public void onSwitchButtonPressed() {
        // Get the values to switch
        String fromLocation = txtFromStation.getText().toString();
        String toLocation = txtToStation.getText().toString();

        // Set new values without triggering the station dropdown
        txtFromStation.setThreshold(1000);
        txtFromStation.setText(toLocation);
        txtFromStation.setThreshold(THRESHOLD);
        txtToStation.setThreshold(1000);
        txtToStation.setText(fromLocation);
        txtToStation.setThreshold(THRESHOLD);
    }

    public void onNearestStationButtonPressed(View v) {
        DelayAutoCompleteTextView textView = null;
        switch (v.getId()) {
            case R.id.button_nearest_station_from:
                textView = txtFromStation;
                break;
            case R.id.about:
                textView = txtToStation;
                break;
            default:
                break;
        }

        if (mListener != null) {
            mListener.onNearestLocationSearchStarted(textView);
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
        void onNearestLocationSearchStarted(DelayAutoCompleteTextView textView);
    }
}

package ch.hsr.se.mas.fahrplanapp;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ch.schoeb.opendatatransport.model.Station;

public class SettingsActivity extends AppCompatActivity {
    private DelayAutoCompleteTextView textHomeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set Navigate Up button settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get all needed GUI elements
        StationAutoCompleteAdapter adapter = new StationAutoCompleteAdapter(this);
        textHomeAddress = (DelayAutoCompleteTextView) findViewById(R.id.text_home_address);
        textHomeAddress.setThreshold(Globals.SEARCH_THRESHOLD);
        textHomeAddress.setAdapter(adapter);
        textHomeAddress.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator_home_address));
        textHomeAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Station station = (Station) adapterView.getItemAtPosition(position);
                textHomeAddress.setTextWithoutSearch(station.getName());
            }
        });

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Globals.SETTINGS_NAME, MODE_PRIVATE);
        String homeAddress = settings.getString(Globals.SETTINGS_HOME_ADDRESS, "");

        // Set the settings values
        setHomeAddress(homeAddress);
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        SharedPreferences settings = getSharedPreferences(Globals.SETTINGS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Set values
        editor.putString(Globals.SETTINGS_HOME_ADDRESS, getHomeAddress());

        // Commit the edits!
        editor.apply();
    }

    private void setHomeAddress(String homeAddress) {
        textHomeAddress.setTextWithoutSearch(homeAddress);
    }

    private String getHomeAddress() {
        return textHomeAddress.getText().toString();
    }
}

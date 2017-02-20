package ch.hsr.se.mas.fahrplanapp;

import android.location.Location;
import android.os.AsyncTask;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;

public class SearchNearestStationAsyncTask extends AsyncTask<Void, Void, Station> {
    private DelayAutoCompleteTextView textView;
    private Location location;

    public SearchNearestStationAsyncTask(DelayAutoCompleteTextView textView, Location location) {
        super();
        this.textView = textView;
        this.location = location;
    }

    @Override
    protected Station doInBackground(Void... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        Station station = null;

        try {
            if (this.location != null) {
                StationList stationList = repo.findStations(this.location.getLatitude(), this.location.getLongitude());
                if (stationList.getStations().size() > 0) {
                    station = stationList.getStations().get(0);
                }
            }
        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return station;
    }

    @Override
    protected void onPostExecute(Station station) {
        if (station != null && textView != null) {
            textView.setTextWithoutSearch(station.getName());
        }
    }
}

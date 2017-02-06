package ch.hsr.se.mas.fahrplanapp;

import android.os.AsyncTask;
import android.util.Log;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.StationList;

public class SearchStationsAsyncTask extends AsyncTask<String, Void, StationList> {

    @Override
    protected StationList doInBackground(String... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        StationList stationList = null;

        try {
            stationList = repo.findStations(params[0] + "*");

        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return stationList;
    }

    @Override
    protected void onPostExecute(StationList stationList) {
        //Log.d("StationList:", stationList.toString());
        //Log.d("Count:", String.valueOf(stationList.getStations().size()));
    }
}

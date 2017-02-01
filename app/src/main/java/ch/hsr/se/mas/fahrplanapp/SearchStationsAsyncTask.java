package ch.hsr.se.mas.fahrplanapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.StationList;

public class SearchStationsAsyncTask extends AsyncTask<Void, Void, StationList> {
    private View resultView;

    public SearchStationsAsyncTask(View view){
        this.resultView = view;
    }

    @Override
    protected StationList doInBackground(Void... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        StationList stationList = null;

        try {
            stationList = repo.findStations("Z*");

        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return stationList;
    }

    @Override
    protected void onPostExecute(StationList stationList) {
        Log.d("StationList:", stationList.toString());
        Log.d("Count:", String.valueOf(stationList.getStations().size()));

        //TextView txt = (TextView) resultView.findViewById(R.id.text_overview);
        //txt.setText(connectionList.toString());
    }
}

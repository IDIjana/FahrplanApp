package ch.hsr.se.mas.fahrplanapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;

public class SearchConnectionsAsyncTask extends AsyncTask<String, Void, ConnectionList> {
    private View resultView;

    public SearchConnectionsAsyncTask(View view){
        this.resultView = view;
    }

    @Override
    protected ConnectionList doInBackground(String... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        ConnectionList connectionList = null;

        // Get all relevant parameters
        if (params.length != 5) { throw new IllegalArgumentException(); }

        try {
            connectionList = repo.searchConnections(params[0], params[1], "", params[2], params[3],
                    Boolean.valueOf(params[4]));

        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return connectionList;
    }

    @Override
    protected void onPostExecute(ConnectionList connectionList) {
        Log.d("ConnectionList", connectionList.toString());

        TextView txt = (TextView) resultView.findViewById(R.id.text_overview);
        txt.setText(connectionList.toString());
    }
}

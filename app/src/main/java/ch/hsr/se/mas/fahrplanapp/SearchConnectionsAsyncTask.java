package ch.hsr.se.mas.fahrplanapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;

public class SearchConnectionsAsyncTask extends AsyncTask<Void, Void, ConnectionList> {
    private Search search;
    private Context context;

    public SearchConnectionsAsyncTask(Context context, Search search) {
        super();
        this.context = context;
        this.search = search;
    }

    @Override
    protected ConnectionList doInBackground(Void... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        ConnectionList connectionList = null;

        try {
            connectionList = repo.searchConnections(search.getFromStation(), search.getToStation(), null, search.getDate(), search.getTime(), search.isArrivalTime());
        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return connectionList;
    }

    @Override
    protected void onPostExecute(ConnectionList connectionList) {
        Log.d("ConnectionList", connectionList.toString());

        ConnectionOverviewAdapter adapter = new ConnectionOverviewAdapter(context, connectionList);
        ListView list = (ListView) ((Activity) context).findViewById(R.id.fragment_search_results);
        list.setAdapter(adapter);
    }
}

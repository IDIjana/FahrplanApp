package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;

/**
 * Created by Alexandra on 31.01.2017.
 */


public class ConnectionOverviewAdapter extends BaseAdapter {

    private Context context;
    private List<Connection> connections;

    public ConnectionOverviewAdapter(Context context,ConnectionList connections) {
        this.context = context;
        this.connections = connections.getConnections();
    }
    @Override
    public int getCount() {
        return connections.size();
    }

    @Override
    public Object getItem(int i) {
        return connections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_connection_overview_item, viewGroup, false);
        }

        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Connection connection = (Connection)getItem(i);

        TextView txtFrom = (TextView) view.findViewById(R.id.from);
        txtFrom.setText(connection.getFrom().getStation().getName().trim());

        TextView txtTo = (TextView) view.findViewById(R.id.to);
        txtTo.setText(connection.getTo().getStation().getName().trim());

        Date departure = new Date(connection.getFrom().getDepartureTimestamp().longValue()*1000);
        TextView txtStart = (TextView) view.findViewById(R.id.start);

        String start = tf.format(departure).trim();
        txtStart.setText(tf.format(departure).trim());

        Date arrival = new Date(Long.parseLong(connection.getTo().getArrivalTimestamp())*1000);
        TextView txtEnd= (TextView) view.findViewById(R.id.end);
        txtEnd.setText(tf.format(arrival).trim());

        TextView txtDuration = (TextView) view.findViewById(R.id.duration);
        txtDuration.setText(connection.getDuration().toString().replace("00d","").trim());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}

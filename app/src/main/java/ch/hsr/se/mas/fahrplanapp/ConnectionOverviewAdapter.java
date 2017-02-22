package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;


public class ConnectionOverviewAdapter extends BaseAdapter {

    private Context context;
    private List<Connection> connections;

    public ConnectionOverviewAdapter(Context context, ConnectionList connections) {
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
        Connection connection = (Connection) getItem(i);

        if (connection.getSections().get(0).getJourney() != null) {
            TextView txtTransport = (TextView) view.findViewById(R.id.product);
            txtTransport.setText((String) connection.getSections().get(0).getJourney().getName());

            TextView txtDirection = (TextView) view.findViewById(R.id.direction);
            txtDirection.setText("Richtung " + (String) connection.getSections().get(0).getJourney().getTo());
        }

        TextView txtTo = (TextView) view.findViewById(R.id.to);
        txtTo.setText(connection.getTo().getStation().getName());

        TextView txtFrom = (TextView) view.findViewById(R.id.from);
        txtFrom.setText(connection.getFrom().getStation().getName());


        Date departure = new Date(connection.getFrom().getDepartureTimestamp().longValue() * 1000);
        TextView txtStart = (TextView) view.findViewById(R.id.start);
        txtStart.setText(tf.format(departure).trim());

        Date arrival = new Date(Long.parseLong(connection.getTo().getArrivalTimestamp()) * 1000);
        TextView txtEnd = (TextView) view.findViewById(R.id.end);
        txtEnd.setText(tf.format(arrival).trim());

        TextView txtDuration = (TextView) view.findViewById(R.id.duration);
        String duration = connection.getDuration().replace("00d", "").trim();
        duration = duration.substring(0, duration.length() - 3);
        txtDuration.setText(duration);

        TextView txtPlattform = (TextView) view.findViewById(R.id.platform);
        if (connection.getFrom().getPlatform() != null) {
            txtPlattform.setText("Gl. " + (String) connection.getFrom().getPlatform());
        }


        ImageView imgCapaFirstClass = (ImageView) view.findViewById(R.id.class1);

        Drawable drawCapaFristClass;


        int capaFirst = 0;
        if (connection.getCapacity1st() != null)
            capaFirst = connection.getCapacity1st().intValue();

        switch (capaFirst) {
            case 1:
                drawCapaFristClass = context.getResources().getDrawable(R.drawable.ic_capacity_low, null);
                break;
            case 2:
                drawCapaFristClass = context.getResources().getDrawable(R.drawable.ic_capacity_medium, null);
                break;
            case 3:
                drawCapaFristClass = context.getResources().getDrawable(R.drawable.ic_capacity_high, null);
                break;
            default:
                drawCapaFristClass=null;
                break;
        }
        imgCapaFirstClass.setImageDrawable(drawCapaFristClass);


        ImageView imgCapaSecondClass = (ImageView) view.findViewById(R.id.class2);
        Drawable drawCapaSecondClass;

        int capaSecond = 0;
        if (connection.getCapacity2nd() != null)
            capaSecond = connection.getCapacity2nd().intValue();

        switch (capaSecond) {
            case 1:
                drawCapaSecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_low, null);
                break;
            case 2:
                drawCapaSecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_medium, null);
                break;
            case 3:
                drawCapaSecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_high, null);
                break;
            default:
                drawCapaSecondClass=null;
                break;
        }
        imgCapaSecondClass.setImageDrawable(drawCapaSecondClass);


        ImageView imgProduct = (ImageView) view.findViewById(R.id.productIcon);

        Drawable drawProduct;


        String cat = "";
        if (connection.getSections().get(0).getJourney() != null) {
            cat = connection.getSections().get(0).getJourney().getCategoryCode();
        }

        switch (cat) {
            case "4":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_boat, null);
                break;
            case "6":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_bus, null);
                break;
            case "3":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_train, null);
                break;
            default:
                drawProduct = context.getResources().getDrawable(R.drawable.ic_train, null);
                break;
        }
        imgProduct.setImageDrawable(drawProduct);

        if(connection.getTo().getDelay()!=null) {
            TextView txtDelay = (TextView) view.findViewById(R.id.delay);
            txtDelay.setText("+" + connection.getTo().getDelay());
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}

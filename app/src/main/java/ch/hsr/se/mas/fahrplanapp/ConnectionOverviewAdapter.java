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
            txtTransport.setText(connection.getSections().get(0).getJourney().getName());

            TextView txtDirection = (TextView) view.findViewById(R.id.direction);
            String direction = this.context.getString(R.string.direction) + " " +
                    connection.getSections().get(0).getJourney().getTo();
            txtDirection.setText(direction);
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

        TextView txtPlatform = (TextView) view.findViewById(R.id.platform);
        if (connection.getFrom().getPlatform() != null && !connection.getFrom().getPlatform().isEmpty()) {
            String platform = this.context.getString(R.string.platform) + " " + connection.getFrom().getPlatform();
            txtPlatform.setText(platform);
        }

        int capacityFirst = 0;
        if (connection.getCapacity1st() != null)
            capacityFirst = connection.getCapacity1st().intValue();

        Drawable drawCapacityFirstClass;
        switch (capacityFirst) {
            case 1:
                drawCapacityFirstClass = context.getResources().getDrawable(R.drawable.ic_capacity_low, null);
                break;
            case 2:
                drawCapacityFirstClass = context.getResources().getDrawable(R.drawable.ic_capacity_medium, null);
                break;
            case 3:
                drawCapacityFirstClass = context.getResources().getDrawable(R.drawable.ic_capacity_high, null);
                break;
            default:
                drawCapacityFirstClass = context.getResources().getDrawable(R.drawable.ic_capacity_unknown, null);
                break;
        }
        ImageView imgCapacityFirstClass = (ImageView) view.findViewById(R.id.class1);
        imgCapacityFirstClass.setImageDrawable(drawCapacityFirstClass);

        int capacitySecond = 0;
        if (connection.getCapacity2nd() != null)
            capacitySecond = connection.getCapacity2nd().intValue();

        Drawable drawCapacitySecondClass;
        switch (capacitySecond) {
            case 1:
                drawCapacitySecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_low, null);
                break;
            case 2:
                drawCapacitySecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_medium, null);
                break;
            case 3:
                drawCapacitySecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_high, null);
                break;
            default:
                drawCapacitySecondClass = context.getResources().getDrawable(R.drawable.ic_capacity_unknown, null);
                break;
        }
        ImageView imgCapacitySecondClass = (ImageView) view.findViewById(R.id.class2);
        imgCapacitySecondClass.setImageDrawable(drawCapacitySecondClass);

        String cat = "";
        if (connection.getSections().get(0).getJourney() != null) {
            cat = connection.getSections().get(0).getJourney().getCategoryCode();
        }

        Drawable drawProduct;
        switch (cat) {
            case "4":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_boat, null);
                break;
            case "6":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_bus, null);
                break;
            case "7":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_tram, null);
                break;
            case "9":
                drawProduct = context.getResources().getDrawable(R.drawable.ic_subway, null);
                break;
            default:
                drawProduct = context.getResources().getDrawable(R.drawable.ic_train, null);
                break;
        }
        ImageView imgProduct = (ImageView) view.findViewById(R.id.productIcon);
        imgProduct.setImageDrawable(drawProduct);

        if(connection.getTo().getDelay()!=null) {
            TextView txtDelay = (TextView) view.findViewById(R.id.delay);
            String delay = "+" + connection.getTo().getDelay();
            txtDelay.setText(delay);
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}

package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;


public class StationAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private StationList list = new StationList();

    public StationAutoCompleteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.getStations().size();
    }

    @Override
    public Station getItem(int index) {
        return list.getStations().get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.station_dropdown_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text_station)).setText(getItem(position).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        StationList stations = new SearchStationsAsyncTask().execute(constraint.toString()).get();

                        // Assign the data to the FilterResults
                        filterResults.values = stations;
                        filterResults.count = stations.getStations().size();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    list = (StationList) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}

package ch.hsr.se.mas.fahrplanapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;

public class SearchNearestStationAsyncTask extends AsyncTask<Void, Void, Station> {
    private Context context;
    private DelayAutoCompleteTextView textView;
    private LocationManager locationManager;
    private ProgressDialog progressDialog;
    private Location lastLocation;

    public SearchNearestStationAsyncTask(Context context, DelayAutoCompleteTextView textView) {
        super();
        this.context = context;
        this.textView = textView;
        this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected Station doInBackground(Void... params) {
        // Get Repository
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        Station station = null;
        Looper.myLooper().prepare();

        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
            if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)this.context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return null;
            }
            locationManager.requestSingleUpdate(criteria, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lastLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            }, null);
            if (this.lastLocation != null) {
                double latitude = this.lastLocation.getLatitude();
                double longitude = this.lastLocation.getLongitude();
                String searchString = "*&x=" + latitude + "&y=" + longitude;
                StationList stationList = repo.findStations(searchString);
                station = stationList.getStations().get(0);
            }
        } catch (OpenDataTransportException e) {
            e.printStackTrace();
        }

        return station;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage(this.context.getString(R.string.search_in_progress));
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Station station) {
        if (station != null && textView != null) {
            textView.setTextWithoutSearch(station.getName());
        }
        progressDialog.dismiss();
    }
}

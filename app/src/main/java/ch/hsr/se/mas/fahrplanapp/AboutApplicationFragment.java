package ch.hsr.se.mas.fahrplanapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutApplicationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_application, container, false);

        String version = "";
        try {
            version = getContext().getPackageManager().getPackageInfo(
                    getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView versionView = (TextView) view.findViewById(R.id.about_application_version);
        versionView.setText(getString(R.string.app_name) + " " + version);

        return view;
    }
}



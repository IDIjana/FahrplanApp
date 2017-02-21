package ch.hsr.se.mas.fahrplanapp;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.io.IOException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;

/**
 * Created by Hermann on 21.02.2017.
 */

public class SettingsFragment extends Fragment {

        public View onCreateSettingsView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws IOException, ClassNotFoundException {
            View view = inflater.inflate(R.layout.fragment_settings, container, false);
            Settings set1 = new Settings();

                CheckBox checkTrain = (CheckBox) view.findViewById(R.id.cbxtrain);
                checkTrain.setChecked(set1.containstransp(Transportation.TRAIN));
                CheckBox checkTram = (CheckBox) view.findViewById(R.id.cbxtram);
                checkTram.setChecked(set1.containstransp(Transportation.TRAM));
                CheckBox checkBus = (CheckBox) view.findViewById(R.id.cbxbus);
                checkBus.setChecked(set1.containstransp(Transportation.BUS));
                CheckBox checkCableCar = (CheckBox) view.findViewById(R.id.cbxcablecar);
                checkBus.setChecked(set1.containstransp(Transportation.CABLECAR));
                CheckBox checkShip = (CheckBox) view.findViewById(R.id.cbxcablecar);
                checkBus.setChecked(set1.containstransp(Transportation.SHIP));
                Switch classSwitch = (Switch) view.findViewById(R.id.selectclass);
                classSwitch.setChecked(set1.getClasse()==1);


            return view;
        }


}

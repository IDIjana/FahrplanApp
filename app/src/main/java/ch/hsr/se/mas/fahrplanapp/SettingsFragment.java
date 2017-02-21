package ch.hsr.se.mas.fahrplanapp;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.io.IOException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Button;

/**
 * Created by Hermann on 21.02.2017.
 */

public class SettingsFragment extends Fragment {

        Settings set1;
        View view;

        public View onCreateSettingsView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws IOException, ClassNotFoundException {
            view = inflater.inflate(R.layout.fragment_settings, container, false);
            set1 = new Settings();

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
                EditText homeStation = (EditText) view.findViewById(R.id.takemehome);
                homeStation.setText(set1.getHome());
                Button btn = (Button) view.findViewById(R.id.buttonsaveSettings);
                btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditText homeStation = (EditText) view.findViewById(R.id.takemehome);
                    set1.setHome(homeStation.getText().toString());
                    set1.serializeSettings();
                }
            });
            return view;
        }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbxtrain:
                if (checked) set1.addTransportation(Transportation.TRAIN);
                else set1.removeTransportation(Transportation.TRAIN);
                break;
            case R.id.cbxbus:
                if (checked) set1.addTransportation(Transportation.BUS);
                else set1.removeTransportation(Transportation.BUS);
                break;
            case R.id.cbxtram:
                if (checked) set1.addTransportation(Transportation.TRAM);
                else set1.removeTransportation(Transportation.TRAM);
                break;
            case R.id.cbxship:
                if (checked) set1.addTransportation(Transportation.SHIP);
                else set1.removeTransportation(Transportation.SHIP);
                break;
            case R.id.cbxcablecar:
                if (checked) set1.addTransportation(Transportation.CABLECAR);
                else set1.removeTransportation(Transportation.CABLECAR);
                break;
        }
    }

    public void onSwitchClicked(View view){
        boolean checked = ((Switch) view).isChecked();

        if(checked) set1.setClasse(1);
        else set1.setClasse(2);

    }



}

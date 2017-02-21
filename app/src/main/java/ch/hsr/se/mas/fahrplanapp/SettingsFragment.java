package ch.hsr.se.mas.fahrplanapp;

import android.support.v7.app.AppCompatActivity;
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

public class SettingsFragment extends AppCompatActivity {

        Settings set1;
        View view;
        CheckBox  checkTrain, checkTram, checkBus, checkCableCar, checkShip;

         public SettingsFragment() throws IOException, ClassNotFoundException{
             set1=new Settings();

         }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
            view = inflater.inflate(R.layout.fragment_settings, container, false);

                fillCheckbox(checkTrain, R.id.cbxtrain, Transportation.TRAIN);
                fillCheckbox(checkTram, R.id.cbxtram, Transportation.TRAM);
                fillCheckbox(checkBus, R.id.cbxbus, Transportation.BUS);
                fillCheckbox(checkShip, R.id.cbxship, Transportation.SHIP);
                fillCheckbox(checkCableCar, R.id.cbxcablecar, Transportation.CABLECAR);
                Switch classSwitch = (Switch) view.findViewById(R.id.selectclass);
                classSwitch.setChecked(set1.getClasse()==1);
                EditText homeStation = (EditText) view.findViewById(R.id.takemehome);
                homeStation.setText(set1.getHome());
                Button btn = (Button) view.findViewById(R.id.buttonsaveSettings);
                btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditText homeStation = (EditText) view.findViewById(R.id.takemehome);
                    set1.setHome(homeStation.getText().toString());
                    set1.serializeSettings();}
            });

            return view;
        }
        private void fillCheckbox(CheckBox in, int id, Transportation transp){
            in = (CheckBox) view.findViewById(id);
            in.setChecked(set1.containstransp(transp));
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

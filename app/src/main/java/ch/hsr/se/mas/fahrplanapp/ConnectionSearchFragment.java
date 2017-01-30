package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;


public class ConnectionSearchFragment extends Fragment {
    private Date searchDate;

    private ConnectionSearchFragmentInteractionListener mListener;

    public ConnectionSearchFragment() {
        searchDate = new Date(System.currentTimeMillis());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection_search, container, false);

        Button btn = (Button) view.findViewById(R.id.button_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonPressed();
            }
        });

        return view;
    }

    public void onSearchButtonPressed() {
        if (mListener != null) {
            mListener.onSearchStarted();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConnectionSearchFragmentInteractionListener) {
            mListener = (ConnectionSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ConnectionSearchFragmentInteractionListener {
        void onSearchStarted();
    }
}

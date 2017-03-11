package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import com.seniorproject.sallemapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private int _page;
    private String _title;

    private OnFragmentInteractionListener mListener;



    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(int page, String title) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _page = getArguments().getInt(ARG_PARAM1);
            _title = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);  //Reorder onCreateView by Fisal

        //below line added by Fisal to start loading Sallem Settings from SharedPreferences and view it to layout
        initSettings();

        //below line added by Fisal to start saving Sallem Settings to SharedPreferences
       initSaveSettings();

        return view;   //Reorder onCreateView by Fisal

    }

    //below initSettings() added by Fisal to start loading Sallem Settings from SharedPreferences and view it to layout
    private void initSettings() {
        String allow_user_location = getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getString("allow_location", "true");
        String search_distance = getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getString("search_distance", "1 KM");
        String status = getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getString("status", "Online");

        Switch allowLocationSW = (Switch) findViewById(R.id.btn_allow_user_location);

        if (allow_user_location.equalsIgnoreCase("true")) {
            allowLocationSW.setChecked(true);
        }
        else if (allow_user_location.equalsIgnoreCase("false")) {
            allowLocationSW.setChecked(false);
        }


        Spinner distanceSpinner = (Spinner) findViewById(R.id.spinner2);

        if (search_distance.equalsIgnoreCase("1 KM")) {
            distanceSpinner.equals("1 KM");
        }
        else if (search_distance.equalsIgnoreCase("2 KM")) {
            distanceSpinner.equals("2 KM");
        }
        else if (search_distance.equalsIgnoreCase("3 KM")) {
            distanceSpinner.equals("3 KM");
        }
        else if (search_distance.equalsIgnoreCase("4 KM")) {
            distanceSpinner.equals("4 KM");
        }
        else if (search_distance.equalsIgnoreCase("5 KM")) {
            distanceSpinner.equals("5 KM");
        }
        else if (search_distance.equalsIgnoreCase("6 KM")) {
            distanceSpinner.equals("6 KM");
        }
        else if (search_distance.equalsIgnoreCase("7 KM")) {
            distanceSpinner.equals("7 KM");
        }
        else if (search_distance.equalsIgnoreCase("8 KM")) {
            distanceSpinner.equals("8 KM");
        }
        else if (search_distance.equalsIgnoreCase("9 KM")) {
            distanceSpinner.equals("9 KM");
        }
        else {
            distanceSpinner.equals("10 KM");
        }


        Spinner statusSpinner = (Spinner) findViewById(R.id.spinner3);

        if (status.equalsIgnoreCase("Online")) {
            statusSpinner.equals("Online");
        }
        else if (status.equalsIgnoreCase("Busy")) {
            statusSpinner.equals("Busy");
        }
        else {
            statusSpinner.equals("Offline");
        }
    }



    //below initSettings() added by Fisal to start saving Sallem Settings to SharedPreferences 
    private void initSaveSettings() {
        RelativeLayout rlyout = (RelativeLayout) findViewById(R.id.rlyout_settings);
        rlyout.setOnClickListener(new setOnClickListener() {

            @Override
            public void setOnClickListener(RelativeLayout arg0, int arg1) {
                Switch allowLocationSW = (Switch) findViewById(R.id.btn_allow_user_location);
                Spinner distanceSpinner = (Spinner) findViewById(R.id.spinner2);
                Spinner statusSpinner = (Spinner) findViewById(R.id.spinner3);

                if (allowLocationSW.isChecked()) {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("allow_location", "true").commit();
                }
                else {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("allow_location", "false").commit();
                }


                if (distanceSpinner.getSelectedItem() == "1 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "1 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "2 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "2 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "3 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "3 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "4 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "4 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "5 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "5 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "6 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "6 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "7 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "7 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "8 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "8 KM").commit();
                }
                else if (distanceSpinner.getSelectedItem() == "9 KM") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "9 KM").commit();
                }
                else {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("search_distance", "10 KM").commit();
                }


                if (statusSpinner.getSelectedItem() == "Online") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Online").commit();
                }
                else if (statusSpinner.getSelectedItem() == "Busy") {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Busy").commit();
                }
                else {
                    getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Offline").commit();
                }
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

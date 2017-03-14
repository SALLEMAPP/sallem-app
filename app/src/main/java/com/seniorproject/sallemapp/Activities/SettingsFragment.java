package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    private static final int REQUEST_CODE = 1000;



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

        // Below added by Fisal for change image button
        attachOpenAvatar();

        return view;   //Reorder onCreateView by Fisal

    }

    //below initSettings() added by Fisal to start loading Sallem Settings from SharedPreferences and view it to layout
    private void initSettings() {
        String allow_user_location = getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getString("allow_location", "true");
        Integer search_distance = getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getInt("search_distance", 1);
        String status = getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).getString("status", "Online");

        Switch allowLocationSW = (Switch) getActivity().findViewById(R.id.btn_allow_user_location);

        if (allow_user_location.equalsIgnoreCase("true")) {
            allowLocationSW.setChecked(true);
        }
        else if (allow_user_location.equalsIgnoreCase("false")) {
            allowLocationSW.setChecked(false);
        }


        Spinner distanceSpinner = (Spinner) getActivity().findViewById(R.id.spinner2);

        if (search_distance == 1) {
            distanceSpinner.setSelection(1);
        }
        else if (search_distance == 2) {
            distanceSpinner.setSelection(2);
        }
        else if (search_distance == 3) {
            distanceSpinner.setSelection(3);
        }
        else if (search_distance == 4) {
            distanceSpinner.setSelection(4);
        }
        else if (search_distance == 5) {
            distanceSpinner.setSelection(5);
        }
        else if (search_distance == 6) {
            distanceSpinner.setSelection(6);
        }
        else if (search_distance == 7) {
            distanceSpinner.setSelection(7);
        }
        else if (search_distance == 8) {
            distanceSpinner.setSelection(8);
        }
        else if (search_distance == 9) {
            distanceSpinner.setSelection(9);
        }
        else {
            distanceSpinner.setSelection(10);
        }


        Spinner statusSpinner = (Spinner) getActivity().findViewById(R.id.spinner3);

        for(int i=0;i<3;i++)
            if(status.equals(statusSpinner.getItemAtPosition(i).toString())){
                statusSpinner.setSelection(i);
                break;
            }
    /*
        if (status.equalsIgnoreCase("Online")) {
            statusSpinner.setSelection("Online");
        }
        else if (status.equalsIgnoreCase("Busy")) {
            statusSpinner.equals("Busy");
        }
        else {
            statusSpinner.equals("Offline");
        }
    */

    }



    //below initSettings() added by Fisal to start saving Sallem Settings to SharedPreferences
    private void initSaveSettings() {
        /*
        RelativeLayout rlyout = (RelativeLayout) getActivity().findViewById(R.id.rlyout_settings);
        rlyout.setOnClickListener(new View.OnClickListener()
        */
        Button b = (Button) getActivity().findViewById(R.id.btn_save_settings);
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                    Switch allowLocationSW = (Switch) getActivity().findViewById(R.id.btn_allow_user_location);
                    Spinner distanceSpinner = (Spinner) getActivity().findViewById(R.id.spinner2);
                    Spinner statusSpinner = (Spinner) getActivity().findViewById(R.id.spinner3);

                    if (allowLocationSW.isChecked()) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("allow_location", "true").apply();
                    } else {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("allow_location", "false").apply();
                    }


                    if (distanceSpinner.getSelectedItem() == 1) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 1).apply();
                    } else if (distanceSpinner.getSelectedItem() == 2) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 2).apply();
                    } else if (distanceSpinner.getSelectedItem() == 3) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 3).apply();
                    } else if (distanceSpinner.getSelectedItem() == 4) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 4).apply();
                    } else if (distanceSpinner.getSelectedItem() == 5) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 5).apply();
                    } else if (distanceSpinner.getSelectedItem() == 6) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 6).apply();
                    } else if (distanceSpinner.getSelectedItem() == 7) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 7).apply();
                    } else if (distanceSpinner.getSelectedItem() == 8) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 8).apply();
                    } else if (distanceSpinner.getSelectedItem() == 9) {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 9).apply();
                    } else {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putInt("search_distance", 10).apply();
                    }


                    if (statusSpinner.getSelectedItem() == "Online") {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Online").apply();
                    } else if (statusSpinner.getSelectedItem() == "Busy") {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Busy").apply();
                    } else {
                        getActivity().getSharedPreferences("SallemSettings", Context.MODE_PRIVATE).edit().putString("status", "Offline").apply();
                    }

            }
        });
    }


    private void attachOpenAvatar() {
        ImageButton openAvatarButton = (ImageButton) getActivity().findViewById(R.id.img_add_photo);
        openAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageFromGalary();

            }
        });

    }


    private void openImageFromGalary(){

        Intent gallaryIntent = new Intent();
        gallaryIntent.setType("image/*");
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallaryIntent, REQUEST_CODE);
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

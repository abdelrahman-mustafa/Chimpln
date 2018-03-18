package com.indeves.chmplinapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.R;


public class ProEventResponse extends Fragment {
    private static final String ARG_PARAM1 = "selectedEvent";
    private EventModel selectedEvent;

    public ProEventResponse() {
        // Required empty public constructor
    }

    public static ProEventResponse newInstance(EventModel eventModel) {
        ProEventResponse fragment = new ProEventResponse();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, eventModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedEvent = (EventModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pro_event_response, container, false);
        return rootView;
    }

}

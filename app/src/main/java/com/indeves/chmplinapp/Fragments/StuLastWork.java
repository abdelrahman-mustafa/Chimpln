package com.indeves.chmplinapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indeves.chmplinapp.Adapters.LastWorkImagesAdapter;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StuLastWork.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StuLastWork#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StuLastWork extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StuLastWork() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProLastWork.
     */
    // TODO: Rename and change types and number of parameters
    public static StuLastWork newInstance(String param1, String param2) {
        StuLastWork fragment = new StuLastWork();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stu_last_work, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> images = new ArrayList<>();
        images.add("http://api.learn2crack.com/android/images/donut.png");
        images.add("http://api.learn2crack.com/android/images/eclair.png");
        images.add("http://api.learn2crack.com/android/images/froyo.png");
        images.add("http://api.learn2crack.com/android/images/ginger.png");
        images.add("http://api.learn2crack.com/android/images/honey.png");
        images.add("http://api.learn2crack.com/android/images/icecream.png");
        images.add("http://api.learn2crack.com/android/images/jellybean.png");
        images.add("http://api.learn2crack.com/android/images/kitkat.png");
        images.add("http://api.learn2crack.com/android/images/lollipop.png");
        images.add("http://api.learn2crack.com/android/images/marshmallow.png");
        LastWorkImagesAdapter lastWorkImagesAdapter = new LastWorkImagesAdapter(getContext(), images);
        recyclerView.setAdapter(lastWorkImagesAdapter);
        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onImageClicked(String url) {
        if (mListener != null) {
            mListener.onPhotoClicked(url);
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
        void onPhotoClicked(String url);
    }
}

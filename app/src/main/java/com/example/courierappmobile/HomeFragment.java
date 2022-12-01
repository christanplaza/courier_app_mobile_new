package com.example.courierappmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    TextView textViewName;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        textViewName = rootView.findViewById(R.id.home_username);
        textViewName.setText(sharedPreferences.getString("name", "true"));

        // Inflate the layout for this fragment
        return rootView;
    }
}
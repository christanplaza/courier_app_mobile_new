package com.example.courierappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    TextView textViewName;
    SharedPreferences sharedPreferences;
    String email, userKey;
    List<FeedbackModelClass> feedbackList;
    RecyclerView recyclerView;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        textViewName = rootView.findViewById(R.id.home_username);
        textViewName.setText(sharedPreferences.getString("name", "true"));
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        feedbackList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.list_recycler);
        cardView = rootView.findViewById(R.id.cardView);

        if (sharedPreferences.getString("role", "").equals("customer")) {
            cardView.setVisibility(View.VISIBLE);
        }

        FeedbackAdapter adapter = new FeedbackAdapter(rootView.getContext(), feedbackList);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);


        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        String url = Global.Root_IP + "courier_app_web/api/job_orders/get_job_order_reviews.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("ratings");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    FeedbackModelClass feedbackModel = new FeedbackModelClass();
                                    feedbackModel.setId(jsonObject1.getString("id"));
                                    feedbackModel.setRating(jsonObject1.getString("rating"));
                                    feedbackModel.setFeedback(jsonObject1.getString("customer_feedback"));

                                    feedbackList.add(feedbackModel);
                                }

                                recyclerView.invalidate();
                                recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", email);
                paramV.put("userKey", userKey);
                return paramV;
            }
        };
        queue.add(stringRequest);

        // Inflate the layout for this fragment
        return rootView;
    }
}
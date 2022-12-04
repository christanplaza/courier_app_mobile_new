package com.example.courierappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class OngoingOrdersFragment extends Fragment implements RecyclerViewInterface {
    ProgressBar progressBar;
    TextView title;
    SharedPreferences sharedPreferences;
    String email, userKey;
    List<PendingOrderModelClass> orderList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ongoing_orders, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("courier_app", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        progressBar = rootView.findViewById(R.id.loading);
        title = rootView.findViewById(R.id.title);

        orderList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_view);

        PendingOrdersAdapter adapter = new PendingOrdersAdapter(rootView.getContext(), orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        String url = Global.Root_IP + "courier_app_web/job_orders/ongoing.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("orders");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    PendingOrderModelClass orderModel = new PendingOrderModelClass();
                                    orderModel.setId(jsonObject1.getString("id"));
                                    orderModel.setDatePlaced(jsonObject1.getString("date_placed"));
                                    orderModel.setStatus(jsonObject1.getString("status"));
                                    orderModel.setDescription(jsonObject1.getString("description"));
                                    orderModel.setNote(jsonObject1.getString("note"));
                                    orderModel.setCustomerName(jsonObject1.getString("name"));
                                    orderModel.setCustomerContactNumber(jsonObject1.getString("contact_number"));
                                    orderModel.setCustomerEmail(jsonObject1.getString("email"));
                                    orderModel.setCustomerAddress(jsonObject1.getString("delivery_address"));
                                    orderModel.setCustomerPickupAddress(jsonObject1.getString("pickup_address"));

                                    orderList.add(orderModel);
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
                progressBar.setVisibility(View.GONE);
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

        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), OnGoingOrderDetails.class);

        intent.putExtra("id", orderList.get(position).getId());
        intent.putExtra("description", orderList.get(position).getDescription());
        intent.putExtra("note", orderList.get(position).getNote());
        intent.putExtra("date_placed", orderList.get(position).getDatePlaced());
        intent.putExtra("status", orderList.get(position).getStatus());
        intent.putExtra("name", orderList.get(position).getCustomerName());
        intent.putExtra("email", orderList.get(position).getCustomerEmail());
        intent.putExtra("contact_number", orderList.get(position).getCustomerContactNumber());
        intent.putExtra("address", orderList.get(position).getCustomerAddress());
        intent.putExtra("pickup_address", orderList.get(position).getCustomerPickupAddress());

        startActivity(intent);
    }
}
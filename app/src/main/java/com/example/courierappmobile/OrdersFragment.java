package com.example.courierappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class OrdersFragment extends Fragment implements RecyclerViewInterface {
    ProgressBar progressBar;
    TextView title;
    SharedPreferences sharedPreferences;
    String email, userKey;
    List<OrderModelClass> orderList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        progressBar = rootView.findViewById(R.id.loading);
        title = rootView.findViewById(R.id.title);

        orderList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_view);

        Adapter adapter = new Adapter(rootView.getContext(), orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        String url = Global.Root_IP + "courier_app_web/api/orders.php";

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

                                    OrderModelClass orderModel = new OrderModelClass();
                                    orderModel.setId(jsonObject1.getString("id"));
                                    orderModel.setDatePlaced(jsonObject1.getString("date_placed"));
                                    orderModel.setDescription(jsonObject1.getString("description"));
                                    orderModel.setNote(jsonObject1.getString("note"));
                                    orderModel.setAddress(jsonObject1.getString("delivery_address"));
                                    orderModel.setPickupAddress(jsonObject1.getString("pickup_address"));
                                    if (!jsonObject1.getString("status").equals("Pending")) {
                                        orderModel.setDriverName(jsonObject1.getString("name"));
                                        orderModel.setDriverNumber(jsonObject1.getString("contact_number"));
                                        orderModel.setDeliveryFee(jsonObject1.getString("delivery_fee"));
                                    }
                                    orderModel.setStatus(jsonObject1.getJSONObject("recent_status").getString("status"));
                                    orderModel.setRating(jsonObject1.getString("rating"));
                                    orderModel.setFeedback(jsonObject1.getString("customer_feedback"));

                                    orderList.add(orderModel);
                                }

//                                Adapter adapter = new Adapter(rootView.getContext(), orderList, this);
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
        Intent intent = new Intent(getActivity().getApplicationContext(), OrderDetails.class);

        intent.putExtra("id", orderList.get(position).getId());
        intent.putExtra("description", orderList.get(position).getDescription());
        intent.putExtra("note", orderList.get(position).getNote());
        intent.putExtra("date_placed", orderList.get(position).getDatePlaced());
        intent.putExtra("status", orderList.get(position).getStatus());
        intent.putExtra("address", orderList.get(position).getAddress());
        intent.putExtra("pickup_address", orderList.get(position).getPickupAddress());
        if (!orderList.get(position).getStatus().equals("Pending")) {
            intent.putExtra("driver_name", orderList.get(position).getDriverName());
            intent.putExtra("driver_contact_number", orderList.get(position).getDriverNumber());
            intent.putExtra("delivery_fee", orderList.get(position).getDeliveryFee());
        }

        if (orderList.get(position).getRating().equals("null")) {
            intent.putExtra("notRated", "true");
        }

        startActivity(intent);
    }
}
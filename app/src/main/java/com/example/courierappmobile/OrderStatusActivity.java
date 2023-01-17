package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class OrderStatusActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    List<StatusModelClass> statusList;
    String email, userKey, orderID;
    RecyclerView recyclerView;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        orderID = getIntent().getStringExtra("id");
        recyclerView = findViewById(R.id.list_recycler);
        backBtn = findViewById(R.id.button);

        statusList = new ArrayList<>();
        JobOrderStatusAdapter adapter = new JobOrderStatusAdapter(getApplicationContext(), statusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.Root_IP + "courier_app_web/api/job_orders/job_order_statuses.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                String message = jsonObject.getString("status_list");
                                JSONArray jsonArray = new JSONArray(message);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    StatusModelClass statusModel = new StatusModelClass();
                                    statusModel.setId(jsonObject1.getString("id"));
                                    statusModel.setJobOrderID(jsonObject1.getString("job_order_id"));
                                    statusModel.setStatusMessage(jsonObject1.getString("status_message"));
                                    statusModel.setDate(jsonObject1.getString("date"));
                                    statusModel.setTime(jsonObject1.getString("time"));

                                    statusList.add(statusModel);
                                }
                                recyclerView.invalidate();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", email);
                paramV.put("userKey", userKey);
                paramV.put("status", "Cancelled");
                paramV.put("orderID", orderID);
                return paramV;
            }
        };
        queue.add(stringRequest);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
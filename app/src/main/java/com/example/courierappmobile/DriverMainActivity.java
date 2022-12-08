package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverMainActivity extends AppCompatActivity {
    TextView cName, cNumber, cDate, cPickup, cDelivery, cDescription, cNote, header, lDate, lAddress, lPickup, lDescription, lNote;
    SharedPreferences sharedPreferences;
    String email, userKey;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        cName = findViewById(R.id.driver_customer_name);
        cNumber = findViewById(R.id.driver_customer_contact_number);
        cDate = findViewById(R.id.driver_estimated_date);
        cPickup = findViewById(R.id.driver_pickup_address);
        cDelivery = findViewById(R.id.driver_delivery_address);
        cDescription = findViewById(R.id.driver_customer_description);
        cNote = findViewById(R.id.driver_customer_note);
        header = findViewById(R.id.driver_header);
        fab = findViewById(R.id.fab);

        lDate = findViewById(R.id.label_date);
        lPickup = findViewById(R.id.label_address);
        lAddress= findViewById(R.id.label_delivery);
        lDescription = findViewById(R.id.label_description);
        lNote = findViewById(R.id.label_note);

        cName.setVisibility(View.GONE);
        cNumber.setVisibility(View.GONE);
        cDate.setVisibility(View.GONE);
        cPickup.setVisibility(View.GONE);
        cDelivery.setVisibility(View.GONE);
        cDescription.setVisibility(View.GONE);
        cNote.setVisibility(View.GONE);
        lDate.setVisibility(View.GONE);
        lPickup.setVisibility(View.GONE);
        lAddress.setVisibility(View.GONE);
        lDescription.setVisibility(View.GONE);
        lNote.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.Root_IP + "courier_app_web/api/get_driver_orders.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONObject order = jsonObject.getJSONObject("order");
                                cName.setText(order.getString("name"));
                                cNumber.setText(order.getString("contact_number"));
                                cDate.setText(order.getString("estimated_time"));
                                cPickup.setText(order.getString("pickup_address"));
                                cDelivery.setText(order.getString("delivery_address"));
                                cDescription.setText(order.getString("description"));
                                cNote.setText(order.getString("note"));
                                header.setText("Current Job Order");

                                cName.setVisibility(View.VISIBLE);
                                cNumber.setVisibility(View.VISIBLE);
                                cDate.setVisibility(View.VISIBLE);
                                cPickup.setVisibility(View.VISIBLE);
                                cDelivery.setVisibility(View.VISIBLE);
                                cDescription.setVisibility(View.VISIBLE);
                                cNote.setVisibility(View.VISIBLE);
                                lDate.setVisibility(View.VISIBLE);
                                lPickup.setVisibility(View.VISIBLE);
                                lAddress.setVisibility(View.VISIBLE);
                                lDescription.setVisibility(View.VISIBLE);
                                lNote.setVisibility(View.VISIBLE);

                            } else {
                                header.setText("No Current Job");
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
                return paramV;
            }
        };
        queue.add(stringRequest);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.Root_IP + "courier_app_web/api/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged_in", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("userKey", "");
                                    editor.putString("role", "");
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
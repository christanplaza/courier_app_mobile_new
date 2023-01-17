package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView cName, cNumber, cDate, cPickup, cDelivery, cDescription, cNote, header, lDate, lAddress, lPickup, lDescription, lNote, lFee, cFee;
    SharedPreferences sharedPreferences;
    String email, userKey, orderID;
    FloatingActionButton fab;
    Button confirmOrder, addStatus, markDelivered;

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
        cFee = findViewById(R.id.driver_delivery_fee);
        lFee = findViewById(R.id.label_fee);

        confirmOrder = findViewById(R.id.confirm_button);
        addStatus = findViewById(R.id.update_button);
        markDelivered = findViewById(R.id.delivered_button);

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
        cFee.setVisibility(View.GONE);
        lFee.setVisibility(View.GONE);

        confirmOrder.setVisibility(View.GONE);
        addStatus.setVisibility(View.GONE);
        markDelivered.setVisibility(View.GONE);

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
                                String orderStatus = order.getString("status");
                                orderID = order.getString("id");
                                cName.setText(order.getString("name"));
                                cNumber.setText(order.getString("contact_number"));
                                cDate.setText(order.getString("estimated_time"));
                                cPickup.setText(order.getString("pickup_address"));
                                cDelivery.setText(order.getString("delivery_address"));
                                cDescription.setText(order.getString("description"));
                                cNote.setText(order.getString("note"));
                                cFee.setText("₱" + order.getString("delivery_fee"));
                                header.setText("Current Job Order");

                                if (orderStatus.equals("Ongoing")) {
                                    confirmOrder.setVisibility(View.VISIBLE);
                                } else {
                                    addStatus.setVisibility(View.VISIBLE);
                                    markDelivered.setVisibility(View.VISIBLE);
                                }

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
                                cFee.setVisibility(View.VISIBLE);
                                lFee.setVisibility(View.VISIBLE);

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

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.Root_IP + "courier_app_web/api/job_orders/update_job_order.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                        addInTransitStatus(email, userKey, orderID);
                                    } else {
                                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
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
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                        paramV.put("orderID",  orderID);
                        paramV.put("status",  "In Transit");
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });


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

        addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, DriverStatusUpdate.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

        markDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.Root_IP + "courier_app_web/api/job_orders/delivered_job_order_status.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");

                                    if (status.equals("success")) {
                                        Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(getApplicationContext(), "Delivered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
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
                        paramV.put("orderID", orderID);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    public void addInTransitStatus(String email, String userKey, String orderID) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.Root_IP + "courier_app_web/api/job_orders/add_job_order_status.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Order Confirmed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
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
                paramV.put("orderID", orderID);
                paramV.put("status", "Truck has left the Facility");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}
package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {
    TextView orderNumber, orderPlaced, orderNote, orderDescription, orderStatus, orderAddress, orderPickupAddress, driverName, driverNumber;
    Button closeButton, cancelButton;
    String email, userKey, orderID;
    TableRow detailsDriverName, detailsDriverNumber;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        orderNumber = findViewById(R.id.order_id);
        orderPlaced = findViewById(R.id.order_placed);
        orderNote = findViewById(R.id.order_note);
        orderDescription = findViewById(R.id.order_description);
        orderStatus = findViewById(R.id.order_status);
        closeButton = findViewById(R.id.close_button);
        orderAddress = findViewById(R.id.order_address);
        orderPickupAddress = findViewById(R.id.order_pickup_address);
        driverName = findViewById(R.id.order_driver);
        driverNumber = findViewById(R.id.order_driver_number);
        detailsDriverName = findViewById(R.id.details_driver_name);
        detailsDriverNumber = findViewById(R.id.details_driver_number);
        cancelButton = findViewById(R.id.cancel_order);



        if (!getIntent().getStringExtra("status").equals("Pending") ) {
            driverName.setText(getIntent().getStringExtra("driver_name"));
            driverNumber.setText(getIntent().getStringExtra("driver_contact_number"));
            detailsDriverName.setVisibility(View.VISIBLE);
            detailsDriverNumber.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.GONE);
        }

        Log.d("Intent", getIntent().toUri(0));

        orderNumber.setText(getIntent().getStringExtra("id"));
        orderDescription.setText(getIntent().getStringExtra("description"));
        orderNote.setText(getIntent().getStringExtra("note"));
        orderPlaced.setText(getIntent().getStringExtra("date_placed"));
        orderStatus.setText(getIntent().getStringExtra("status"));
        orderAddress.setText(getIntent().getStringExtra("address"));
        orderPickupAddress.setText(getIntent().getStringExtra("pickup_address"));
        orderID = orderNumber.getText().toString();

        OrdersFragment ordersFragment = new OrdersFragment();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
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
                                        Toast.makeText(getApplicationContext(), "Order has been " + status, Toast.LENGTH_SHORT);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Order cancelled", Toast.LENGTH_SHORT);
                                        finish();
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
            }
        });
    }
}
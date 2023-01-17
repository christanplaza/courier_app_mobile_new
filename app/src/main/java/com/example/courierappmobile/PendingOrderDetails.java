package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PendingOrderDetails extends AppCompatActivity {
    TextView orderNumber, orderPlaced, orderNote, orderDescription, orderCustomerName, orderContactNumber, orderAddress, orderPickupAddress;
    Button closeButton, approveButton, declineButton;
    String email, userKey, estimated_date;
    String[] drivers;
    int[] driverIDs;
    SharedPreferences sharedPreferences;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_details);

        orderNumber = findViewById(R.id.order_id);
        orderPlaced = findViewById(R.id.order_placed);
        orderNote = findViewById(R.id.order_note);
        orderDescription = findViewById(R.id.order_description);
        orderCustomerName = findViewById(R.id.date_placed);
        orderContactNumber = findViewById(R.id.order_customer_contact_number);
        orderAddress = findViewById(R.id.order_address);
        orderPickupAddress = findViewById(R.id.order_pickup_address);
        closeButton = findViewById(R.id.home_button);
        approveButton = findViewById(R.id.approve_button);
        declineButton = findViewById(R.id.decline_button);

        orderNumber.setText(getIntent().getStringExtra("id"));
        orderCustomerName.setText(getIntent().getStringExtra("name"));;
        orderContactNumber.setText(getIntent().getStringExtra("contact_number"));
        orderDescription.setText(getIntent().getStringExtra("description"));
        orderNote.setText(getIntent().getStringExtra("note"));
        orderPlaced.setText(getIntent().getStringExtra("date_placed"));
        orderAddress.setText(getIntent().getStringExtra("address"));
        orderPickupAddress.setText(getIntent().getStringExtra("pickup_address"));

        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.Root_IP + "courier_app_web/api/get_vacant_drivers.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("drivers");
                                drivers = new String[jsonArray.length()];
                                driverIDs = new int[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject driverOBJ = jsonArray.getJSONObject(i);
                                    drivers[i] = driverOBJ.getString("name");
                                    driverIDs[i] = Integer.parseInt(driverOBJ.getString("id"));
                                }

//                                Toast.makeText(getApplicationContext(), "Order has been " + status, Toast.LENGTH_SHORT);
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finish();
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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrder("Declined", email, userKey, getIntent().getStringExtra("id"));
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateOrder("Approved", email, userKey, getIntent().getStringExtra("id"));

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(PendingOrderDetails.this, R.style.datepicker),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                estimated_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PendingOrderDetails.this, R.style.AlertDialogCustom));
                                builder.setTitle("List of Available Drivers");
                                builder.setItems(drivers, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichdriver) {

                                        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(PendingOrderDetails.this, R.style.AlertDialogCustom)).create();
                                        alertDialog.setTitle("Delivery Fee");

                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        lp.setMargins(16, 16, 16, 16);
                                        final EditText input = new EditText(PendingOrderDetails.this);
                                        input.setLayoutParams(lp);
                                        input.setTextColor(Color.WHITE);
                                        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        alertDialog.setView(input);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                                        String url = Global.Root_IP + "courier_app_web/api/assign_driver.php";

                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            String status = jsonObject.getString("status");

                                                                            if (status.equals("success")) {
                                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                                Toast.makeText(getApplicationContext(), "Order assigned to Driver", Toast.LENGTH_SHORT);
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
                                                                paramV.put("courier", driverIDs[whichdriver] + "");
                                                                paramV.put("date", estimated_date);
                                                                paramV.put("orderID", orderNumber.getText().toString());
                                                                paramV.put("delivery_fee", input.getText().toString());
                                                                return paramV;
                                                            }
                                                        };
                                                        queue.add(stringRequest);
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });
                                builder.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.setMessage("Enter Estimated Date");
                datePickerDialog.show();
            }
        });
    }

    public void updateOrder(String status, String email, String userKey, String orderID) {
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
                paramV.put("status", status);
                paramV.put("orderID", orderID);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}
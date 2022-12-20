package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class OrderHistoryDetails extends AppCompatActivity {
    TextView orderStatus, orderNumber, orderPlaced, orderNote, orderDescription, orderCustomerName, orderContactNumber, orderAddress, orderPickupAddress, driverName, driverNumber;
    Button closeButton;
    String email, userKey, estimated_date, status;
    String[] drivers;
    int[] driverIDs;
    SharedPreferences sharedPreferences;
    private int mYear, mMonth, mDay;
    TableRow nameRow, numberRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        orderNumber = findViewById(R.id.order_id);
        orderPlaced = findViewById(R.id.order_placed);
        orderNote = findViewById(R.id.order_note);
        orderDescription = findViewById(R.id.order_description);
        orderCustomerName = findViewById(R.id.date_placed);
        orderContactNumber = findViewById(R.id.order_customer_contact_number);
        orderAddress = findViewById(R.id.order_address);
        orderPickupAddress = findViewById(R.id.order_pickup_address);
        closeButton = findViewById(R.id.home_button);
        driverName = findViewById(R.id.order_driver_name);
        driverNumber = findViewById(R.id.order_driver_number);
        orderStatus = findViewById(R.id.order_status);
        nameRow = findViewById(R.id.driver_name_row);
        numberRow = findViewById(R.id.driver_contact_number_row);

        status = getIntent().getStringExtra("status");
        orderNumber.setText(getIntent().getStringExtra("id"));
        orderCustomerName.setText(getIntent().getStringExtra("name"));
        orderStatus.setText(status);
        orderContactNumber.setText(getIntent().getStringExtra("contact_number"));
        orderDescription.setText(getIntent().getStringExtra("description"));
        orderNote.setText(getIntent().getStringExtra("note"));
        orderPlaced.setText(getIntent().getStringExtra("date_placed"));
        orderAddress.setText(getIntent().getStringExtra("address"));
        orderPickupAddress.setText(getIntent().getStringExtra("pickup_address"));

        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        if (status.equals("Pending") || status.equals("Cancelled")) {
            nameRow.setVisibility(View.GONE);
            numberRow.setVisibility(View.GONE);
        } else {
            driverName.setText(getIntent().getStringExtra("driverName"));
            driverNumber.setText(getIntent().getStringExtra("driverNumber"));
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
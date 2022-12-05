package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class OrderDetails extends AppCompatActivity {
    TextView orderNumber, orderPlaced, orderNote, orderDescription, orderStatus, orderAddress, orderPickupAddress, driverName, driverNumber;
    Button closeButton;
    TableRow detailsDriverName, detailsDriverNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

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

        orderNumber.setText(getIntent().getStringExtra("id"));
        orderDescription.setText(getIntent().getStringExtra("description"));
        orderNote.setText(getIntent().getStringExtra("note"));
        orderPlaced.setText(getIntent().getStringExtra("date_placed"));
        orderStatus.setText(getIntent().getStringExtra("status"));
        orderAddress.setText(getIntent().getStringExtra("address"));
        orderPickupAddress.setText(getIntent().getStringExtra("pickup_address"));

        if (!getIntent().getStringExtra("status").equals("Pending")) {
            driverName.setText(getIntent().getStringExtra("driver_name"));
            driverNumber.setText(getIntent().getStringExtra("driver_contact_number"));
            detailsDriverName.setVisibility(View.VISIBLE);
            detailsDriverNumber.setVisibility(View.VISIBLE);
        }

        OrdersFragment ordersFragment = new OrdersFragment();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
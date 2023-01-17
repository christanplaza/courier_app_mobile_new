package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

public class RateOrder extends AppCompatActivity {
    EditText feedbackInput;
    RatingBar ratingInput;
    Button submitBtn, cancelBtn;
    SharedPreferences sharedPreferences;
    String email, userKey, orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_order);

        sharedPreferences = getSharedPreferences("courier_app", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        orderID = getIntent().getStringExtra("id");
        ratingInput = findViewById(R.id.ratingbar);
        feedbackInput = findViewById(R.id.feedback_text);
        submitBtn = findViewById(R.id.submit_btn);
        cancelBtn = findViewById(R.id.back_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.Root_IP + "courier_app_web/api/job_orders/add_review.php";

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
                                        Toast.makeText(getApplicationContext(), "Rate submitted", Toast.LENGTH_SHORT);
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
                        paramV.put("orderID", orderID);
                        paramV.put("rating", ratingInput.getRating() + "");
                        paramV.put("feedback", feedbackInput.getText().toString());
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
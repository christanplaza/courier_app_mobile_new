package com.example.courierappmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextContactNumber, textInputEditTextPassword, textInputEditTextConfirmPassword;
    Button buttonSubmit;
    String name, email, contact_number, password, password_confirm;
    TextView textViewError;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textInputEditTextName = findViewById(R.id.register_name);
        textInputEditTextEmail = findViewById(R.id.register_email);
        textInputEditTextContactNumber = findViewById(R.id.register_contact_number);
        textInputEditTextPassword = findViewById(R.id.register_password);
        textInputEditTextConfirmPassword = findViewById(R.id.register_password_confirm);
        buttonSubmit = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                name = textInputEditTextName.getText().toString();
                email = textInputEditTextEmail.getText().toString();
                contact_number = textInputEditTextContactNumber.getText().toString();
                password = textInputEditTextPassword.getText().toString();
                password_confirm = textInputEditTextConfirmPassword.getText().toString();


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.Root_IP + "courier_app_web/api/register.php";;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    textViewError.setText(response);
                                    textViewError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("name", name);
                        paramV.put("email", email);
                        paramV.put("contact_number", contact_number);
                        paramV.put("password", password);
                        paramV.put("password_confirm", password_confirm);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
package com.example.courierappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestOrderFragment extends Fragment {
    TextInputEditText textInputEditTextDescription, textInputEditTextNote;
    TextView textViewError;
    Button submitButton;
    String description, note, email, userKey;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_order, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("courier_app", Context.MODE_PRIVATE);

        textInputEditTextDescription = rootView.findViewById(R.id.request_order_description);
        textInputEditTextNote = rootView.findViewById(R.id.request_order_note);
        submitButton = rootView.findViewById(R.id.submit);
        progressBar = rootView.findViewById(R.id.loading);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = textInputEditTextDescription.getText().toString();
                note = textInputEditTextNote.getText().toString();
                email = sharedPreferences.getString("email", "true");
                userKey = sharedPreferences.getString("userKey", "true");

                RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
                String url = "http://192.168.1.8/courier_app_web/job_orders/create.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");

                                    if (status.equals("success")) {
                                        Intent intent = new Intent(rootView.getContext(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                        paramV.put("email", email);
                        paramV.put("description", description);
                        paramV.put("note", note);
                        paramV.put("userKey", userKey);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        return rootView;
    }
}
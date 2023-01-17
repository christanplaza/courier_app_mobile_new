package com.example.courierappmobile;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {
    private Context mContext;
    private List<FeedbackModelClass> feedbackData;

    public FeedbackAdapter(Context mContext, List<FeedbackModelClass> feedbackData) {
        this.mContext = mContext;
        this.feedbackData = feedbackData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.feedback_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.rating.setText("Rating: " + feedbackData.get(position).getRating());
        holder.customerFeedback.setText(feedbackData.get(position).getFeedback());
    }

    @Override
    public int getItemCount() { return feedbackData.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rating, customerFeedback;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rating = itemView.findViewById(R.id.rating);
            customerFeedback = itemView.findViewById(R.id.customer_feedback);
        }
    }
}

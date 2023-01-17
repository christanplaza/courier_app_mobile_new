package com.example.courierappmobile;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobOrderStatusAdapter extends RecyclerView.Adapter<JobOrderStatusAdapter.MyViewHolder>{
    private Context mContext;
    private List<StatusModelClass> statusData;

    public JobOrderStatusAdapter (Context mContext, List<StatusModelClass> statusData) {
        this.mContext = mContext;
        this.statusData = statusData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.order_status_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.statusDate.setText(statusData.get(position).getDate());
        holder.statusTime.setText(statusData.get(position).getTime());
        holder.statusMessage.setText(statusData.get(position).getStatusMessage());
        if (position == 0) {
            holder.statusDate.setTypeface(null, Typeface.BOLD);
            holder.statusTime.setTypeface(null, Typeface.BOLD);
            holder.statusMessage.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() { return statusData.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusDate, statusMessage, statusTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            statusDate = itemView.findViewById(R.id.status_date);
            statusMessage = itemView.findViewById(R.id.status_message);
            statusTime = itemView.findViewById(R.id.status_time);
        }
    }
}

package com.example.courierappmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<OrderModelClass> orderData;

    public Adapter(Context mContext, List<OrderModelClass> orderData, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.orderData = orderData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.order_card, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orderId.setText("Order #" + orderData.get(position).getId());
        holder.dateRequested.setText("Date Requested " + orderData.get(position).getDatePlaced());
        holder.status.setText(orderData.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, dateRequested, status;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            orderId = itemView.findViewById(R.id.textView);
            dateRequested = itemView.findViewById(R.id.textView2);
            status = itemView.findViewById(R.id.textView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

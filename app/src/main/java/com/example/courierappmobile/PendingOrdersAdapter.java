package com.example.courierappmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<PendingOrderModelClass> orderData;

    public PendingOrdersAdapter(Context mContext, List<PendingOrderModelClass> orderData, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.orderData = orderData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pending_order_card, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orderId.setText("Order #" + orderData.get(position).getId());
        holder.dateRequested.setText(orderData.get(position).getDatePlaced());
        holder.name.setText(orderData.get(position).getCustomerName());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, dateRequested, name;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_number);
            dateRequested = itemView.findViewById(R.id.date_placed);
            name = itemView.findViewById(R.id.order_customer_name);

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

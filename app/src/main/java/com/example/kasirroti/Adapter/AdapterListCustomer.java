package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.DataCustomer;

import java.util.ArrayList;
import com.example.kasirroti.R;

public class AdapterListCustomer extends RecyclerView.Adapter<AdapterListCustomer.ViewHolder> {
    private Context context;
    private ArrayList<DataCustomer> dataCustomers;
    private static recyclerViewClickListener listener;

    public AdapterListCustomer(Context context, ArrayList<DataCustomer> dataCustomers) {
        this.context = context;
        this.dataCustomers = dataCustomers;
    }

    @NonNull
    @Override
    public AdapterListCustomer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListCustomer.ViewHolder holder, int position) {
        DataCustomer dataCustomer   = dataCustomers.get(position);
        holder.txt_nama_cus.setText(dataCustomer.getNama());
        holder.txt_no_cus.setText(dataCustomer.getNotelp());
        holder.txt_poin.setText(dataCustomer.getPoin());
    }

    @Override
    public int getItemCount() {
        return dataCustomers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_nama_cus, txt_no_cus, txt_poin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_no_cus      = itemView.findViewById(R.id.txt_no_cus);
            txt_nama_cus    = itemView.findViewById(R.id.txt_nama_cus);
            txt_poin        = itemView.findViewById(R.id.txt_poin);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterListCustomer.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }
}

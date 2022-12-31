package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterPembayaran extends RecyclerView.Adapter<AdapterPembayaran.ViewHolder> {

    private Context context;
    private ArrayList<String> listMetode;

    private static recyclerViewClickListener listener;

    public AdapterPembayaran(Context context, ArrayList<String> listMetode) {
        this.context        = context;
        this.listMetode     = listMetode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pembayaran, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data         = listMetode.get(position);

        holder.txt_pembayaran.setText(data);
    }

    @Override
    public int getItemCount() {
        return listMetode.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_pembayaran;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_pembayaran          = itemView.findViewById(R.id.txt_pembayaran);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterPembayaran.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);
    }
}

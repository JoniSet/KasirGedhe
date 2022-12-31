package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListUlasan;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterUlasan extends RecyclerView.Adapter<AdapterUlasan.ViewHolder> {

    Context context;
    ArrayList<ListUlasan> listUlasan;

    public AdapterUlasan(Context context, ArrayList<ListUlasan> listUlasan) {
        this.context = context;
        this.listUlasan = listUlasan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ulasan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListUlasan data = listUlasan.get(position);

        holder.txtnama.setText(data.getNama_user());
        holder.txt_tanggal.setText(data.getTgl_ulasan());
        holder.txtdetail.setText(data.getSaran());

        if (data.getBintang().equals("")){
            holder.ratingBar.setRating(0);
        }else {
            float bintang = Float.parseFloat(data.getBintang());
            holder.ratingBar.setRating(bintang);
        }
    }

    @Override
    public int getItemCount() {
//        int size    = 0;
//        if (listUlasan.size() < 3){
//            size    = listUlasan.size();
//        }
//        else {
//            size    = 3;
//        }
        return listUlasan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnama, txt_tanggal, txtdetail;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtnama     = itemView.findViewById(R.id.txtnama);
            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
            txtdetail   = itemView.findViewById(R.id.txtdetail);
            ratingBar   = itemView.findViewById(R.id.ratingBar);
        }
    }
}

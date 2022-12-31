package com.example.kasirroti.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListKategori;
import com.example.kasirroti.Model.ListProduk;
import com.example.kasirroti.R;

import java.util.List;


public class AdapterKategoriPoin extends RecyclerView.Adapter<AdapterKategoriPoin.ViewHolder> {

    Activity context;
    List<ListKategori> listKategori;


    private static recyclerViewClickListener listener;

    ListProduk data_produk;

    public AdapterKategoriPoin(Activity context, List<ListKategori> listKategori) {
        this.context        = context;
        this.listKategori   = listKategori;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kategori, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListKategori data   = listKategori.get(position);
        holder.T_kategori.setText(data.getNama_kategori());
        holder.T_id_kategori.setText(data.getId_kategori());

//        listKategori.get(0).setDi_klik("true");

        if (data.getDi_klik().equals("false")){
            holder.T_kategori.setTextColor(context.getResources().getColor(R.color.text));
            holder.T_kategori.setBackgroundResource(R.drawable.bg_layout);
        }
        else {
            holder.T_kategori.setTextColor(context.getResources().getColor(R.color.putih));
            holder.T_kategori.setBackgroundResource(R.drawable.bg_orange);
        }
    }

    @Override
    public int getItemCount() {
        return listKategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView T_kategori, T_id_kategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            T_kategori      = itemView.findViewById(R.id.T_kategori);
            T_id_kategori   = itemView.findViewById(R.id.T_id_kategori);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            T_kategori.setBackgroundResource(R.drawable.bg_layout);
            T_kategori.setTextColor(Color.DKGRAY);

            listener.onClick(v, getAdapterPosition(), T_kategori);

            T_kategori.setBackgroundResource(R.drawable.bg_layout);
            T_kategori.setTextColor(Color.DKGRAY);
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterKategoriPoin.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position, TextView textView);

    }
}

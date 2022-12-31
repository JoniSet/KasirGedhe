package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListInputPesan;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterProdukDiStruk extends RecyclerView.Adapter<AdapterProdukDiStruk.ViewHolder> {

    private Context context;
    private ArrayList<ListInputPesan> listInputPesan;

    public AdapterProdukDiStruk(Context context, ArrayList<ListInputPesan> listInputPesan) {
        this.context = context;
        this.listInputPesan = listInputPesan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_struk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListInputPesan data = listInputPesan.get(position);

        holder.txt_jumlah_struk.setText(data.getJumlah() + " X Rp. " + data.getHarga());
        holder.txt_produk_struk.setText(data.getNama_produk());

        int total           = Integer.parseInt(data.getJumlah()) * Integer.parseInt(data.getHarga());

        holder.txt_total_struk.setText("Rp. " + String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return listInputPesan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_produk_struk, txt_jumlah_struk, txt_total_struk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_produk_struk        = itemView.findViewById(R.id.txt_produk_struk);
            txt_jumlah_struk        = itemView.findViewById(R.id.txt_jumlah_struk);
            txt_total_struk         = itemView.findViewById(R.id.txt_total_struk);
        }
    }
}

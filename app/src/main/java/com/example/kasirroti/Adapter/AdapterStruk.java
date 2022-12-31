package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.DBListPesan;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;

import java.util.List;

public class AdapterStruk extends RecyclerView.Adapter<AdapterStruk.ViewHolder> {

    private Context context;
    private List<DBListPesan> listStruk;

    Tanggal tanggal     = new Tanggal();

    public AdapterStruk(Context context, List<DBListPesan> listStruk) {
        this.context    = context;
        this.listStruk  = listStruk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_struk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DBListPesan data    = listStruk.get(position);
        holder.txt_produk_struk.setText(data.getName());
        holder.txt_jumlah_struk.setText(data.getJumlah());
        holder.txt_harga_struk.setText(tanggal.formatRupiah(Double.parseDouble(data.getHarga())));
        holder.txt_total_struk.setText(tanggal.formatRupiah(Double.parseDouble(data.getTotal())));
    }

    @Override
    public int getItemCount() {
        return listStruk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_produk_struk, txt_jumlah_struk, txt_harga_struk, txt_total_struk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_total_struk         = itemView.findViewById(R.id.txt_total_struk);
            txt_produk_struk        = itemView.findViewById(R.id.txt_produk_struk);
            txt_jumlah_struk        = itemView.findViewById(R.id.txt_jumlah_struk);
        }
    }
}

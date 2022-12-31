package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListLaporanPerproduk;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLaporanPerproduk extends RecyclerView.Adapter<AdapterLaporanPerproduk.ViewHolder> {

    private Context context;
    private List<ListLaporanPerproduk> listLaporanPerproduk;

    Tanggal tanggal               = new Tanggal();

    public AdapterLaporanPerproduk(Context context, List<ListLaporanPerproduk> listLaporanPerproduk) {
        this.context = context;
        this.listLaporanPerproduk = listLaporanPerproduk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_perproduk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPerproduk data   = listLaporanPerproduk.get(position);

        holder.nama.setText(data.getNama_produk());
        holder.pendapatan.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));
        holder.jumlah.setText(data.getTerjual());

        Picasso.get()
                .load(data.getImage())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listLaporanPerproduk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, pendapatan, jumlah;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama            = itemView.findViewById(R.id.txt_nama_produk_perproduk);
            pendapatan      = itemView.findViewById(R.id.txt_pendapatan_perproduk);
            jumlah          = itemView.findViewById(R.id.T_2);

            img             = itemView.findViewById(R.id.img_d);
        }
    }
}

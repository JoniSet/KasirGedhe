package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListLaporanPerkategori;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;

import java.util.ArrayList;

public class AdapterLaporanPerkategori extends RecyclerView.Adapter<AdapterLaporanPerkategori.ViewHolder> {

    private Context context;
    private ArrayList<ListLaporanPerkategori> listLaporanPerkategori;

    Tanggal tanggal                 = new Tanggal();

    public AdapterLaporanPerkategori(Context context, ArrayList<ListLaporanPerkategori> listLaporanPerkategori) {
        this.context                = context;
        this.listLaporanPerkategori = listLaporanPerkategori;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view                   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_perkategori, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPerkategori data = listLaporanPerkategori.get(position);

        holder.txt_jml_kategori.setText(data.getTerjual());

        holder.txt_nama_kategori.setText(data.getNama_kategori());

        holder.txt_pendapatan_perkategori.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));

        holder.txt_kode_kategori.setText(data.getNama_kategori().substring(0, 2));
        holder.txt_kode_kategori.setAllCaps(true);
    }

    @Override
    public int getItemCount() {
        return listLaporanPerkategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_nama_kategori, txt_pendapatan_perkategori, txt_jml_kategori, txt_kode_kategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_jml_kategori            = itemView.findViewById(R.id.txt_jml_kategori);
            txt_nama_kategori           = itemView.findViewById(R.id.txt_nama_kategori);
            txt_pendapatan_perkategori  = itemView.findViewById(R.id.txt_pendapatan_perkategori);
            txt_kode_kategori           = itemView.findViewById(R.id.txt_kode_kategori);
        }
    }
}

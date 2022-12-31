package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListRiwayatRetur;
import com.example.kasirroti.Model.ListRiwayatUpdateStok;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterStokRetur extends RecyclerView.Adapter<AdapterStokRetur.ViewHolder> {
    private Context context;
    private ArrayList<ListRiwayatRetur> listRiwayatRetur;

    public AdapterStokRetur(Context context, ArrayList<ListRiwayatRetur> listRiwayatRetur) {
        this.context            = context;
        this.listRiwayatRetur   = listRiwayatRetur;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_retur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListRiwayatRetur data  = listRiwayatRetur.get(position);

        holder.txt_jml_update.setText("-" + data.getStok_masuk());
        holder.txt_kasir.setText("Kasir  : " + data.getName());
        holder.txt_tanggal_update.setText(data.getTgl());
        holder.txt_nama_bahan_update.setText(data.getNama_bahan());
        holder.txt_keterangan.setText(data.getKeterangan());
//        holder.txt_status.setText(data.getStatus());

        if (data.getStatus().equals("approve"))
        {
            holder.txt_status.setBackgroundResource(R.drawable.bg_hijau);
            holder.txt_status.setText("diterima");
        }
        else if (data.getStatus().equals("denied"))
        {
            holder.txt_status.setBackgroundResource(R.drawable.bg_merah);
            holder.txt_status.setText("ditolak");
        }
        else{
            holder.txt_status.setText("proses");
            holder.txt_status.setBackgroundResource(R.drawable.bg_orange);
        }
    }

    @Override
    public int getItemCount() {
        return listRiwayatRetur.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_kasir, txt_tanggal_update, txt_nama_bahan_update, txt_jml_update, txt_keterangan, txt_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_jml_update          = itemView.findViewById(R.id.txt_jml_update);
            txt_tanggal_update      = itemView.findViewById(R.id.txt_tanggal_update);
            txt_nama_bahan_update   = itemView.findViewById(R.id.txt_nama_bahan_update);
            txt_kasir               = itemView.findViewById(R.id.txt_kasir);
            txt_keterangan          = itemView.findViewById(R.id.txt_keterangan);
            txt_status              = itemView.findViewById(R.id.txt_status);
        }
    }
}

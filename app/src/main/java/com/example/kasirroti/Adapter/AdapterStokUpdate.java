package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListRiwayatUpdateStok;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterStokUpdate extends RecyclerView.Adapter<AdapterStokUpdate.ViewHolder> {
    private Context context;
    private ArrayList<ListRiwayatUpdateStok> listRiwayatUpdateStok;

    public AdapterStokUpdate(Context context, ArrayList<ListRiwayatUpdateStok> listRiwayatUpdateStok) {
        this.context = context;
        this.listRiwayatUpdateStok = listRiwayatUpdateStok;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListRiwayatUpdateStok data  = listRiwayatUpdateStok.get(position);

        holder.txt_jml_update.setText("+" + data.getStok_masuk());
        holder.txt_kasir.setText("Kasir  : " + data.getName());
        holder.txt_tanggal_update.setText(data.getTgl());
        holder.txt_nama_bahan_update.setText(data.getNama_bahan());
    }

    @Override
    public int getItemCount() {
        return listRiwayatUpdateStok.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_kasir, txt_tanggal_update, txt_nama_bahan_update, txt_jml_update;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_jml_update          = itemView.findViewById(R.id.txt_jml_update);
            txt_tanggal_update      = itemView.findViewById(R.id.txt_tanggal_update);
            txt_nama_bahan_update   = itemView.findViewById(R.id.txt_nama_bahan_update);
            txt_kasir               = itemView.findViewById(R.id.txt_kasir);
        }
    }
}

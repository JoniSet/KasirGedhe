package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListLaporanPertahun;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;

import java.util.ArrayList;

public class AdapterLaporanTransPertahun extends RecyclerView.Adapter<AdapterLaporanTransPertahun.ViewHolder> {

    private Context context;
    private ArrayList<ListLaporanPertahun> listLaporanPertahun;
    private static recyclerViewClickListener listener;

    Tanggal tanggal                 = new Tanggal();

    public AdapterLaporanTransPertahun(Context context, ArrayList<ListLaporanPertahun> listLaporanPertahun) {
        this.context                = context;
        this.listLaporanPertahun    = listLaporanPertahun;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view                   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_transaksi_pertahun, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPertahun data    = listLaporanPertahun.get(position);

        holder.txt_pendapatan_trans_bulan.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));
        holder.txt_jml_transakasi_bulan.setText(data.getJml_transaksi() + " Transaksi");

        holder.txt_trans_bulan.setText(data.getBulan());

    }

    @Override
    public int getItemCount() {
        return listLaporanPertahun.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_trans_bulan, txt_jml_transakasi_bulan, txt_pendapatan_trans_bulan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_pendapatan_trans_bulan          = itemView.findViewById(R.id.txt_pendapatan_trans);
            txt_trans_bulan                     = itemView.findViewById(R.id.txt_trans_tahun);
            txt_jml_transakasi_bulan            = itemView.findViewById(R.id.txt_jml_transakasi);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterLaporanTransPertahun.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }

}

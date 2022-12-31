package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListLaporanPertanggal;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;

import java.util.ArrayList;

public class AdapterLaporanTransPertanggal extends RecyclerView.Adapter<AdapterLaporanTransPertanggal.ViewHolder> {

    private Context context;
    private ArrayList<ListLaporanPertanggal> listLaporanPertanggal;
    private static recyclerViewClickListener listener;

    Tanggal tanggal                 = new Tanggal();

    public AdapterLaporanTransPertanggal(Context context, ArrayList<ListLaporanPertanggal> listLaporanPertanggal) {
        this.context = context;
        this.listLaporanPertanggal = listLaporanPertanggal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_transaksi_pertanggal, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPertanggal data  = listLaporanPertanggal.get(position);

        holder.txt_pendapatan_trans.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));
        holder.txt_jml_transakasi_tanggal.setText(data.getJml_transaksi() + " Transaksi");

        holder.txt_trans_tanggal.setText(data.getTanggal().substring(0, 2));

        holder.txt_trans_tanggal2.setText(data.getBulan().substring(3, 6));
//        holder.txt_trans_tanggal.setText(data.getTanggal().substring(0, 1));

    }

    @Override
    public int getItemCount() {
        return listLaporanPertanggal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_trans_tanggal, txt_trans_tanggal2, txt_trans_tanggal3, txt_jml_transakasi_tanggal, txt_pendapatan_trans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_pendapatan_trans                = itemView.findViewById(R.id.txt_pendapatan_trans);
            txt_trans_tanggal                   = itemView.findViewById(R.id.txt_trans_tanggal);
            txt_trans_tanggal2                  = itemView.findViewById(R.id.txt_trans_tanggal2);
            txt_trans_tanggal3                  = itemView.findViewById(R.id.txt_trans_tanggal3);
            txt_jml_transakasi_tanggal          = itemView.findViewById(R.id.txt_jml_transakasi_tanggal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterLaporanTransPertanggal.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }

}

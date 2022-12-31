package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListLaporanPerbulan;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;

import java.util.ArrayList;

public class AdapterLaporanTransPerbulan extends RecyclerView.Adapter<AdapterLaporanTransPerbulan.ViewHolder> {

    private Context context;
    private ArrayList<ListLaporanPerbulan> listLaporanPerbulan;
    private static recyclerViewClickListener listener;

    Tanggal tanggal                 = new Tanggal();

    public AdapterLaporanTransPerbulan(Context context, ArrayList<ListLaporanPerbulan> listLaporanPerbulan) {
        this.context                = context;
        this.listLaporanPerbulan    = listLaporanPerbulan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view                   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_transaksi_perbulan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPerbulan data    = listLaporanPerbulan.get(position);

        holder.txt_pendapatan_trans_bulan.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));
        holder.txt_jml_transakasi_bulan.setText(data.getJml_transaksi() + " Transaksi");

        if (data.getBulan().equals("01"))
        {
            holder.txt_trans_bulan.setText("Jan");
        }
        else if (data.getBulan().equals("02"))
        {
            holder.txt_trans_bulan.setText("Feb");
        }
        else if (data.getBulan().equals("03"))
        {
            holder.txt_trans_bulan.setText("Mar");
        }
        else if (data.getBulan().equals("04"))
        {
            holder.txt_trans_bulan.setText("Apr");
        }
        else if (data.getBulan().equals("05"))
        {
            holder.txt_trans_bulan.setText("Mei");
        }
        else if (data.getBulan().equals("06"))
        {
            holder.txt_trans_bulan.setText("Jun");
        }
        else if (data.getBulan().equals("07"))
        {
            holder.txt_trans_bulan.setText("Jul");
        }
        else if (data.getBulan().equals("08"))
        {
            holder.txt_trans_bulan.setText("Agu");
        }
        else if (data.getBulan().equals("09"))
        {
            holder.txt_trans_bulan.setText("Sep");
        }
        else if (data.getBulan().equals("10"))
        {
            holder.txt_trans_bulan.setText("Okt");
        }
        else if (data.getBulan().equals("11"))
        {
            holder.txt_trans_bulan.setText("Nov");
        }
        else {
            holder.txt_trans_bulan.setText("Des");
        }



    }

    @Override
    public int getItemCount() {
        return listLaporanPerbulan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_trans_bulan, txt_jml_transakasi_bulan, txt_pendapatan_trans_bulan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_pendapatan_trans_bulan          = itemView.findViewById(R.id.txt_pendapatan_trans_bulan);
            txt_trans_bulan                     = itemView.findViewById(R.id.txt_trans_bulan);
            txt_jml_transakasi_bulan            = itemView.findViewById(R.id.txt_jml_transakasi_bulan);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterLaporanTransPerbulan.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }

}

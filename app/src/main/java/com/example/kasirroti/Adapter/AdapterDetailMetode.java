package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListDetailMetode;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterDetailMetode extends RecyclerView.Adapter<AdapterDetailMetode.ViewHolder> {

    private Context context;
    private ArrayList<ListDetailMetode> listMeetode;

    public AdapterDetailMetode(Context context, ArrayList<ListDetailMetode> listMeetode) {
        this.context = context;
        this.listMeetode = listMeetode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_metode, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListDetailMetode data = listMeetode.get(position);

        holder.txt_jml_metode.setText(data.getJml_transaksi());
        holder.txt_metode.setText(data.getTipe());
        holder.txt_total.setText(data.getTotal_pendapatan());

    }

    @Override
    public int getItemCount() {
        return listMeetode.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_metode, txt_jml_metode, txt_total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_total           = itemView.findViewById(R.id.txt_total);
            txt_metode          = itemView.findViewById(R.id.txt_metode);
            txt_jml_metode      = itemView.findViewById(R.id.txt_jml_metode);
        }
    }
}

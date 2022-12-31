package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.LaporanScreen.LaporanTransaksiPerhariActivity;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterFilterMetodeBayar extends RecyclerView.Adapter<AdapterFilterMetodeBayar.ViewHolder> {

    private Context context;
    private ArrayList<String> listMetode;

    public AdapterFilterMetodeBayar(Context context, ArrayList<String> listMetode) {
        this.context        = context;
        this.listMetode     = listMetode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_metode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.metode.setText(listMetode.get(position));
        int pos                 = position;

        LaporanTransaksiPerhariActivity.listFilter.add(listMetode.get(position));

        holder.metode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.metode.isChecked()){
                    LaporanTransaksiPerhariActivity.listFilter.add(listMetode.get(pos));
                }
                else {
                    LaporanTransaksiPerhariActivity.listFilter.remove(listMetode.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMetode.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox metode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            metode              = itemView.findViewById(R.id.txt_metode);
        }
    }
}

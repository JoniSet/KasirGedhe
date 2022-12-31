package com.example.kasirroti.Adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListSisaBahan;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterSisaBahan extends RecyclerView.Adapter<AdapterSisaBahan.ViewHolder> {

    private Context context;
    private ArrayList<ListSisaBahan> listSisaBahan;

    public AdapterSisaBahan(Context context, ArrayList<ListSisaBahan> listSisaBahan) {
        this.context        = context;
        this.listSisaBahan  = listSisaBahan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sisa_bahan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListSisaBahan data  = listSisaBahan.get(position);

        holder.txt_nama_bahan.setText(data.getNama_bahan());
        holder.jml.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.jml.getText().toString().length() > 0){
                    data.setJumlah(holder.jml.getText().toString());
                }
                else {
                    data.setJumlah("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SettingSatuan(holder.spinner_sisa_bhn, data);
    }

    @Override
    public int getItemCount() {
        return listSisaBahan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama_bahan;
        EditText jml;
        Spinner spinner_sisa_bhn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama_bahan          = itemView.findViewById(R.id.txt_nama_bahan);
            jml                     = itemView.findViewById(R.id.edt_jml);
            spinner_sisa_bhn        = itemView.findViewById(R.id.spinner_sisa_bhn);
        }
    }

    private void SettingSatuan(Spinner spinner, ListSisaBahan listSisaBahan){
        ArrayList<String> listSatuan       = new ArrayList<>();
        AdapterSpinnerSatuanBahan adapter;
        listSatuan.add("---");
        listSatuan.add("BKS");
        listSatuan.add("EMBER");
        listSatuan.add("KLG");
        listSatuan.add("KG");
        listSatuan.add("PAIL");
        listSatuan.add("PCS");

        adapter                     = new AdapterSpinnerSatuanBahan(context, listSatuan);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String satuan       = (String) parent.getItemAtPosition(position);
                if (satuan.equals("---")){
                    satuan          = "";
                }
                listSisaBahan.setSatuan(satuan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}

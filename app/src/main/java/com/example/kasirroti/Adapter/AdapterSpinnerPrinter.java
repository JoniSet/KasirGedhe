package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kasirroti.Model.ListBluetooth;
import com.example.kasirroti.Model.ListOutlet;
import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterSpinnerPrinter extends ArrayAdapter<ListBluetooth> {

    public AdapterSpinnerPrinter(@NonNull Context context, ArrayList<ListBluetooth> listItem) {
        super(context, 0, listItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_spinner_bluetooth, parent, false
            );
        }

        TextView textViewName           = convertView.findViewById(R.id.txt_nama_bluetooth);
        ListBluetooth currentItem       = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getNama_outlet());
        }

        return convertView;
    }
}

package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kasirroti.R;

import java.util.ArrayList;

public class AdapterSpinnerSatuanBahan extends ArrayAdapter<String> {

    public AdapterSpinnerSatuanBahan(@NonNull Context context, ArrayList<String> listItem) {
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
                    R.layout.list_spinner_satuan, parent, false
            );
        }

        TextView textViewName           = convertView.findViewById(R.id.textView);
        String currentItem              = getItem(position);


        if (currentItem != null) {
            textViewName.setText(currentItem);
        }
        return convertView;
    }
}

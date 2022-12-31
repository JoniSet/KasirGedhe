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

import com.example.kasirroti.R;
import com.example.kasirroti.Model.ListOutlet;

import java.util.ArrayList;
public class CustomSpinnerAdapter extends ArrayAdapter<ListOutlet> {

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<ListOutlet> listItem) {
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
                    R.layout.list_spinner, parent, false
            );
        }
        ImageView imageViewFlag         = convertView.findViewById(R.id.imageView);
        TextView textViewName           = convertView.findViewById(R.id.textView);
        TextView textViewId             = convertView.findViewById(R.id.textViewId);
        ListOutlet currentItem          = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(R.drawable.logo_roti);
            textViewName.setText(currentItem.getNama_outlet());
            textViewId.setText(currentItem.getId());
        }
        return convertView;
    }
}

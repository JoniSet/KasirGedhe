package com.example.kasirroti.bantuan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AdapterStok extends RecyclerView.Adapter<AdapterStok.ViewHolder> {

    private Context context;
    private ArrayList<ListStok> listStok;
    private static AdapterStok.recyclerViewClickListener listener;

    public AdapterStok(Context context, ArrayList<ListStok> listStok) {
        this.context = context;
        this.listStok = listStok;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stok, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListStok data               = listStok.get(position);

        int pos                     = holder.getAdapterPosition();
        int poss                    = position;

        holder.txt_jml_stok.setText(data.getStok());
        holder.txt_jml_stok.setVisibility(View.VISIBLE);
        holder.txt_nama_stok.setText(data.getNama_bahan());
        holder.shimmerjmlStok.setVisibility(View.GONE);

        holder.txt_kode_stok.setText(data.getNama_bahan().substring(0,2));
    }

    @Override
    public int getItemCount() {
        return listStok.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txt_nama_stok, txt_jml_stok, txt_kode_stok, txt_id_stok;
        private RelativeLayout R_11;
        private ShimmerFrameLayout shimmerjmlStok;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama_stok           = itemView.findViewById(R.id.txt_nama_stok);
            txt_jml_stok            = itemView.findViewById(R.id.txt_jml_stok);
            txt_kode_stok           = itemView.findViewById(R.id.txt_kode_stok);
            R_11                    = itemView.findViewById(R.id.R_11);
            shimmerjmlStok          = itemView.findViewById(R.id.shimmerjmlStok);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterStok.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }
}
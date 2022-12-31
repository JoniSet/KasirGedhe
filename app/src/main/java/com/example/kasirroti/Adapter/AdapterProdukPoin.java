package com.example.kasirroti.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListProduk;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProdukPoin extends RecyclerView.Adapter<AdapterProdukPoin.ViewHolder> {

    Context context;
    private List<ListProduk> listProduk;

    Tanggal tanggal         = new Tanggal();
    private static AdapterProdukPoinOnClick listener;

    public AdapterProdukPoin(Context context, List<ListProduk> listProduk) {
        this.context    = context;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListProduk data = listProduk.get(position);
        holder.txt_harga.setText(data.getHarga());
        holder.txt_nama_roti_a.setText(data.getNama_produk());
        String gambar   = data.getImg();
        String a        = String.valueOf(position);

        if (gambar.length() > 5) {
            Picasso.get()
                    .load(gambar)
                    .into(holder.img_a);
        }


    }


    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_a;
        TextView txt_nama_roti_a, txt_harga, txt_jml;
        RelativeLayout R_8;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_a               = itemView.findViewById(R.id.img_a);
            txt_jml             = itemView.findViewById(R.id.txt_jml);
            txt_nama_roti_a     = itemView.findViewById(R.id.txt_nama_roti_a);
            txt_harga           = itemView.findViewById(R.id.txt_harga_a);
            R_8                 = itemView.findViewById(R.id.R_8);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void adapterPoinClickListener(AdapterProdukPoinOnClick listener){
        AdapterProdukPoin.listener = listener;
    }

    public interface AdapterProdukPoinOnClick{
        void onClick(View v, int position);
    }

    //==============================================================================================

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

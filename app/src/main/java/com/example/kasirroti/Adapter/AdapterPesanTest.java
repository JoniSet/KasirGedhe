package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.Model.ListGridPesanan;
import com.example.kasirroti.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPesanTest extends RecyclerView.Adapter<AdapterPesanTest.ViewHolder> {

    Context context;
    ArrayList<ListGridPesanan> listPesan;

    private static recyclerViewClickListener listener;

    public AdapterPesanTest(Context context, ArrayList<ListGridPesanan> listPesan) {
        this.context = context;
        this.listPesan = listPesan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_pesanan, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListGridPesanan list    = listPesan.get(position);

        holder.txt_nama_roti_a.setText(list.getNama_produk());
        holder.txt_jumlah_a.setText(list.getJumlah());
        holder.txt_harga_a.setText("Rp. " + list.getHarga_satuan());

        if (list.getJumlah().equals("0"))
        {
            holder.R_10.setVisibility(View.GONE);
        }
        else
        {
            holder.R_10.setVisibility(View.VISIBLE);
            holder.txt_total_a.setText("Rp. " +  String.valueOf(Integer.parseInt(list.getJumlah())
                    * Integer.parseInt(list.getHarga_satuan())));


            Picasso.get()
                    .load(list.getImg())
                    .into(holder.img_b);
        }

    }

    @Override
    public int getItemCount() {
        return listPesan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_b;
        TextView txt_nama_roti_a, txt_total_a, txt_harga_a, txt_jumlah_a;
        RelativeLayout R_10;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_b           = itemView.findViewById(R.id.img_b);
            txt_nama_roti_a = itemView.findViewById(R.id.txt_nama_roti_a);
            txt_total_a     = itemView.findViewById(R.id.txt_total_a);
            txt_harga_a     = itemView.findViewById(R.id.txt_harga_a);
            txt_jumlah_a    = itemView.findViewById(R.id.txt_jumlah_a);
            R_10            = itemView.findViewById(R.id.R_10);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterPesanTest.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }

}

package com.example.kasirroti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListStokKeluar;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.StokScreen.StokKeluarActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterStokKeluar extends RecyclerView.Adapter<AdapterStokKeluar.ViewHolder> {

    private Context context;
    private ArrayList<ListStokKeluar> listStok;
    private static recyclerViewClickListener listener;

    public AdapterStokKeluar(Context context, ArrayList<ListStokKeluar> listStok) {
        this.context    = context;
        this.listStok   = listStok;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stok, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListStokKeluar data         = listStok.get(position);

        int pos                     = holder.getAdapterPosition();

        holder.txt_jml_stok.setText(data.getJml_stok());
        holder.txt_nama_stok.setText(data.getNama_stok());

        holder.txt_kode_stok.setText(data.getNama_stok().substring(0,2));

        StokKeluarActivity.dialog.show();

        AndroidNetworking.post(Server.URL + "stok_bahan")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .addBodyParameter("id_bahan", data.getId_stok())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                holder.txt_jml_stok.setVisibility(View.VISIBLE);
                                holder.txt_jml_stok.setText(response.getString("stok"));
                                holder.shimmerjmlStok.setVisibility(View.GONE);

                                listStok.get(pos).setJml_stok(response.getString("stok"));

                                StokKeluarActivity.dialog.dismiss();
                            }
                            else
                            {
                                holder.txt_jml_stok.setText("0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            holder.txt_jml_stok.setText("0");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        holder.txt_jml_stok.setText("0");
                    }
                });

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
        AdapterStokKeluar.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }
}

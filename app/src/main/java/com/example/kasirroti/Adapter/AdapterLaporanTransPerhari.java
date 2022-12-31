package com.example.kasirroti.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.LaporanScreen.LaporanTransaksiPerhariActivity;
import com.example.kasirroti.Model.ListLaporanPerhari;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.Helper.Tanggal;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterLaporanTransPerhari extends RecyclerView.Adapter<AdapterLaporanTransPerhari.ViewHolder> {

    private Context context;
    private ArrayList<ListLaporanPerhari> listLaporanPerhari;
    private static recyclerViewClickListener listener;

    Tanggal tanggal             = new Tanggal();

    public AdapterLaporanTransPerhari(Context context, ArrayList<ListLaporanPerhari> listLaporanPerhari) {
        this.context = context;
        this.listLaporanPerhari = listLaporanPerhari;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_transaksi_perhari, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanPerhari data     = listLaporanPerhari.get(position);
        String pos                  = String.valueOf(position);

        holder.txt_pendapatan_trans.setText(tanggal.formatRupiah(Double.parseDouble(data.getPendapatan())));
        holder.txt_jam_transakasi_harian.setText(data.getJam());
        holder.txt_no_transakasi.setText(data.getNo());

        holder.txt_trans_tanggal_harian.setText(data.getTanggal().substring(0, 2));

        holder.txt_trans_tanggal_harian2.setText(data.getBulan().substring(3, 6));

        holder.img_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog(data.getId(), pos);
            }
        });

        if (data.getStatus().equals("proses"))
        {
            holder.L_report_harian.setBackgroundResource(R.color.light_gray);
            holder.img_hapus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listLaporanPerhari.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_jam_transakasi_harian, txt_trans_tanggal_harian, txt_trans_tanggal_harian2, txt_trans_tanggal_harian3, txt_no_transakasi, txt_pendapatan_trans;
        private ImageView img_hapus;
        private LinearLayout L_report_harian;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_jam_transakasi_harian           = itemView.findViewById(R.id.txt_jam_transakasi_harian);
            txt_pendapatan_trans                = itemView.findViewById(R.id.txt_pendapatan_trans_harian);
            txt_trans_tanggal_harian            = itemView.findViewById(R.id.txt_trans_tanggal_harian);
            txt_trans_tanggal_harian2           = itemView.findViewById(R.id.txt_trans_tanggal_harian2);
            txt_trans_tanggal_harian3           = itemView.findViewById(R.id.txt_trans_tanggal_harian3);
            txt_no_transakasi                   = itemView.findViewById(R.id.txt_no_transakasi);
            img_hapus                           = itemView.findViewById(R.id.img_batal);
            L_report_harian                     = itemView.findViewById(R.id.L_report_harian);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(recyclerViewClickListener listener){
        AdapterLaporanTransPerhari.listener = listener;
    }

    public interface recyclerViewClickListener{
        void onClick(View v, int position);

    }

    private void show_dialog(String id_transaksi, String position) {
        final Dialog dialog                           = new Dialog(context);
        dialog.setContentView(R.layout.dialog_batal_transaksi);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        int pos                 = Integer.parseInt(position);

        ImageView img_x         = dialog.findViewById(R.id.img_x);
        EditText edt_alasan     = dialog.findViewById(R.id.edt_alasan);
        Button btn_simpan_alasan    = dialog.findViewById(R.id.btn_simpan_alasan);

        img_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_simpan_alasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alasan           = edt_alasan.getText().toString();

                if (alasan.isEmpty()) {
                    FancyToast.makeText(context, "Harap Memasukkan Keterangan",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

                }
                else {
                    dialog.dismiss();
                    btn_simpan_alasan.setEnabled(false);
                    Dialog log          = new Dialog(context);
                    log.show();
                    log.setContentView(R.layout.loading);
                    log.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    log.setCancelable(false);

                    TextView txt_title          = log.findViewById(R.id.txt_title);
                    txt_title.setText("Mengirim Permintaan");

                    AndroidNetworking.post(Server.URL + "req_pembatalan")
                            .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                            .addBodyParameter("id_transaksi", id_transaksi)
                            .addBodyParameter("keterangan", alasan)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        log.dismiss();
                                        if (response.getString("response").equals("200")) {
                                            getBahanOutlet(dialog, pos);
                                        } else {
                                            FancyToast.makeText(context, "Permintaan pembatalan gagal!!",
                                                    FancyToast.LENGTH_SHORT,
                                                    FancyToast.ERROR,
                                                    false).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        log.dismiss();
                                        FancyToast.makeText(context, "Request Error!",
                                                FancyToast.LENGTH_SHORT,
                                                FancyToast.ERROR,
                                                false).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    log.dismiss();
                                    FancyToast.makeText(context, "Network Error!",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false).show();
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    private void getBahanOutlet(Dialog dialog, int pos) {
        SqliteHelper sqliteHelper       = new SqliteHelper(context);
        sqliteHelper.delete_bahan_outlet();
        AndroidNetworking.post(Server.URL + "stok_manajemen")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");

                                FancyToast.makeText(context, "Permintaan pembatalan terkirim!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false).show();
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    if (!jsonObject.getString("stok").equals("0")) {
                                        sqliteHelper.add_bahan_outlet(
                                                jsonObject.getString("id_stok"),
                                                jsonObject.getString("id_bahan"),
                                                jsonObject.getString("stok"),
                                                jsonObject.getString("nama_bahan"));
                                        LaporanTransaksiPerhariActivity.adapter.notifyItemChanged(pos);
                                        ((Activity) context).recreate();
                                        dialog.dismiss();
                                    }
                                }
                            } else {
                                FancyToast.makeText(context,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(context, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Bahan Outlet", String.valueOf(anError.getErrorCode()));

                        FancyToast.makeText(context, "Gagal mengambil data Bahan Outlet, Jaringan Bermasalah",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

}

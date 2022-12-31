package com.example.kasirroti.StokScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterStokRetur;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListRiwayatRetur;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class HistoryReturStokActivity extends AppCompatActivity {

    private RecyclerView recycler_riwayat;
    private ArrayList<ListRiwayatRetur> listRiwayatUpdateStok  = new ArrayList<>();
    private AdapterStokRetur adapterStok;
    private ImageView img_back, tutorial;

    private TextView txt_semua, txt_approve, txt_proses, txt_denied;
    private String status           = "semua";

    String tgl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_retur_stok);

        recycler_riwayat            = findViewById(R.id.recycler_riwayat);
        recycler_riwayat.setItemAnimator(new DefaultItemAnimator());
        recycler_riwayat.setLayoutManager(new LinearLayoutManager(this));
        recycler_riwayat.setHasFixedSize(true);

        txt_semua                   = findViewById(R.id.txt_semua);
        txt_approve                 = findViewById(R.id.txt_approve);
        txt_proses                  = findViewById(R.id.txt_proses);
        txt_denied                  = findViewById(R.id.txt_denied);
        tutorial                    = findViewById(R.id.tutorial);
        img_back                    = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorial.setEnabled(false);
                recycler_riwayat.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_tutorial();
                    }
                }, 200);
            }
        });

        txt_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status              = "proses";
                txt_proses.setBackgroundResource(R.color.hijau);
                txt_semua.setBackgroundResource(android.R.color.transparent);
                txt_approve.setBackgroundResource(android.R.color.transparent);
                txt_denied.setBackgroundResource(android.R.color.transparent);
                getRiwayat(status);
            }
        });

        txt_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status              = "approve";
                txt_approve.setBackgroundResource(R.color.hijau);
                txt_semua.setBackgroundResource(android.R.color.transparent);
                txt_proses.setBackgroundResource(android.R.color.transparent);
                txt_denied.setBackgroundResource(android.R.color.transparent);
                getRiwayat(status);
            }
        });

        txt_semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status              = "semua";
                txt_semua.setBackgroundResource(R.color.hijau);
                txt_approve.setBackgroundResource(android.R.color.transparent);
                txt_proses.setBackgroundResource(android.R.color.transparent);
                txt_denied.setBackgroundResource(android.R.color.transparent);
                getSemuaRiwayat();
            }
        });

        txt_denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status              = "denied";
                txt_denied.setBackgroundResource(R.color.hijau);
                txt_approve.setBackgroundResource(android.R.color.transparent);
                txt_proses.setBackgroundResource(android.R.color.transparent);
                txt_semua.setBackgroundResource(android.R.color.transparent);
                getRiwayat(status);
            }
        });

        getSemuaRiwayat();
    }

    public void getRiwayat(String sts){
            listRiwayatUpdateStok.clear();
            recycler_riwayat.setAdapter(null);

            Dialog dialog               = new Dialog(HistoryReturStokActivity.this);
            dialog.show();
            dialog.setContentView(R.layout.loading);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            SimpleDateFormat format1    = new SimpleDateFormat("dd MMM yyyy");
            AndroidNetworking.post(Server.URL + "histori_stok_keluar")
                    .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("response").equals("200"))
                                {
                                    JSONArray array         = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++)
                                    {
                                        JSONObject object   = array.getJSONObject(i);

                                        String nama_bahan   = object.getString("nama_bahan");
                                        String tgl          = object.getString("tgl_create");
                                        String stok_keluar  = object.getString("stok_keluar");
                                        String name         = object.getString("name");
                                        String status       = object.getString("status");
                                        String keterangan   = object.getString("keterangan");

                                        String date         = tgl.substring(0, 10);
                                        String jam          = tgl.substring(11, 16);

                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                            Date date1      = format.parse(date);
                                            tgl1            = format1.format(date1);
    //                                            Toast.makeText(LaporanTransaksiPertanggalActivity.this, format1.format(date1).toString()
    //                                                    , Toast.LENGTH_SHORT).show();

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (status.equals(sts))
                                        {
                                            ListRiwayatRetur o  = new ListRiwayatRetur(
                                                    nama_bahan,
                                                    tgl1 + "   " + jam,
                                                    stok_keluar,
                                                    name,
                                                    keterangan,
                                                    status
                                            );

                                            listRiwayatUpdateStok.add(o);
                                        }



                                    }

                                    adapterStok             = new AdapterStokRetur(HistoryReturStokActivity.this, listRiwayatUpdateStok);
                                    adapterStok.notifyDataSetChanged();

                                    recycler_riwayat.setAdapter(adapterStok);
                                    dialog.dismiss();
                                }else
                                {
                                    dialog.dismiss();
                                    FancyToast.makeText(HistoryReturStokActivity.this,
                                            response.getString("message"),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.INFO,
                                            false)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                SingOut signOut = new SingOut();
                                signOut.Logout(HistoryReturStokActivity.this);
                            }
                            else {
                                dialog.dismiss();
                            }

                        }
                    });
}

    public void getSemuaRiwayat(){
            listRiwayatUpdateStok.clear();
            recycler_riwayat.setAdapter(null);

            Dialog dialog               = new Dialog(HistoryReturStokActivity.this);
            dialog.show();
            dialog.setContentView(R.layout.loading);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            SimpleDateFormat format1    = new SimpleDateFormat("dd MMM yyyy");
            AndroidNetworking.post(Server.URL + "histori_stok_keluar")
                    .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("response").equals("200"))
                                {
                                    JSONArray array         = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++)
                                    {
                                        JSONObject object   = array.getJSONObject(i);

                                        String nama_bahan   = object.getString("nama_bahan");
                                        String tgl          = object.getString("tgl_create");
                                        String stok_keluar  = object.getString("stok_keluar");
                                        String name         = object.getString("name");
                                        String status       = object.getString("status");
                                        String keterangan   = object.getString("keterangan");

                                        String date         = tgl.substring(0, 10);
                                        String jam          = tgl.substring(11, 16);

                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                            Date date1      = format.parse(date);
                                            tgl1            = format1.format(date1);
    //                                            Toast.makeText(LaporanTransaksiPertanggalActivity.this, format1.format(date1).toString()
    //                                                    , Toast.LENGTH_SHORT).show();

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        ListRiwayatRetur o  = new ListRiwayatRetur(
                                                nama_bahan,
                                                tgl1 + "   " + jam,
                                                stok_keluar,
                                                name,
                                                keterangan,
                                                status
                                        );

                                        listRiwayatUpdateStok.add(o);


                                    }

                                    adapterStok             = new AdapterStokRetur(HistoryReturStokActivity.this, listRiwayatUpdateStok);
                                    adapterStok.notifyDataSetChanged();

                                    recycler_riwayat.setAdapter(adapterStok);
                                    dialog.dismiss();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    FancyToast.makeText(HistoryReturStokActivity.this,
                                            response.getString("message"),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.INFO,
                                            false)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                SingOut signOut = new SingOut();
                                signOut.Logout(HistoryReturStokActivity.this);
                            }
                            else {
                                dialog.dismiss();
                            }
                        }
                    });
}

    private void show_tutorial(){
        new GuideView.Builder(this)
                .setTitle("Status Pengembalian Bahan")
                .setContentText("Anda bisa beralih ke kategori lain yang telah tersedia")
                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_status))
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(HistoryReturStokActivity.this)
                                .setTitle("Nama Bahan")
                                .setContentText("")
                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_nama_bahan_update))
                                .setContentTextSize(16)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(HistoryReturStokActivity.this)
                                                .setTitle("Nama Pemohon Pengembalian Bahan")
                                                .setContentText("")
                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_kasir))
                                                .setContentTextSize(12)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        new GuideView.Builder(HistoryReturStokActivity.this)
                                                                .setTitle("Keterangan Pengembalian Bahan")
                                                                .setContentText("")
                                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_keterangan))
                                                                .setContentTextSize(12)//optional
                                                                .setTitleTextSize(20)//optional
                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {

                                                                        new GuideView.Builder(HistoryReturStokActivity.this)
                                                                                .setTitle("Tanggal Pengembalian Bahan")
                                                                                .setContentText("")
                                                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_tanggal_update))
                                                                                .setContentTextSize(12)//optional
                                                                                .setTitleTextSize(20)//optional
                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                .setGuideListener(new GuideListener() {
                                                                                    @Override
                                                                                    public void onDismiss(View view) {

                                                                                        new GuideView.Builder(HistoryReturStokActivity.this)
                                                                                                .setTitle("Jumlah Bahan Dikembalikan")
                                                                                                .setContentText("")
                                                                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_jml_update))
                                                                                                .setContentTextSize(12)//optional
                                                                                                .setTitleTextSize(20)//optional
                                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                                .setGuideListener(new GuideListener() {
                                                                                                    @Override
                                                                                                    public void onDismiss(View view) {

                                                                                                        tutorial.setEnabled(true);

                                                                                                    }
                                                                                                })
                                                                                                .build()
                                                                                                .show();

                                                                                    }
                                                                                })
                                                                                .build()
                                                                                .show();

                                                                    }
                                                                })
                                                                .build()
                                                                .show();

                                                    }
                                                })
                                                .build()
                                                .show();

                                    }
                                })
                                .build()
                                .show();
                    }

                })
                .build()
                .show();
    }
}
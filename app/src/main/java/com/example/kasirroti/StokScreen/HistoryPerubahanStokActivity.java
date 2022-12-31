package com.example.kasirroti.StokScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterStokUpdate;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListRiwayatUpdateStok;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;

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

public class HistoryPerubahanStokActivity extends AppCompatActivity {

    private RecyclerView recycler_riwayat;
    private ArrayList<ListRiwayatUpdateStok> listRiwayatUpdateStok  = new ArrayList<>();
    private AdapterStokUpdate adapterStok;

    ImageView img_back, img_retur, tutorial;

    View retur;

    String tgl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_perubahan_stok);

        tutorial                    = findViewById(R.id.tutorial);
        img_back                    = findViewById(R.id.img_back);
        img_retur                   = findViewById(R.id.img_retur);

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_riwayat.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_tutorial();
                    }
                }, 200);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent       = new Intent(HistoryPerubahanStokActivity.this, StokKeluarActivity.class);
                startActivity(intent);
            }
        });

        recycler_riwayat            = findViewById(R.id.recycler_riwayat);
        recycler_riwayat.setItemAnimator(new DefaultItemAnimator());
        recycler_riwayat.setLayoutManager(new LinearLayoutManager(this));
        recycler_riwayat.setHasFixedSize(true);

        getRiwayat();
    }

    public void getRiwayat(){
        Dialog dialog               = new Dialog(HistoryPerubahanStokActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        SimpleDateFormat format1    = new SimpleDateFormat("dd MMM yyyy");;

        AndroidNetworking.post(Server.URL + "histori_stok_masuk")
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

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);

                                    String nama_bahan   = object.getString("nama_bahan");
                                    String tgl          = object.getString("tgl");
                                    String stok_masuk   = object.getString("stok_masuk");
                                    String name         = object.getString("name");

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

                                    if (!stok_masuk.equals("0"))
                                    {
                                        ListRiwayatUpdateStok data  = new ListRiwayatUpdateStok(
                                                nama_bahan,
                                                tgl1 + "    " + jam,
                                                stok_masuk,
                                                name
                                        );

                                        listRiwayatUpdateStok.add(data);
                                    }
                                }

                                adapterStok             = new AdapterStokUpdate(HistoryPerubahanStokActivity.this, listRiwayatUpdateStok);
                                adapterStok.notifyDataSetChanged();

                                recycler_riwayat.setAdapter(adapterStok);
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.dismiss();
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
                            signOut.Logout(HistoryPerubahanStokActivity.this);
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });

    }

    private void show_tutorial(){

        new GuideView.Builder(HistoryPerubahanStokActivity.this)
                .setTitle("Nama penambah stok")
                .setContentText("")
                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_kasir))
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(HistoryPerubahanStokActivity.this)
                                .setTitle("Tanggal penambahan")
                                .setContentText("")
                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_tanggal_update))
                                .setContentTextSize(12)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(HistoryPerubahanStokActivity.this)
                                                .setTitle("Nama Bahan")
                                                .setContentText("")
                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_nama_bahan_update))
                                                .setContentTextSize(12)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        new GuideView.Builder(HistoryPerubahanStokActivity.this)
                                                                .setTitle("Jumlah Bahan Ditambahkan")
                                                                .setContentText("")
                                                                .setTargetView(recycler_riwayat.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_jml_update))
                                                                .setContentTextSize(12)//optional
                                                                .setTitleTextSize(20)//optional
                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {

                                                                        tutorial.setEnabled(true);
                                                                        new GuideView.Builder(HistoryPerubahanStokActivity.this)
                                                                                .setTitle("Menu Retur Stok")
                                                                                .setContentText("Anda bisa meminta pengembalian bahan pada menu retur stok")
                                                                                .setTargetView(img_retur)
                                                                                .setContentTextSize(16)//optional
                                                                                .setTitleTextSize(20)//optional
                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                .setGuideListener(new GuideListener() {
                                                                                    @Override
                                                                                    public void onDismiss(View view) {

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
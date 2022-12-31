package com.example.kasirroti.StokScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.DataSqlite.BahanOutletSqlite;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.bantuan.AdapterStokKeluar;
import com.example.kasirroti.bantuan.ListStokKeluar;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StokKeluarActivity extends AppCompatActivity {

    TextView hotlist_hot, txt_title;
    private RecyclerView list_stok;
    private ArrayList<ListStokKeluar> listStok;
    private AdapterStokKeluar adapter;

    private ImageView hotlist_bell, img_kiri, tutorial;

    public static ProgressDialog dialog;

    String tipe, status;

    SqliteHelper sqliteHelper;
    ArrayList<BahanOutletSqlite> arrayList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_keluar);
        txt_title       = findViewById(R.id.txt_title);

        hotlist_hot     = findViewById(R.id.hotlist_hot);
        hotlist_bell    = findViewById(R.id.hotlist_bell);
        img_kiri        = findViewById(R.id.img_kiri);
        tutorial        = findViewById(R.id.tutorial);

        dialog           = new ProgressDialog(StokKeluarActivity.this);

        sqliteHelper    = new SqliteHelper(this);
        arrayList       = sqliteHelper.readAllBahanOutletSqlite();

        img_kiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        hotlist_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StokKeluarActivity.this, HistoryReturStokActivity.class));

            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_tutorial();
            }
        });

        listStok                    = new ArrayList<>();
        list_stok                   = findViewById(R.id.list_stok);
        list_stok.setLayoutManager(new LinearLayoutManager(this));
        list_stok.setItemAnimator(new DefaultItemAnimator());
        list_stok.setHasFixedSize(true);

        set_stok();
        get_data_proses();
    }

    private void set_stok() {
        listStok.clear();
        list_stok.setAdapter(null);

        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title          = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengambil\nData Bahan");

        AndroidNetworking.post(Server.URL + "stok_manajemen" +
                "")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
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

                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object   = array.getJSONObject(i);

                                    ListStokKeluar data  = new ListStokKeluar(
                                            object.getString("id_stok"),
                                            object.getString("id_bahan"),
                                            object.getString("stok"),
                                            object.getString("nama_bahan")
                                    );

                                    listStok.add(data);

                                    adapter             = new AdapterStokKeluar(StokKeluarActivity.this, listStok);
                                    adapter.notifyDataSetChanged();

                                    adapter.setOnItemClickListener(new AdapterStokKeluar.recyclerViewClickListener() {
                                        @Override
                                        public void onClick(View v, int position) {
                                            retur(listStok, position);
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    list_stok.setAdapter(adapter);

                                    dialog.dismiss();
                                }
                            }
                            else
                            {
                                dialog.dismiss();
                                FancyToast.makeText(StokKeluarActivity.this,  "Data Kosong",
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(StokKeluarActivity.this,  "Masalah Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(StokKeluarActivity.this,  "Masalah Jaringan!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void retur(ArrayList<ListStokKeluar> listStok, int position) {
        Log.d("StokBahan", sqliteHelper.readStokSqlite(listStok.get(position).getId_bahan()));
        AlertDialog.Builder builder = new AlertDialog.Builder(StokKeluarActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_stok, null);
        builder.setCancelable(true);
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();

        TextView txt_kode_stok_dialog   = view.findViewById(R.id.txt_kode_stok_dialog);
        TextView txt_aaa                = view.findViewById(R.id.txt_aaa);
        TextView txt_nama_bahan         = view.findViewById(R.id.txt_nama_bahan);
        TextView txt_jml_skrg           = view.findViewById(R.id.txt_jml_skrg);
        TextView txt_ok                 = view.findViewById(R.id.txt_ok);
        EditText edt_nama_pembeli       = view.findViewById(R.id.edt_cari_nomor_wa);
        EditText edt_keterangan         = view.findViewById(R.id.edt_keterangan);
        LinearLayout L_ket              = view.findViewById(R.id.L_ket);

        ImageView img_stok_min          = view.findViewById(R.id.img_stok_min);
        ImageView img_stok_plus         = view.findViewById(R.id.img_stok_plus);

        L_ket.setVisibility(View.VISIBLE);
        img_stok_min.setVisibility(View.GONE);

        txt_aaa.setText("Retur Bahan");
        edt_nama_pembeli.setText("0");
        txt_kode_stok_dialog.setText(listStok.get(position).getNama_bahan().substring(0, 2));
        txt_nama_bahan.setText(listStok.get(position).getNama_bahan());
        txt_jml_skrg.setText(listStok.get(position).getStok());

        int jml_awal                    = Integer.parseInt(listStok.get(position).getStok());

        img_stok_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jml                 = Integer.parseInt(edt_nama_pembeli.getText().toString());
                int jml_final           = jml + 1;

                edt_nama_pembeli.setText(String.valueOf(jml_final));
                img_stok_min.setVisibility(View.VISIBLE);

                if (jml_final >= jml_awal)
                {
                    img_stok_plus.setVisibility(View.GONE);
                }
                else
                {
                    img_stok_plus.setVisibility(View.VISIBLE);
                }
            }
        });

        img_stok_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jml                 = Integer.parseInt(edt_nama_pembeli.getText().toString());
                int jml_final           = jml - 1;

                edt_nama_pembeli.setText(String.valueOf(jml_final));

                if (Integer.parseInt(edt_nama_pembeli.getText().toString()) <= 0)
                {
                    img_stok_min.setVisibility(View.GONE);
                }
                else
                {
                    img_stok_min.setVisibility(View.VISIBLE);
                    img_stok_plus.setVisibility(View.VISIBLE);
                }

            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keterangan               = edt_keterangan.getText().toString();
                if (keterangan.isEmpty()){
                    FancyToast.makeText(
                            StokKeluarActivity.this,
                            "Mohon isi keterangan!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            false
                    ).show();
                }
                else if (edt_nama_pembeli.getText().toString().equals("0")){
                    FancyToast.makeText(
                            StokKeluarActivity.this,
                            "Mohon Masukkan Jumlah!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            false
                    ).show();
                }
                else {
                    txt_ok.setEnabled(false);
                    dialog.dismiss();
                    req_retur(listStok, edt_nama_pembeli.getText().toString(), keterangan, position);
                }
            }
        });

    }

    private void req_retur(ArrayList<ListStokKeluar> listStok, String jml, String keterangan, int position){
        Dialog dialog1              = new Dialog(StokKeluarActivity.this);
        dialog1.show();
        dialog1.setContentView(R.layout.loading);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);

        TextView txt_title          = dialog1.findViewById(R.id.txt_title);
        txt_title.setText("Mengirim Permintaan");

        AndroidNetworking.post(Server.URL + "stok_keluar")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .addBodyParameter("id_bahan", listStok.get(position).getId_bahan())
                .addBodyParameter("stok_keluar", jml)
                .addBodyParameter("keterangan", keterangan)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                if (arrayList.size() != 0) {
                                    sqliteHelper.updateStokBahan(
                                            String.valueOf(Integer.parseInt(sqliteHelper.readStokSqlite(listStok.get(position).getId_bahan())) - Integer.parseInt(jml)),
                                            listStok.get(position).getId_bahan()
                                    );
                                }
                                set_stok();
                                get_data_proses();
                            }
                            else{
                                set_stok();FancyToast.makeText(
                                        StokKeluarActivity.this,
                                        response.getString("message"),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                            }
                            dialog1.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog1.dismiss();
                            set_stok();FancyToast.makeText(
                                    StokKeluarActivity.this,
                                    "Kesalahan request!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog1.dismiss();
                        set_stok();FancyToast.makeText(
                                StokKeluarActivity.this,
                                "Kesalahan jaringan!",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                        ).show();
                        Log.d("Test", String.valueOf(anError.getErrorCode()));
                    }
                });
    }

    private void get_data_proses(){
        Dialog dialog1              = new Dialog(StokKeluarActivity.this);
        dialog1.show();
        dialog1.setContentView(R.layout.loading);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);

        ArrayList<String> arrayStatus   = new ArrayList<>();

        AndroidNetworking.post(Server.URL + "histori_stok_keluar")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
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

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    status = object.getString("status");

                                    if (status.equals("proses")) {
                                        arrayStatus.add(status);
                                    }
                                }

                                hotlist_hot.setText(String.valueOf(arrayStatus.size()));
                                hotlist_hot.setVisibility(View.VISIBLE);

                                dialog1.dismiss();
                            }
                            else
                            {
                                dialog1.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog1.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog1.dismiss();

                    }
                });
    }

    private void show_tutorial(){
        new TapTargetSequence(StokKeluarActivity.this)
                .targets(
                        TapTarget.forView(hotlist_bell, "Riwayat Pengurangan Stok", "Berisi riwayat serta rincian pengurangan stok bahan")
                                .outerCircleColor(android.R.color.holo_orange_dark)
                                .outerCircleAlpha(1f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.black)
                                .textColor(android.R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(20),

                        TapTarget.forView(list_stok.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_nama_stok), "Nama Stok Bahan", "")
                                .outerCircleColor(android.R.color.holo_orange_dark)
                                .outerCircleAlpha(1f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.black)
                                .textColor(android.R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(40),

                        TapTarget.forView(list_stok.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_jml_stok), "Jumlah Stok Bahan!", "")
                                .outerCircleColor(android.R.color.holo_orange_dark)
                                .outerCircleAlpha(1f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.black)
                                .textColor(android.R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(40)).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                final Dialog dialog     = new Dialog(StokKeluarActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_tutorial_tambah_stok);

                TextView txt_title          = dialog.findViewById(R.id.txt_title);
                TextView txt_step           = dialog.findViewById(R.id.txt_step);
                TextView txt_note           = dialog.findViewById(R.id.txt_note);

                txt_title.setText("Langkah - langkah pengurangan stok bahan");
                txt_step.setText(R.string.step_retur);
                txt_note.setVisibility(View.GONE);

                FloatingActionButton fab    = dialog.findViewById(R.id.fab_tutup);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnitmation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}